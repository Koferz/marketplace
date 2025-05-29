package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import helpers.fail
import models.State
import ru.otus.otuskotlin.yieldInsights.common.repo.*
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление вклада из БД по ID"
    on { state == State.RUNNING }
    handle {
        val request = DbDepositIdRequest(depositRepoPrepare)
        when(val result = depositRepo.deleteDeposit(request)) {
            is DbDepositResponseOk -> depositRepoDone = result.data
            is DbDepositResponseErr -> {
                fail(result.errors)
                depositRepoDone = depositRepoRead
            }
            is DbDepositResponseErrWithData -> {
                fail(result.errors)
                depositRepoDone = result.data
            }
        }
    }
}
