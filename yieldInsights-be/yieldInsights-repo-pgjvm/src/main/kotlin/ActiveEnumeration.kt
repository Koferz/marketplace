package ru.otus.otuskotlin.yieldInsights.repo.postgresql

import models.DepositActive
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.activeEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.IS_ACTIVE_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.IS_ACTIVE_TRUE -> DepositActive.TRUE
            SqlFields.IS_ACTIVE_FALSE -> DepositActive.FALSE
            else -> DepositActive.NONE
        }
    },
    toDb = { value ->
        when (value) {
            DepositActive.TRUE -> PgDepositActiveTrue
            DepositActive.FALSE -> PgDepositActiveFalse
            DepositActive.NONE -> throw Exception("Wrong value of Active type. NONE is unsupported")
        }
    }
)

sealed class PgDepositActiveValue(enVal: String): PGobject() {
    init {
        type = SqlFields.IS_ACTIVE_TYPE
        value = enVal
    }
}

object PgDepositActiveTrue: PgDepositActiveValue(SqlFields.IS_ACTIVE_TRUE) {
    private fun readResolve(): Any = PgDepositActiveTrue
}

object PgDepositActiveFalse: PgDepositActiveValue(SqlFields.IS_ACTIVE_FALSE) {
    private fun readResolve(): Any = PgDepositActiveFalse
}
