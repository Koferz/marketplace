package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import CorSettings
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import DepositRepositoryMock

class BizRepoSearchTest {

    private val userId = UserId("321")
    private val command = Command.SEARCH
    private val initAd = Deposit(
        depositId = DepositId("123"),
        rate = "18",
        depositName = "abc",
        ownerId = userId
    )
    private val repo = DepositRepositoryMock(
        invokeSearchDeposit = {
            DbDepositsResponseOk(
                data = listOf(initAd),
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = DepositProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            depositFilterRequest = DepositFilter(
                searchString = "abc"
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(1, ctx.depositsResponse.size)
    }
}
