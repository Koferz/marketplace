package ru.otus.otuskotlin.yieldInsights.biz.general

import Context
import models.Command
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.chain


fun ICorChainDsl<Context>.operation(
    title: String,
    command: Command,
    block: ICorChainDsl<Context>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}
