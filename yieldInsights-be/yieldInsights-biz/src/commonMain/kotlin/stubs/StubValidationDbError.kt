package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import helpers.fail
import models.DepositError
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker
import stubs.Stubs

fun ICorChainDsl<Context>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == Stubs.DB_ERROR && state == State.RUNNING }
    handle {
        fail(
            DepositError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
