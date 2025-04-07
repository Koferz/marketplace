package ru.otus.otuskotlin.marketplace.biz.stubs

import Context
import helpers.fail
import models.DepositError
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs


fun ICorChainDsl<Context>.stubValidationBadDepositTerm(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для depositTerm
    """.trimIndent()

    on { stubCase == Stubs.BAD_DEPOSIT_TERM && state == State.RUNNING }
    handle {
        fail(
            DepositError(
                group = "validation",
                code = "validation-depositTerm",
                field = "depositTerm",
                message = "Wrong depositTerm field"
            )
        )
    }
}
