package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import helpers.errorValidation
import helpers.fail
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<Context>.validateDepositOpeningDateHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("^([0-9]{4})-([0-9]{2})-([0-9]{2})$")
    on { depositValidating.openingDate.isNotEmpty() && depositValidating.openingDate.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "openingDate",
                violationCode = "noContent",
                description = "field must contain numbers"
            )
        )
    }
}
