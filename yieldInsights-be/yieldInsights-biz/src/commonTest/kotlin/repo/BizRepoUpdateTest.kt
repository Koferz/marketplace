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

class BizRepoUpdateTest {

    private val userId = UserId("321")
    private val command = Command.UPDATE
    private val initDeposit = Deposit(
        depositId = DepositId("123"),
        depositName = "abc",
        rate = "abc",
        ownerId = userId,
        lock = DepositLock("123-234-abc-ABC"),
    )
    private val repo = DepositRepositoryMock(
        invokeReadDeposit = {
            DbDepositResponseOk(
                data = initDeposit,
            )
        },
        invokeUpdateDeposit = {
            DbDepositResponseOk(
                data = Deposit(
                    depositId = DepositId("123"),
                    depositName = "xyz",
                    rate = "xyz",
                    lock = DepositLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = DepositProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val depositToUpdate = Deposit(
            depositId = DepositId("123"),
            depositName = "xyz",
            rate = "xyz",
            lock = DepositLock("123-234-abc-ABC"),
        )
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            depositRequest = depositToUpdate,
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(depositToUpdate.depositId, ctx.depositResponse.depositId)
        assertEquals(depositToUpdate.depositId, ctx.depositResponse.depositId)
        assertEquals(depositToUpdate.rate, ctx.depositResponse.rate)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
