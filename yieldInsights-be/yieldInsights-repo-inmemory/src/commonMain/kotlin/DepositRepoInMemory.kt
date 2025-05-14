package ru.otus.otuskotlin.yieldInsights.repo.inmemory

import IRepoDepositInitializable
import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.Deposit
import models.DepositId
import models.DepositLock
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.yieldInsights.common.repo.*
import ru.otus.otuskotlin.yieldInsights.common.repo.exceptions.RepoEmptyLockException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class DepositRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : DepositRepoBase(), IRepoDeposit, IRepoDepositInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, DepositEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(ads: Collection<Deposit>) = ads.map { ad ->
        val entity = DepositEntity(ad)
        require(entity.depositId != null)
        cache.put(entity.depositId, entity)
        ad
    }

    override suspend fun createDeposit(rq: DbDepositRequest): IDbDepositResponse = tryDepositMethod {
        val key = randomUuid()
        val deposit = rq.deposit.copy(depositId = DepositId(key), lock = DepositLock(randomUuid()))
        val entity = DepositEntity(deposit)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbDepositResponseOk(deposit)
    }

    override suspend fun readDeposit(rq: DbDepositIdRequest): IDbDepositResponse = tryDepositMethod {
        val key = rq.id.takeIf { it != DepositId.NONE }?.asString() ?: return@tryDepositMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbDepositResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateDeposit(rq: DbDepositRequest): IDbDepositResponse = tryDepositMethod {
        val rqAd = rq.deposit
        val id = rqAd.depositId.takeIf { it != DepositId.NONE } ?: return@tryDepositMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqAd.lock.takeIf { it != DepositLock.NONE } ?: return@tryDepositMethod errorEmptyLock(id)

        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                oldAd.lock == DepositLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldAd.lock != oldLock -> errorRepoConcurrency(oldAd, oldLock)
                else -> {
                    val newAd = rqAd.copy(lock = DepositLock(randomUuid()))
                    val entity = DepositEntity(newAd)
                    cache.put(key, entity)
                    DbDepositResponseOk(newAd)
                }
            }
        }
    }


    override suspend fun deleteDeposit(rq: DbDepositIdRequest): IDbDepositResponse = tryDepositMethod {
        val id = rq.id.takeIf { it != DepositId.NONE } ?: return@tryDepositMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != DepositLock.NONE } ?: return@tryDepositMethod errorEmptyLock(id)

        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                oldAd.lock == DepositLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldAd.lock != oldLock -> errorRepoConcurrency(oldAd, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbDepositResponseOk(oldAd)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchDeposit(rq: DbDepositFilterRequest): IDbDepositsResponse = tryDepositsMethod {
        val result: List<Deposit> = cache.asMap().asSequence()
            .filter { entry ->
                rq.rateFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.rate?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbDepositsResponseOk(result)
    }
}
