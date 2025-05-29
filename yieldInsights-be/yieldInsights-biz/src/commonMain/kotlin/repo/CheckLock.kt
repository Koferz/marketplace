package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import helpers.fail
import models.State
import ru.otus.otuskotlin.yieldInsights.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == State.RUNNING && depositValidated.lock != depositRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(depositRepoRead, depositValidated.lock).errors)
    }
}
