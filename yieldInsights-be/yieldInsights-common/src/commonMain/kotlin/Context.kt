import kotlinx.datetime.Instant
import stubs.Stubs
import models.WorkMode
import models.*

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<Error> = mutableListOf(),

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var depositRequest: Deposit = Deposit(),
    var adFilterRequest: DepositFilter = DepositFilter(),

    var depositResponse: Deposit = Deposit(),
    var depositsResponse: MutableList<Deposit> = mutableListOf(),

    )