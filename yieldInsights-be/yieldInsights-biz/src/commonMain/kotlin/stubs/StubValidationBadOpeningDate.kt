package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import helpers.fail
import models.DepositError
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs

fun ICorChainDsl<Context>.stubValidationBadOpeningDate(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для opening_date
    """.trimIndent()
    on { stubCase == Stubs.BAD_OPENING_DATE && state == State.RUNNING }
    handle {
        fail(
            DepositError(
                group = "validation",
                code = "validation-opening_date",
                field = "opening_date",
                message = "Wrong opening_date field"
            )
        )
    }
}
