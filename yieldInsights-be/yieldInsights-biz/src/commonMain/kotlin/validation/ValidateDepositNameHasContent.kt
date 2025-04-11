package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<Context>.validateDepositNameHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { depositValidating.depositName.isNotEmpty() && !depositValidating.depositName.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "depositName",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
