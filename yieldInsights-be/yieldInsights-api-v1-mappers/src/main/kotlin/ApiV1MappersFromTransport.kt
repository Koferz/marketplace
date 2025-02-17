import ru.otus.yieldInsights.api.v1.models.*
import ru.otus.yieldInsights.*
import exceptions.UnknownRequestClass
import models.*

fun Context.fromTransport(request: IRequest) = when (request) {
    is DepositCreateRequest -> fromTransport(request)
    is DepositReadRequest -> fromTransport(request)
    is DepositUpdateRequest -> fromTransport(request)
    is DepositDeleteRequest -> fromTransport(request)
    is DepositSearchRequest -> fromTransport(request)
    is DepositClosingRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toAdId() = this?.let { DepositId(it) } ?: DepositId.NONE
private fun String?.toAdWithId() = MkplAd(id = this.toAdId())
private fun String?.toAdLock() = this?.let { DepositLock(it) } ?: DepositLock.NONE

private fun AdDebug?.transportToWorkMode(): MkplWorkMode = when (this?.mode) {
    AdRequestDebugMode.PROD -> MkplWorkMode.PROD
    AdRequestDebugMode.TEST -> MkplWorkMode.TEST
    AdRequestDebugMode.STUB -> MkplWorkMode.STUB
    null -> MkplWorkMode.PROD
}

private fun AdDebug?.transportToStubCase(): MkplStubs = when (this?.stub) {
    AdRequestDebugStubs.SUCCESS -> MkplStubs.SUCCESS
    AdRequestDebugStubs.NOT_FOUND -> MkplStubs.NOT_FOUND
    AdRequestDebugStubs.BAD_ID -> MkplStubs.BAD_ID
    AdRequestDebugStubs.BAD_TITLE -> MkplStubs.BAD_TITLE
    AdRequestDebugStubs.BAD_DESCRIPTION -> MkplStubs.BAD_DESCRIPTION
    AdRequestDebugStubs.BAD_VISIBILITY -> MkplStubs.BAD_VISIBILITY
    AdRequestDebugStubs.CANNOT_DELETE -> MkplStubs.CANNOT_DELETE
    AdRequestDebugStubs.BAD_SEARCH_STRING -> MkplStubs.BAD_SEARCH_STRING
    null -> MkplStubs.NONE
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

private fun DepositReadObject?.toInternal(): Deposit = if (this != null) {
    Deposit(depositId = id.toAdId())
} else {
    Deposit()
}


fun Context.fromTransport(request: DepositUpdateRequest) {
    command = Command.UPDATE
    depositRequest = request.deposit?.toInternal() ?: Deposit()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdDeleteRequest) {
    command = MkplCommand.DELETE
    adRequest = request.ad.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun AdDeleteObject?.toInternal(): MkplAd = if (this != null) {
    MkplAd(
        id = id.toAdId(),
        lock = lock.toAdLock(),
    )
} else {
    MkplAd()
}

fun MkplContext.fromTransport(request: AdSearchRequest) {
    command = MkplCommand.SEARCH
    adFilterRequest = request.adFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdOffersRequest) {
    command = MkplCommand.OFFERS
    adRequest = request.ad?.id.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun AdSearchFilter?.toInternal(): MkplAdFilter = MkplAdFilter(
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
    lock = lock.toAdLock(),
)

private fun AdVisibility?.fromTransport(): MkplVisibility = when (this) {
    AdVisibility.PUBLIC -> MkplVisibility.VISIBLE_PUBLIC
    AdVisibility.OWNER_ONLY -> MkplVisibility.VISIBLE_TO_OWNER
    AdVisibility.REGISTERED_ONLY -> MkplVisibility.VISIBLE_TO_GROUP
    null -> MkplVisibility.NONE
}

private fun DealSide?.fromTransport(): MkplDealSide = when (this) {
    DealSide.DEMAND -> MkplDealSide.DEMAND
    DealSide.SUPPLY -> MkplDealSide.SUPPLY
    null -> MkplDealSide.NONE
}
