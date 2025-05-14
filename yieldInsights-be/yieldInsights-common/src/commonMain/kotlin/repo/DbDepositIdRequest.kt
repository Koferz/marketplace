package ru.otus.otuskotlin.yieldInsights.common.repo

import models.Deposit
import models.DepositId
import models.DepositLock

data class DbDepositIdRequest(
    val id: DepositId,
    val lock: DepositLock = DepositLock.NONE,
) {
    constructor(deposit: Deposit): this(deposit.depositId, deposit.lock)
}
