package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        depositRepoPrepare = depositValidated.deepCopy()
    }
}
