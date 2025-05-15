package ru.otus.otuskotlin.yieldInsights.repo.postgresql

import models.DepositOperation
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.operationEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.DEPOSIT_OPERATION_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.DEPOSIT_OPERATION_PROLONGATION -> DepositOperation.PROLONGATION
            SqlFields.DEPOSIT_OPERATION_CLOSE -> DepositOperation.CLOSE
            else -> DepositOperation.NONE
        }
    },
    toDb = { value ->
        when (value) {
            DepositOperation.PROLONGATION -> PgDepositTypeProlongation
            DepositOperation.CLOSE -> PgDepositTypeClose
            DepositOperation.NONE -> throw Exception("Wrong value of Operation Type. NONE is unsupported")
        }
    }
)

sealed class PgDepositTypeValue(enVal: String): PGobject() {
    init {
        type = SqlFields.DEPOSIT_OPERATION_TYPE
        value = enVal
    }
}

object PgDepositTypeProlongation: PgDepositTypeValue(SqlFields.DEPOSIT_OPERATION_PROLONGATION) {
    private fun readResolve(): Any = PgDepositTypeProlongation
}

object PgDepositTypeClose: PgDepositTypeValue(SqlFields.DEPOSIT_OPERATION_CLOSE) {
    private fun readResolve(): Any = PgDepositTypeClose
}
