package ru.otus.otuskotlin.yieldInsights.common.repo

import helpers.errorSystem
import models.Deposit
import models.DepositError
import models.DepositId
import models.DepositLock
import ru.otus.otuskotlin.yieldInsights.common.repo.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.yieldInsights.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: DepositId) = DbAdResponseErr(
    DepositError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbAdResponseErr(
    DepositError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldAd: Deposit,
    expectedLock: DepositLock,
    exception: Exception = RepoConcurrencyException(
        id = oldAd.depositId,
        expectedLock = expectedLock,
        actualLock = oldAd.lock,
    ),
) = DbAdResponseErrWithData(
    ad = oldAd,
    err = DepositError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID $oldAd has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: DepositId) = DbAdResponseErr(
    DepositError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbAdResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
