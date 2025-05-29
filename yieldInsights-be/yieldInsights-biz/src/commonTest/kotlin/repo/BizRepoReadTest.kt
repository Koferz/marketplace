package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import CorSettings
import DepositProcessor
import DepositRepositoryMock
import kotlinx.coroutines.test.runTest
import models.*
import repo.repoNotFoundTest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = UserId("321")
    private val command = Command.READ
    private val initDeposit = Deposit(
        depositId = DepositId("123"),
        depositName = "abc",
        rate = "18",
        depositTerm = "180",
        ownerId = userId
    )
    private val repo = DepositRepositoryMock(
        invokeReadDeposit = {
            DbDepositResponseOk(
                data = initDeposit,
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = DepositProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
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
        assertEquals(initDeposit.depositId, ctx.depositResponse.depositId)
        assertEquals(initDeposit.depositName, ctx.depositResponse.depositName)
        assertEquals(initDeposit.rate, ctx.depositResponse.rate)
        assertEquals(initDeposit.depositTerm, ctx.depositResponse.depositTerm)
        assertEquals(initDeposit.ownerId, ctx.depositResponse.ownerId)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
