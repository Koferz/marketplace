package ru.otus.otuskotlin.yieldInsights.repo.postgresql

import models.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class DepositTable(tableName: String) : Table(tableName) {
    val depositId = text(SqlFields.DEPOSIT_ID)
    val bankName = text(SqlFields.BANK_NAME)
    val depositName = text(SqlFields.DEPOSIT_NAME).nullable()
    val rate = text(SqlFields.RATE).nullable()
    val openingDate = text(SqlFields.OPENING_DATE)
    val depositTerm = text(SqlFields.DEPOSIT_TERM)
    val depositAmount = text(SqlFields.DEPOSIT_AMOUNT)
    val depositOperation = operationEnumeration(SqlFields.DEPOSIT_OPERATION)
    val owner = text(SqlFields.OWNER_ID)
    val isActive = activeEnumeration(SqlFields.IS_ACTIVE)
    val lock = text(SqlFields.LOCK)
    //val productId = text(SqlFields.PRODUCT_ID).nullable()

    override val primaryKey = PrimaryKey(depositId)

    fun from(res: ResultRow) = Deposit(
        depositId = DepositId(res[depositId].toString()),
        bankName = res[bankName],
        depositName = res[depositName] ?: "",
        ownerId = UserId(res[owner].toString()),
        rate = res[rate].toString(),
        openingDate = res[openingDate],
        depositTerm = res[depositTerm],
        depositAmount = res[depositAmount],
        depositOperation = res[depositOperation],
        isActive = res[isActive],
        lock = DepositLock(res[lock])
    )

    fun to(it: UpdateBuilder<*>, deposit: Deposit, randomUuid: () -> String) {
        it[depositId] = deposit.depositId.takeIf { it != DepositId.NONE }?.asString() ?: randomUuid()
        it[bankName] = deposit.bankName
        it[depositName] = deposit.depositName
        it[owner] = deposit.ownerId.asString()
        it[rate] = deposit.rate
        it[openingDate] = deposit.openingDate
        it[depositTerm] = deposit.depositTerm
        it[depositAmount] = deposit.depositAmount
        it[depositOperation] = deposit.depositOperation
        it[isActive] = deposit.isActive
        it[lock] = deposit.lock.takeIf { it != DepositLock.NONE }?.asString() ?: randomUuid()
    }

}

