package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import CorSettings
import models.DepositId
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs

fun ICorChainDsl<Context>.stubClosingSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для получения ближайших для закрытия вкладов
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubClosingSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            depositResponse = DepositStub.prepareResult {
                depositRequest.depositId.takeIf { it != DepositId.NONE }?.also { this.depositId = it }
            }
            depositsResponse.addAll(DepositStub.prepareClosingList(depositResponse.rate))
        }
    }
}
