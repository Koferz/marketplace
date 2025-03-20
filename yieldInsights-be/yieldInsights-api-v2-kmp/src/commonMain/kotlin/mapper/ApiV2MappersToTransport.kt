package ru.otus.otuskotlin.yieldInsights.api.v2.mappers

import Context
import models.*
import ru.otus.yieldInsights.api.v2.models.*
import exceptions.UnknownCommand

fun Context.toTransportAd(): IResponse = when (val cmd = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.CLOSING -> toTransportClosing()
    Command.NONE -> throw UnknownCommand(cmd)
}

fun Context.toTransportCreate() = DepositCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    deposit = depositResponse.toTransportDeposit()
)

fun Context.toTransportRead() = DepositReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    deposit = depositResponse.toTransportDeposit()
)

fun Context.toTransportUpdate() = DepositUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    deposit = depositResponse.toTransportDeposit()
)

fun Context.toTransportDelete() = DepositDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    deposit = depositResponse.toTransportDeposit()
)

fun Context.toTransportSearch() = DepositSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    deposits = depositsResponse.toTransportDeposit()
)

fun Context.toTransportClosing() = DepositClosingResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    deposits = depositsResponse.toTransportDeposit()
)

fun Context.toTransportInit() = DepositInitResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun List<Deposit>.toTransportDeposit(): List<DepositResponseObject>? = this
    .map { it.toTransportDeposit() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Deposit.toTransportDeposit(): DepositResponseObject = DepositResponseObject(
    id = depositId.takeIf { it != DepositId.NONE }?.asString(),
    bankName = bankName.toString(),
    depositName = depositName.takeIf { it.isNotBlank() },
    rate = rate.takeIf { it.isNotBlank() },
    openingDate = openingDate.takeIf { it.isNotBlank() },
    depositTerm = depositTerm.takeIf { it.isNotBlank() },
    depositAmount = depositAmount.takeIf { it.isNotBlank() },
    depositOperation = depositOperation.toString(),
    isActive = isActive.toString(),
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
)

private fun List<DepositError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportDeposit() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun DepositError.toTransportDeposit() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank()},
)

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING, State.FINISHING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.NONE -> null
}