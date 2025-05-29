import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit

data class CorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val repoStub: IRepoDeposit = IRepoDeposit.NONE,
    val repoTest: IRepoDeposit = IRepoDeposit.NONE,
    val repoProd: IRepoDeposit = IRepoDeposit.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}