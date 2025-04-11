package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import helpers.fail
import models.DepositError
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker

fun ICorChainDsl<Context>.stubNoCase(title: String) = worker{
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        fail(
            DepositError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
