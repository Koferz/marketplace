package ru.otus.otuskotlin.marketplace.biz.stubs

import Context
import CorSettings
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs


fun ICorChainDsl<Context>.stubDeleteSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления вклада
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = DepositStub.prepareResult {
                depositRequest.depositName.takeIf { it.isNotBlank() }?.also { this.depositName = it }
            }
            depositResponse = stub
        }
    }
}
