package helpers

import models.Deposit
import models.DepositError

fun Throwable.asDepositError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = DepositError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)