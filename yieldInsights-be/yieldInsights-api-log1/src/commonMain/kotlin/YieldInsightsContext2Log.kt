package ru.otus.yieldInsights.marketplace.api.log1.mapper

import Context
import kotlinx.datetime.Clock
import models.*
import ru.otus.yieldInsights.api.log1.models.*

fun Context.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "yieldInsights",
    deposit = toLog(),
    errors = errors.map { it.toLog() },
)

private fun Context.toLog(): YieldInsightsLogModel? {
    val depositNone = Deposit()
    return YieldInsightsLogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        requestDeposit = depositRequest.takeIf { it != depositNone }?.toLog(),
        responseDeposit = depositResponse.takeIf { it != depositNone }?.toLog(),
        responseDeposits = depositsResponse.takeIf { it.isNotEmpty() }?.filter { it != depositNone }?.map { it.toLog() },
        requestFilter = depositFilterRequest.takeIf { it != DepositFilter() }?.toLog(),
    ).takeIf { it != YieldInsightsLogModel() }
}

private fun DepositFilter.toLog() = DepositFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },

)

private fun DepositError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun Deposit.toLog() = DepositLog(
    id = depositId.takeIf { it != DepositId.NONE }?.asString(),
    bankName = bankName.takeIf { it != DepositOrgName.NONE }?.asString(),
    depositName = depositName.takeIf { it.isNotBlank() },
    rate = rate.takeIf { it.isNotBlank() },
    openingDate = openingDate.takeIf { it.isNotBlank() },
    depositTerm = depositTerm.takeIf { it.isNotBlank() },
    depositAmount = depositAmount.takeIf { it.isNotBlank() },
    depositOperation = depositOperation.toString(),
    isActive = isActive.toString()
)
