package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import models.State
import helpers.fail
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositFilterRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositsResponseOk
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск депозита в БД по фильтру"
    on { state == State.RUNNING }
    handle {
        val request = DbDepositFilterRequest(
            rateFilter = depositFilterValidated.searchString
        )
        when(val result = depositRepo.searchDeposit(request)) {
            is DbDepositsResponseOk -> depositsRepoDone = result.data.toMutableList()
            is DbDepositsResponseErr -> fail(result.errors)
        }
    }
}
