package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import CorSettings
import models.DepositOperation
import models.DepositOrgName
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs


fun ICorChainDsl<Context>.stubCreateSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания вклада
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = DepositStub.prepareResult {
                depositRequest.bankName.takeIf { it != DepositOrgName.NONE }?.also { this.bankName = it }
                depositRequest.depositName.takeIf { it.isNotBlank() }?.also { this.depositName = it }
                depositRequest.rate.takeIf { it.isNotBlank() }?.also { this.rate = it }
                depositRequest.openingDate.takeIf { it.isNotBlank() }?.also { this.openingDate = it }
                depositRequest.depositTerm.takeIf { it.isNotBlank() }?.also { this.depositTerm = it }
                depositRequest.depositAmount.takeIf { it.isNotBlank() }?.also { this.depositAmount = it }
                depositRequest.depositOperation.takeIf { it != DepositOperation.PROLONGATION }?.also { depositOperation = it }
            }
            depositResponse = stub
        }
    }
}
