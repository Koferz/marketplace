package ru.otus.otuskotlin.yieldInsights.repo.postgresql.ntv

object SqlFields {
    const val DEPOSIT_ID = "id"
    const val BANK_NAME = "bankName"
    const val DEPOSIT_NAME = "depositName"
    const val RATE = "rate"
    const val OPENING_DATE = "openingDate"
    const val DEPOSIT_TERM = "depositTerm"
    const val DEPOSIT_AMOUNT = "depositAmount"
    const val DEPOSIT_OPERATION = "depositOperation"
    const val IS_ACTIVE = "isActive"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"
    const val OWNER_ID = "owner_id"


    const val DEPOSIT_OPERATION_TYPE = "deposit_operation_type"
    const val DEPOSIT_OPERATION_PROLONGATION = "prolongation"
    const val DEPOSIT_OPERATION_CLOSE = "close"

    const val IS_ACTIVE_TYPE = "deposit_operation_type"
    const val IS_ACTIVE_TRUE = "true"
    const val IS_ACTIVE_FALSE = "false"

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        DEPOSIT_ID, BANK_NAME, DEPOSIT_NAME, RATE, OPENING_DATE, DEPOSIT_TERM, OWNER_ID, DEPOSIT_AMOUNT,
        DEPOSIT_OPERATION, IS_ACTIVE, LOCK, OWNER_ID
    )
}
