package ru.otus.otuskotlin.yieldInsights.repo.inmemory

import models.Deposit
import models.DepositLock

data class DepositEntity(
    val depositId: String? = null,
    val depositName: String? = null,
    val rate: String? = null,
    val openingDate: String? = null,
    val depositTerm: String? = null,
    val depositAmount: String? = null,
    val depositOperation: String? = null,
    val isActive: String? = null,
    val lock: String? = null,
) {
    constructor(model: Deposit): this(
        depositId = depositId.takeIf { it.isNotBlank() },
        depositName = depositName.takeIf { it.isNotBlank() },
        rate = rate.takeIf { it.isNotBlank() },
        openingDate = openingDate.takeIf { it.isNotBlank() },
        depositTerm = depositTerm.takeIf { it.isNotBlank() },
        depositAmount = depositAmount.takeIf { it.isNotBlank() },
        depositOperation = depositOperation.toString(),
        isActive = isActive.toString(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
        // Не нужно сохранять permissions, потому что он ВЫЧИСЛЯЕМЫЙ, а не хранимый
    )

    fun toInternal() = Deposit(
        depositId = depositId.takeIf { it.isNotBlank() },
        depositName = depositName.takeIf { it.isNotBlank() },
        rate = rate.takeIf { it.isNotBlank() },
        openingDate = openingDate.takeIf { it.isNotBlank() },
        depositTerm = depositTerm.takeIf { it.isNotBlank() },
        depositAmount = depositAmount.takeIf { it.isNotBlank() },
        depositOperation = depositOperation.toString(),
        isActive = isActive.toString(),
        lock = lock?.let { DepositLock(it) } ?: DepositLock.NONE,
    )
}
