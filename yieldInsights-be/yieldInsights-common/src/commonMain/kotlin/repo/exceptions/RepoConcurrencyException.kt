package ru.otus.otuskotlin.yieldInsights.common.repo.exceptions

import models.DepositId
import models.DepositLock

class RepoConcurrencyException(id: DepositId, expectedLock: DepositLock, actualLock: DepositLock?): RepoDepositException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
