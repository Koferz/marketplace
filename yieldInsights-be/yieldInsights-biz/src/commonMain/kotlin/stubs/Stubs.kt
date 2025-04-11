package ru.otus.otuskotlin.yieldInsights.biz.stubs

import Context
import models.State
import models.WorkMode
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.chain


fun ICorChainDsl<Context>.stubs(title: String, block: ICorChainDsl<Context>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == WorkMode.STUB && state == State.RUNNING }
}
