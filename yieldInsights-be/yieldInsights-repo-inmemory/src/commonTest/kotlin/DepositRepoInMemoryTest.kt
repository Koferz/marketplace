
import ru.otus.otuskotlin.yieldInsights.repo.inmemory.DepositRepoInMemory

class DepositRepoInMemoryCreateTest : RepoDepositCreateTest() {
    override val repo = DepositRepoInitialized(
        DepositRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class DepositRepoInMemoryDeleteTest : RepoDepositDeleteTest() {
    override val repo = DepositRepoInitialized(
        DepositRepoInMemory(),
        initObjects = initObjects,
    )
}

class DepositRepoInMemoryReadTest : RepoDepositReadTest() {
    override val repo = DepositRepoInitialized(
        DepositRepoInMemory(),
        initObjects = initObjects,
    )
}

class DepositRepoInMemorySearchTest : RepoDepositSearchTest() {
    override val repo = DepositRepoInitialized(
        DepositRepoInMemory(),
        initObjects = initObjects,
    )
}

class DepositRepoInMemoryUpdateTest : RepoDepositUpdateTest() {
    override val repo = DepositRepoInitialized(
        DepositRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
