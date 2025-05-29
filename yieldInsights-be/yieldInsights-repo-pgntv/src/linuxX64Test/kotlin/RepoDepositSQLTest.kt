package ru.otus.otuskotlin.yieldInsights.repo.postgresql

import DepositRepoInitialized
import IRepoDepositInitializable
import RepoDepositCreateTest
import RepoDepositDeleteTest
import RepoDepositReadTest
import RepoDepositSearchTest
import RepoDepositUpdateTest
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit
import ru.otus.otuskotlin.yieldInsights.repo.postgresql.ntv.RepoDepositSql
import kotlin.test.AfterTest

private fun IRepoDeposit.clear() {
    val pgRepo = (this as DepositRepoInitialized).repo as RepoDepositSql
    pgRepo.clear()
}

class RepoAdSQLCreateTest : RepoDepositCreateTest() {
    override val repo: IRepoDepositInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLReadTest : RepoDepositReadTest() {
    override val repo: IRepoDeposit = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLUpdateTest : RepoDepositUpdateTest() {
    override val repo: IRepoDeposit = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )

    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoAdSQLDeleteTest : RepoDepositDeleteTest() {
    override val repo: IRepoDeposit = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoAdSQLSearchTest : RepoDepositSearchTest() {
    override val repo: IRepoDeposit = SqlTestCompanion.repoUnderTestContainer(initObjects)

    @AfterTest
    fun tearDown() = repo.clear()
}
