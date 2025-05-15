import models.Deposit
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.yieldInsights.common.repo.*

class DepositRepositoryMock(
    private val invokeCreateDeposit: (DbDepositRequest) -> IDbDepositResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadDeposit: (DbDepositIdRequest) -> IDbDepositResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateDeposit: (DbDepositRequest) -> IDbDepositResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteDeposit: (DbDepositIdRequest) -> IDbDepositResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchDeposit: (DbDepositFilterRequest) -> IDbDepositsResponse = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepoDeposit {
    override suspend fun createDeposit(rq: DbDepositRequest): IDbDepositResponse {
        return invokeCreateDeposit(rq)
    }

    override suspend fun readDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
        return invokeReadDeposit(rq)
    }

    override suspend fun updateDeposit(rq: DbDepositRequest): IDbDepositResponse {
        return invokeUpdateDeposit(rq)
    }

    override suspend fun deleteDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
        return invokeDeleteDeposit(rq)
    }

    override suspend fun searchDeposit(rq: DbDepositFilterRequest): IDbDepositsResponse {
        return invokeSearchDeposit(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbDepositResponseOk(Deposit())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbDepositsResponseOk(emptyList())
    }
}
