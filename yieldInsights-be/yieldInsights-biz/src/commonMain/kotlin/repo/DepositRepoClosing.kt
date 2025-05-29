package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import helpers.fail
import models.State
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositFilterRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositsResponseOk
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoClosing(rate: String) = worker {
    this.title = rate
    description = "Поиск предложений для вклада по ставке"
    on { state == State.RUNNING }
    handle {
        val depositRequest = depositRepoPrepare
        val filter = DbDepositFilterRequest(rateFilter = depositRequest.rate)

        when (val dbResponse = depositRepo.searchDeposit(filter)) {
            is DbDepositsResponseOk -> depositsRepoDone = dbResponse.data.toMutableList()
            is DbDepositsResponseErr -> fail(dbResponse.errors)
        }
    }
}
