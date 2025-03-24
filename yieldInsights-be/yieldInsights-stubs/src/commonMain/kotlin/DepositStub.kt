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
//
//    fun prepareOffersList(filter: String, type: MkplDealSide) = listOf(
//        mkplAdSupply("s-666-01", filter, type),
//        mkplAdSupply("s-666-02", filter, type),
//        mkplAdSupply("s-666-03", filter, type),
//        mkplAdSupply("s-666-04", filter, type),
//        mkplAdSupply("s-666-05", filter, type),
//        mkplAdSupply("s-666-06", filter, type),
//    )

    private fun depositFind(id: String, filter: String, depositRate: String) =
        deposit(T_BANK, id = id, filter = filter, depositRate)

//    private fun mkplAdSupply(id: String, filter: String) =
//        mkplAd(AD_SUPPLY_BOLT1, id = id, filter = filter)

    private fun deposit(base: Deposit, id: String, filter: String, depositRate: String) = base.copy(
        depositId = DepositId(id),
        depositName = "$filter $id",
        rate = "desc $filter $depositRate"
    )
}