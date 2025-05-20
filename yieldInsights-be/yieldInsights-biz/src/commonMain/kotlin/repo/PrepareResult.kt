package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import models.State
import models.WorkMode
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != WorkMode.STUB }
    handle {
        depositResponse = depositRepoDone
        depositsResponse = depositsRepoDone
        state = when (val st = state) {
            State.RUNNING -> State.FINISHING
            else -> st
        }
    }
}
