package ru.otus.otuskotlin.yieldInsights.common.repo

import helpers.errorSystem
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit

abstract class DepositRepoBase: IRepoDeposit {

    protected suspend fun tryDepositMethod(block: suspend () -> IDbDepositResponse) = try {
        block()
    } catch (e: Throwable) {
        DbAdResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryDepositsMethod(block: suspend () -> IDbDepositsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbAdsResponseErr(errorSystem("methodException", e = e))
    }

}
