package helpers

import Context
import LogLevel
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

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = DepositError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)