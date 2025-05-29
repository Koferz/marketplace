import models.Deposit
import models.DepositLock


abstract class BaseInitDeposits(private val op: String): IInitObjects<Deposit> {
    open val lockOld: DepositLock = DepositLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: DepositLock = DepositLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        rate: String?,
        lock: DepositLock = lockOld,
    ) = Deposit(
        depositName = "deposit",
        rate = "$suf $rate",
        openingDate = "$suf stub description",
        depositTerm = "180",
        depositAmount = "100",
        lock = lock,
    )
}
