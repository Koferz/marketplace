import models.*

object DepositStubTBank {

    val T_BANK: Deposit
        get() = Deposit(
            depositId = DepositId("1"),
            bankName= DepositOrgName(""),
            ownerId = UserId("1"),
            depositName = "deposit1",
            rate = "18",
            openingDate = "2025-02-01",
            depositTerm= "6",
            depositAmount = "100000",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.NONE,
            lock = DepositLock("123-234-abc-ABC")
        )
}