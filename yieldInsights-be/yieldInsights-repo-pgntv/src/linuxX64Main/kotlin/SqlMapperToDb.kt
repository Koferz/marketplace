package ru.otus.otuskotlin.yieldInsights.repo.postgresql.ntv

import models.*
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositFilterRequest


private fun String.toDb() = this.takeIf { it.isNotBlank() }

internal fun DepositId.toDb() = mapOf(
    SqlFields.DEPOSIT_ID to asString().toDb(),
)
internal fun DepositLock.toDb() = mapOf(
    SqlFields.LOCK_OLD to asString().toDb(),
)

internal fun Deposit.toDb() = depositId.toDb() + mapOf(
    SqlFields.DEPOSIT_ID to depositId.toDb(),
    SqlFields.BANK_NAME to bankName.toDb(),
    SqlFields.DEPOSIT_NAME to depositName.toDb(),
    SqlFields.RATE to rate.toDb(),
    SqlFields.OPENING_DATE to openingDate.toDb(),
    SqlFields.DEPOSIT_TERM to depositTerm.toDb(),
    SqlFields.DEPOSIT_AMOUNT to depositAmount.toDb(),
    SqlFields.DEPOSIT_OPERATION to depositOperation.toDb(),
    SqlFields.IS_ACTIVE to isActive.toDb(),
    SqlFields.LOCK to lock.asString().toDb(),
)

internal fun DbDepositFilterRequest.toDb() = mapOf(
    SqlFields.RATE to rateFilter.toDb(),
)

private fun DepositOperation.toDb() = when (this) {
    DepositOperation.PROLONGATION -> SqlFields.DEPOSIT_OPERATION_PROLONGATION
    DepositOperation.CLOSE -> SqlFields.DEPOSIT_OPERATION_CLOSE
    else -> DepositOperation.NONE
}

private fun DepositActive.toDb() = when (this) {
    DepositActive.TRUE -> SqlFields.IS_ACTIVE_TRUE
    DepositActive.FALSE -> SqlFields.IS_ACTIVE_FALSE
    DepositActive.NONE -> null
}
