package ru.otus.otuskotlin.yieldInsights.repo.postgresql.ntv

import io.github.moreirasantos.pgkn.resultset.ResultSet
import models.*


internal fun ResultSet.fromDb(cols: List<String>): Deposit {
    val fieldsMap = cols.mapIndexed { i: Int, field: String -> field to i }.toMap()
    fun col(field: String): String? = fieldsMap[field]?.let { getString(it) }
    return Deposit(
        depositId = col(SqlFields.DEPOSIT_ID)?.let { DepositId(it) } ?: DepositId.NONE,
        bankName = col(SqlFields.BANK_NAME) ?: "",
        depositName = col(SqlFields.DEPOSIT_NAME) ?: "",
        rate = col(SqlFields.RATE) ?: "",
        openingDate = col(SqlFields.OPENING_DATE) ?: "",
        depositTerm = col(SqlFields.DEPOSIT_TERM) ?: "",
        depositAmount = col(SqlFields.DEPOSIT_AMOUNT) ?: "",
        ownerId = col(SqlFields.OWNER_ID)?.let { UserId(it) } ?: UserId.NONE,
        depositOperation = col(SqlFields.DEPOSIT_OPERATION_TYPE).asDepositOperation(),
        isActive = col(SqlFields.IS_ACTIVE_TYPE).asIsActive(),
        lock = col(SqlFields.LOCK)?.let { DepositLock(it) } ?: DepositLock.NONE,
    )
}

private fun String?.asDepositOperation(): DepositOperation = when (this) {
    SqlFields.DEPOSIT_OPERATION_PROLONGATION -> DepositOperation.PROLONGATION
    SqlFields.DEPOSIT_OPERATION_CLOSE -> DepositOperation.CLOSE
    else -> DepositOperation.NONE
}

private fun String?.asIsActive(): DepositActive = when (this) {
    SqlFields.IS_ACTIVE_TRUE -> DepositActive.TRUE
    SqlFields.IS_ACTIVE_FALSE -> DepositActive.FALSE
    else -> DepositActive.NONE
}
