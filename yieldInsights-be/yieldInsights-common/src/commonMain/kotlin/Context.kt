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

    var adRepo: IRepoDeposit = IRepoDeposit.NONE,
    var adRepoRead: Deposit = Deposit(), // То, что прочитали из репозитория
    var adRepoPrepare: Deposit = Deposit(), // То, что готовим для сохранения в БД
    var adRepoDone: Deposit = Deposit(),  // Результат, полученный из БД
    var adsRepoDone: MutableList<Deposit> = mutableListOf(),



    var depositResponse: Deposit = Deposit(),
    var depositsResponse: MutableList<Deposit> = mutableListOf(),

    )