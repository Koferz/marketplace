package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import CorSettings
import DepositProcessor
import DepositRepositoryMock
import kotlinx.coroutines.test.runTest
import models.*
import repo.repoNotFoundTest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoClosingTest {

    private val userId = UserId("321")
    private val command = Command.CLOSING
    private val initDeposit = Deposit(
        depositId = DepositId("123"),
        depositName = "abc",
        rate = "18",
        ownerId = userId,
    )
    private val closingDeposit = Deposit(
        depositId = DepositId("321"),
        depositName = "abcd",
        rate = "18",
    )
    private val repo = DepositRepositoryMock(
        invokeReadDeposit = {
            DbDepositResponseOk(
                data = initDeposit
            )
        },
        invokeSearchDeposit = {
            DbDepositsResponseOk(
                data = listOf(closingDeposit)
            )
        }
    )
    private val settings = CorSettings(
        repoTest = repo
    )
    private val processor = DepositProcessor(settings)

    @Test
    fun repoOffersSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            depositRequest = Deposit(
                depositId = DepositId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(1, ctx.depositsResponse.size)
    }

    @Test
    fun repoOffersNotFoundTest() = repoNotFoundTest(command)
}
