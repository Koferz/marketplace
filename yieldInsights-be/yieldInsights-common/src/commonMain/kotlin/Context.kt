import kotlinx.datetime.Instant
import stubs.Stubs
import models.WorkMode
import models.*
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<DepositError> = mutableListOf(),

    var corSettings: CorSettings = CorSettings(),
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var depositRequest: Deposit = Deposit(),
    var depositFilterRequest: DepositFilter = DepositFilter(),

    var depositValidating: Deposit = Deposit(),
    var depositFilterValidating: DepositFilter = DepositFilter(),

    var depositValidated: Deposit = Deposit(),
    var depositFilterValidated: DepositFilter = DepositFilter(),

    var depositRepo: IRepoDeposit = IRepoDeposit.NONE,
    var depositRepoRead: Deposit = Deposit(), // То, что прочитали из репозитория
    var depositRepoPrepare: Deposit = Deposit(), // То, что готовим для сохранения в БД
    var depositRepoDone: Deposit = Deposit(),  // Результат, полученный из БД
    var depositsRepoDone: MutableList<Deposit> = mutableListOf(),



    var depositResponse: Deposit = Deposit(),
    var depositsResponse: MutableList<Deposit> = mutableListOf(),

    )