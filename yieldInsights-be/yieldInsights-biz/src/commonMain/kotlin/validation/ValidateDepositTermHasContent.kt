package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<Context>.validateDepositTermHasContent(title: String) = worker {
    this.title = title
    on { depositValidating.depositTerm.isNotEmpty() && depositValidating.depositTerm.toIntOrNull() != null }
    handle {
        fail(
            errorValidation(
                field = "depositTerm",
                violationCode = "noContent",
                description = "field must contain numbers"
            )
        )
    }
}
