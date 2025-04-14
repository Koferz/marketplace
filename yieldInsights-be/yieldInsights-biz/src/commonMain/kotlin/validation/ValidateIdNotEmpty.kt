package ru.otus.otuskotlin.marketplace.biz.validation

import Context
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { depositValidating.depositId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "depositId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
