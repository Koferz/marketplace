package helpers

import Context
import models.DepositError
import models.State

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

inline fun Context.addError(vararg error: DepositError) = errors.addAll(error)

inline fun Context.fail(error: DepositError) {
    addError(error)
    state = State.FAILING
}