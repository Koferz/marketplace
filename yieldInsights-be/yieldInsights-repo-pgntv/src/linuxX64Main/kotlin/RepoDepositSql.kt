package ru.otus.otuskotlin.yieldInsights.repo.postgresql.ntv

import IRepoDepositInitializable
import com.benasher44.uuid.uuid4
import io.github.moreirasantos.pgkn.PostgresDriver
import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.coroutines.runBlocking
import models.Deposit
import models.DepositId
import models.DepositLock
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit
import ru.otus.otuskotlin.yieldInsights.common.repo.*
import ru.otus.otuskotlin.yieldInsights.repo.postgresql.SqlProperties
import ru.otus.otuskotlin.yieldInsights.repo.postgresql.ntv.SqlFields.quoted

class RepoDepositSql(
    properties: SqlProperties = SqlProperties(),
    val randomUuid: () -> String = { uuid4().toString() },
) : IRepoDeposit, IRepoDepositInitializable {
    init {
        require(properties.database.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL database must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.schema.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL schema must contain only letters, numbers and underscore symbol '_'"
        }
        require(properties.table.matches(Regex("^[\\w\\d_]+\$"))) {
            "PostgreSQL table must contain only letters, numbers and underscore symbol '_'"
        }
    }

    private val dbName: String = "\"${properties.schema}\".\"${properties.table}\"".apply {
        // Валидируем, что админ не ошибся в имени таблицы
    }

    init {
        initConnection(properties)
    }

    // insert into db (id) VALUES (:id) returning id
    private suspend fun saveElement(savedeposit: Deposit): IDbDepositResponse {
        val sql = """
                INSERT INTO $dbName (
                  ${SqlFields.DEPOSIT_ID.quoted()}, 
                  ${SqlFields.BANK_NAME.quoted()}, 
                  ${SqlFields.DEPOSIT_NAME.quoted()},
                  ${SqlFields.RATE.quoted()},
                  ${SqlFields.OPENING_DATE.quoted()},
                  ${SqlFields.DEPOSIT_TERM.quoted()},
                  ${SqlFields.DEPOSIT_AMOUNT.quoted()},
                  ${SqlFields.DEPOSIT_OPERATION.quoted()},
                  ${SqlFields.IS_ACTIVE.quoted()},
                  ${SqlFields.LOCK.quoted()},
                  ${SqlFields.OWNER_ID.quoted()}
                ) VALUES (
                  :${SqlFields.DEPOSIT_ID}, 
                  :${SqlFields.BANK_NAME}, 
                  :${SqlFields.DEPOSIT_NAME},
                  :${SqlFields.RATE},
                  :${SqlFields.OPENING_DATE},  
                  :${SqlFields.DEPOSIT_TERM},
                  :${SqlFields.DEPOSIT_AMOUNT},
                  :${SqlFields.DEPOSIT_OPERATION}::${SqlFields.DEPOSIT_OPERATION_TYPE}, 
                  :${SqlFields.IS_ACTIVE}::${SqlFields.IS_ACTIVE_TYPE}, 
                  :${SqlFields.LOCK}, 
                  :${SqlFields.OWNER_ID}
                )
                RETURNING ${SqlFields.allFields.joinToString()}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            savedeposit.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbDepositResponseOk(res.first())
    }

    override fun save(deposits: Collection<Deposit>): Collection<Deposit> = runBlocking {
        deposits.map {
            val res = saveElement(it)
            if (res !is DbDepositResponseOk) throw Exception()
            res.data
        }
    }

    override suspend fun createDeposit(rq: DbDepositRequest): IDbDepositResponse {
        val saveAd = rq.deposit.copy(depositId = DepositId(randomUuid()), lock = DepositLock(randomUuid()))
        return saveElement(saveAd)
    }

    override suspend fun readDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName
                WHERE ${SqlFields.DEPOSIT_ID.quoted()} = :${SqlFields.DEPOSIT_ID}
            """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return if (res.isEmpty()) errorNotFound(rq.id) else DbDepositResponseOk(res.first())
    }

    override suspend fun updateDeposit(rq: DbDepositRequest): IDbDepositResponse {
        val sql = """
            WITH update_obj AS (
                UPDATE $dbName a
                SET ${SqlFields.DEPOSIT_ID.quoted()} = :${SqlFields.DEPOSIT_ID}
                , ${SqlFields.BANK_NAME.quoted()} = :${SqlFields.BANK_NAME}
                , ${SqlFields.DEPOSIT_NAME.quoted()} = :${SqlFields.DEPOSIT_NAME}
                , ${SqlFields.RATE.quoted()} = :${SqlFields.RATE}
                , ${SqlFields.OPENING_DATE.quoted()} = :${SqlFields.OPENING_DATE}
                , ${SqlFields.DEPOSIT_TERM.quoted()} = :${SqlFields.DEPOSIT_TERM}
                , ${SqlFields.DEPOSIT_AMOUNT.quoted()} = :${SqlFields.DEPOSIT_AMOUNT}
                , ${SqlFields.DEPOSIT_OPERATION.quoted()} = :${SqlFields.DEPOSIT_OPERATION}::${SqlFields.DEPOSIT_OPERATION_TYPE}
                , ${SqlFields.IS_ACTIVE.quoted()} = :${SqlFields.IS_ACTIVE}::${SqlFields.IS_ACTIVE_TYPE}
                , ${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK}
                , ${SqlFields.OWNER_ID.quoted()} = :${SqlFields.OWNER_ID}
                WHERE  a.${SqlFields.DEPOSIT_ID.quoted()} = :${SqlFields.DEPOSIT_ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING ${SqlFields.allFields.joinToString()}
            ),
            select_obj AS (
                SELECT ${SqlFields.allFields.joinToString()} FROM $dbName 
                WHERE ${SqlFields.DEPOSIT_ID.quoted()} = :${SqlFields.DEPOSIT_ID} 
            )
            (SELECT * FROM update_obj UNION ALL SELECT * FROM select_obj) LIMIT 1
        """.trimIndent()
        val rqAd = rq.deposit
        val newAd = rqAd.copy(lock = DepositLock(randomUuid()))
        val res = driver.execute(
            sql = sql,
            newAd.toDb() + rqAd.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        val returnedAd: Deposit? = res.firstOrNull()
        return when {
            returnedAd == null -> errorNotFound(rq.deposit.depositId)
            returnedAd.lock == newAd.lock -> DbDepositResponseOk(returnedAd)
            else -> errorRepoConcurrency(returnedAd, rqAd.lock)
        }
    }

    override suspend fun deleteDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
        val sql = """
            WITH delete_obj AS (
                DELETE FROM $dbName a
                WHERE  a.${SqlFields.DEPOSIT_ID.quoted()} = :${SqlFields.DEPOSIT_ID}
                AND a.${SqlFields.LOCK.quoted()} = :${SqlFields.LOCK_OLD}
                RETURNING '${SqlFields.DELETE_OK}'
            )
            SELECT ${SqlFields.allFields.joinToString()}, (SELECT * FROM delete_obj) as flag FROM $dbName 
            WHERE ${SqlFields.DEPOSIT_ID.quoted()} = :${SqlFields.DEPOSIT_ID} 
        """.trimIndent()
        val res = driver.execute(
            sql = sql,
            rq.id.toDb() + rq.lock.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) to row.getString(SqlFields.allFields.size) }
        val returnedPair: Pair<Deposit,String?>? = res.firstOrNull()
        val returnedAd: Deposit? = returnedPair?.first
        return when {
            returnedAd == null -> errorNotFound(rq.id)
            returnedPair.second == SqlFields.DELETE_OK -> DbDepositResponseOk(returnedAd)
            else -> errorRepoConcurrency(returnedAd, rq.lock)
        }
    }

    override suspend fun searchDeposit(rq: DbDepositFilterRequest): IDbDepositsResponse {
        val where = listOfNotNull(
            rq.rateFilter.takeIf { it.isNotBlank() }
                ?.let { "${SqlFields.RATE.quoted()} LIKE :${SqlFields.RATE}" },
        )
            .takeIf { it.isNotEmpty() }
            ?.let { "WHERE ${it.joinToString(separator = " AND ")}" }
            ?: ""

        val sql = """
                SELECT ${SqlFields.allFields.joinToString { it.quoted() }}
                FROM $dbName $where
            """.trimIndent()
        println("SQL: $sql")
        val res = driver.execute(
            sql = sql,
            rq.toDb(),
        ) { row: ResultSet -> row.fromDb(SqlFields.allFields) }
        return DbDepositsResponseOk(res)
    }

    fun clear(): Unit = runBlocking {
        val sql = """
                DELETE FROM $dbName 
            """.trimIndent()
        driver.execute(sql = sql)
    }

    companion object {
        private lateinit var driver: PostgresDriver
        private fun initConnection(properties: SqlProperties) {
            if (!this::driver.isInitialized) {
                driver = PostgresDriver(
                    host = properties.host,
                    port = properties.port,
                    user = properties.user,
                    database = properties.database,
                    password = properties.password,
                )
            }
        }
    }
}
