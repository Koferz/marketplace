package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.chain


fun ICorChainDsl<Context>.validation(block: ICorChainDsl<Context>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == State.RUNNING }
}
