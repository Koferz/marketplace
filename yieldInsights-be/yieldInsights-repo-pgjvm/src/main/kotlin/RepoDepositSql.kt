package ru.otus.otuskotlin.yieldInsights.repo.postgresql

import IRepoDepositInitializable
import com.benasher44.uuid.uuid4
import helpers.asDepositError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.Deposit
import models.DepositId
import models.DepositLock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.yieldInsights.common.repo.*

class RepoDepositSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : IRepoDeposit, IRepoDepositInitializable {
    private val adTable = DepositTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        adTable.deleteAll()
    }

    private fun saveObj(deposit: Deposit): Deposit = transaction(conn) {
        val res = adTable
            .insert {
                to(it, deposit, randomUuid)
            }
            .resultedValues
            ?.map { adTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbDepositResponse): IDbDepositResponse =
        transactionWrapper(block) { DbDepositResponseErr(it.asDepositError()) }

    override fun save(deps: Collection<Deposit>): Collection<Deposit> = deps.map { saveObj(it) }
    override suspend fun createDeposit(rq: DbDepositRequest): IDbDepositResponse = transactionWrapper {
        DbDepositResponseOk(saveObj(rq.deposit))
    }

    private fun read(id: DepositId): IDbDepositResponse {
        val res = adTable.selectAll().where {
            adTable.depositId eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbDepositResponseOk(adTable.from(res))
    }

    override suspend fun readDeposit(rq: DbDepositIdRequest): IDbDepositResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: DepositId,
        lock: DepositLock,
        block: (Deposit) -> IDbDepositResponse
    ): IDbDepositResponse =
        transactionWrapper {
            if (id == DepositId.NONE) return@transactionWrapper errorEmptyId

            val current = adTable.selectAll().where { adTable.depositId eq id.asString() }
                .singleOrNull()
                ?.let { adTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun updateDeposit(rq: DbDepositRequest): IDbDepositResponse = update(
        rq.deposit.depositId, rq.deposit.lock
    ) {
        adTable.update({ adTable.depositId eq rq.deposit.depositId.asString() }) {
            to(it, rq.deposit.copy(lock = DepositLock(randomUuid())), randomUuid)
        }
        read(rq.deposit.depositId)
    }

    override suspend fun deleteDeposit(rq: DbDepositIdRequest): IDbDepositResponse = update(rq.id, rq.lock) {
        adTable.deleteWhere { depositId eq rq.id.asString() }
        DbDepositResponseOk(it)
    }

    override suspend fun searchDeposit(rq: DbDepositFilterRequest): IDbDepositsResponse =
        transactionWrapper({
            val res = adTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.rateFilter.isNotBlank()) {
                        add(adTable.rate eq rq.rateFilter)
                    }
                }.reduce { a, b -> a and b }
            }
            DbDepositsResponseOk(data = res.map { adTable.from(it) })
        }, {
            DbAdsResponseErr(it.asDepositError())
        })
}
