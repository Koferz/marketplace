package ru.otus.otuskotlin.yieldInsights.common.repo.exceptions

import models.DepositId


class RepoEmptyLockException(id: DepositId): RepoDepositException(
    id,
    "Lock is empty in DB"
)
