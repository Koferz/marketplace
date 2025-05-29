import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit
import ru.otus.otuskotlin.yieldInsights.common.repo.*


class DepositRepoStub() : IRepoDeposit {
    override suspend fun createDeposit(rq: DbDepositRequest): IDbDepositResponse {
        return DbDepositResponseOk(
            data = DepositStub.get(),
        )
    }

    override suspend fun readDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
        return DbDepositResponseOk(
            data = DepositStub.get(),
        )
    }

    override suspend fun updateDeposit(rq: DbDepositRequest): IDbDepositResponse {
        return DbDepositResponseOk(
            data = DepositStub.get(),
        )
    }

    override suspend fun deleteDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
        return DbDepositResponseOk(
            data = DepositStub.get(),
        )
    }

    override suspend fun searchDeposit(rq: DbDepositFilterRequest): IDbDepositsResponse {
        return DbDepositsResponseOk(
            data = DepositStub.prepareSearchList(filter = "", "18"),
        )
    }
}
