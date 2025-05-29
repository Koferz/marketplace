package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import helpers.fail
import models.State
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErr
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErrWithData
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker

fun ICorChainDsl<Context>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление депозита в БД"
    on { state == State.RUNNING }
    handle {
        val request = DbDepositRequest(depositRepoPrepare)
        when(val result = depositRepo.createDeposit(request)) {
            is DbDepositResponseOk -> depositRepoDone = result.data
            is DbDepositResponseErr -> fail(result.errors)
            is DbDepositResponseErrWithData -> fail(result.errors)
        }
    }
}
