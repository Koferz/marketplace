package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.finishValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        depositValidated = depositValidating
    }
}

fun ICorChainDsl<Context>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        depositFilterValidated = depositFilterValidating
    }
}
