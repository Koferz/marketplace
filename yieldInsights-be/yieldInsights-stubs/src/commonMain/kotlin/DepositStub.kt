import DepositStubTBank.T_BANK
import models.Deposit
import models.DepositId

object DepositStub {

    fun get(): Deposit = T_BANK.copy()

    fun prepareResult(block: Deposit.() -> Unit): Deposit = get().apply(block)

    fun prepareSearchList(filter: String, depositRate: String) = listOf(
        depositFind("d-01", filter, depositRate),
        depositFind("d-02", filter, depositRate),
        depositFind("d-03", filter, depositRate),
    )

    fun prepareClosingList(filter: String, ) = listOf(
        depositSupply("T-bank", filter)
    )

    private fun depositFind(id: String, filter: String, depositRate: String) =
        deposit(T_BANK, id = id, filter = filter)

    private fun depositSupply(id: String, filter: String) =
        deposit(T_BANK, id = id, filter = filter)

    private fun deposit(base: Deposit, id: String, filter: String) = base.copy(
        depositId = DepositId(id),
        depositName = "$filter $id",
        rate = "desc $filter"
    )
}