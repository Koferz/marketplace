package ru.otus.otuskotlin.yieldInsights.common.repo.exceptions

import models.DepositId

open class RepoDepositException(
    @Suppress("unused")
    val depositId: DepositId,
    msg: String,
): RepoException(msg)
