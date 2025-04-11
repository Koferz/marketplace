package models

data class Deposit(
    var depositId: DepositId = DepositId.NONE,
    var bankName: DepositOrgName = DepositOrgName.NONE,
    var ownerId: UserId = UserId.NONE,
    var depositName: String = "",
    var rate: String = "",
    var openingDate: String = "",
    var depositTerm: String = "",
    var depositAmount: String = "",
    var depositOperation: DepositOperation = DepositOperation.PROLONGATION,
    var isActive: DepositActive = DepositActive.NONE,
    var lock: DepositLock = DepositLock.NONE
) {
    fun deepCopy(): Deposit = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = Deposit()
    }

}
