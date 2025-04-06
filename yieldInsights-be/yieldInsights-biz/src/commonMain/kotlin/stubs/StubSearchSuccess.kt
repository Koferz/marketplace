package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import CorSettings
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs

fun ICorChainDsl<Context>.stubSearchSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска вклада
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            depositsResponse.addAll(DepositStub.prepareSearchList(depositFilterRequest.searchString, depositFilterRequest.depositRate))
        }
    }
}
