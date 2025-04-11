package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { depositValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
