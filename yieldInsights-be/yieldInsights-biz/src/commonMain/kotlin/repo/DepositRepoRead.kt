package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import helpers.fail
import models.State
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositIdRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErr
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErrWithData
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение депозита из БД"
    on { state == State.RUNNING }
    handle {
        val request = DbDepositIdRequest(depositValidated)
        when(val result = depositRepo.readDeposit(request)) {
            is DbDepositResponseOk -> depositRepoRead = result.data
            is DbDepositResponseErr -> fail(result.errors)
            is DbDepositResponseErrWithData -> {
                fail(result.errors)
                depositRepoRead = result.data
            }
        }
    }
}
