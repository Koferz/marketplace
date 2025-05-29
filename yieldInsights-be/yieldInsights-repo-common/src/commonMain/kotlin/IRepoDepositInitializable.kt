import models.Deposit
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit

interface IRepoDepositInitializable : IRepoDeposit {
    fun save(deposit: Collection<Deposit>) : Collection<Deposit>
}