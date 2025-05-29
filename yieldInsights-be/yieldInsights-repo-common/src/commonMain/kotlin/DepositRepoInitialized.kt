import models.Deposit

class DepositRepoInitialized(
    val repo: IRepoDepositInitializable,
    initObjects: Collection<Deposit> = emptyList(),
) : IRepoDepositInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Deposit> = save(initObjects).toList()
}
