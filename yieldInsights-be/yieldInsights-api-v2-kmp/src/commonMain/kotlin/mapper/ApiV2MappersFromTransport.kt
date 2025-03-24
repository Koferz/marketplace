package mapper

import Context
import models.*
import ru.otus.yieldInsights.api.v2.models.*
import stubs.Stubs

fun Context.fromTransport(request: IRequest) = when (request) {
    is DepositCreateRequest -> fromTransport(request)
    is DepositReadRequest -> fromTransport(request)
    is DepositUpdateRequest -> fromTransport(request)
    is DepositDeleteRequest -> fromTransport(request)
    is DepositSearchRequest -> fromTransport(request)
    is DepositClosingRequest -> fromTransport(request)
}

private fun String?.toDepositId() = this?.let { DepositId(it) } ?: DepositId.NONE
private fun String?.toAdLock() = this?.let { DepositLock(it) } ?: DepositLock.NONE
private fun DepositReadObject?.toInternal() = if (this != null) {
    Deposit(depositId = id.toDepositId())
} else {
    Deposit()
}
//private fun String?.toProductId() = this?.let { ProductId(it) } ?: MkplProductId.NONE

private fun DepositDebug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    DepositRequestDebugMode.PROD -> WorkMode.PROD
    DepositRequestDebugMode.TEST -> WorkMode.TEST
    DepositRequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun DepositDebug?.transportToStubCase(): Stubs = when (this?.stub) {
    DepositRequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    DepositRequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    DepositRequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    DepositRequestDebugStubs.BAD_OPENING_DATE -> Stubs.BAD_OPENING_DATE
    DepositRequestDebugStubs.BAD_DEPOSIT_TERM -> Stubs.BAD_DEPOSIT_TERM
    DepositRequestDebugStubs.BAD_DEPOSIT_AMOUNT -> Stubs.BAD_DEPOSIT_AMOUNT
    DepositRequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    DepositRequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    null -> Stubs.NONE
}

fun Context.fromTransport(request: DepositCreateRequest) {
    command = Command.CREATE
    depositRequest = request.deposit?.toInternal() ?: Deposit()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: DepositReadRequest) {
    command = Command.READ
    depositRequest = request.deposit.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: DepositUpdateRequest) {
    command = Command.UPDATE
    depositRequest = request.deposit?.toInternal() ?: Deposit()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: DepositDeleteRequest) {
    command = Command.DELETE
    depositRequest = request.deposit.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun DepositDeleteObject?.toInternal(): Deposit = if (this != null) {
    Deposit(
        depositId = id.toDepositId(),
        lock = lock.toAdLock(),
    )
} else {
    Deposit()
}

fun Context.fromTransport(request: DepositSearchRequest) {
    command = Command.SEARCH
    depositFilterRequest = request.depositFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: DepositClosingRequest) {
    command = Command.CLOSING
    depositRequest = request.deposit.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun DepositSearchFilter?.toInternal(): DepositFilter = DepositFilter(
    searchString = this?.searchString ?: ""
)

private fun DepositCreateObject.toInternal(): Deposit = Deposit(
    depositName = this.depositName ?: "",
    rate = this.rate ?: "",
    openingDate = this.openingDate ?: "",
    depositTerm = this.depositTerm ?: "",
    depositAmount = this.depositAmount ?: ""
)

private fun DepositUpdateObject.toInternal(): Deposit = Deposit(
    depositTerm = this.depositTerm ?: "",
    depositAmount = this.depositAmount ?: "",
    lock = lock.toAdLock()
)