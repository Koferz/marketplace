package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import CorSettings
import DepositProcessor
import DepositRepositoryMock
import kotlinx.coroutines.test.runTest
import models.*
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = UserId("321")
    private val command = Command.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = DepositRepositoryMock(
        invokeCreateDeposit = {
            DbDepositResponseOk(
                data = Deposit(
                    depositId = DepositId(uuid),
                    depositName = it.deposit.depositName,
                    rate = it.deposit.rate,
                    openingDate = it.deposit.openingDate,
                    depositTerm = it.deposit.depositTerm,
                    depositAmount = it.deposit.depositAmount,
                    depositOperation = it.deposit.depositOperation,
                    ownerId = userId,
                )
            )
        }
    )
    private val settings = CorSettings(
        repoTest = repo
    )
    private val processor = DepositProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            depositRequest = Deposit(
                depositName = "abc",
                rate = "18"
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertNotEquals(DepositId.NONE, ctx.depositResponse.depositId)
        assertEquals("abc", ctx.depositResponse.depositName)
        assertEquals("abc", ctx.depositResponse.rate)
    }
}
