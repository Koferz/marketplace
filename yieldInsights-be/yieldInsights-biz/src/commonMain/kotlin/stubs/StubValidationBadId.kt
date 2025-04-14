package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import helpers.fail
import models.DepositError
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs

fun ICorChainDsl<Context>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора вклада
    """.trimIndent()
    on { stubCase == Stubs.BAD_ID && state == State.RUNNING }
    handle {
        fail(
            DepositError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
