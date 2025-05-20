package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import CorSettings
import DepositProcessor
import DepositRepositoryMock
import kotlinx.coroutines.test.runTest
import models.*
import repo.repoNotFoundTest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErr
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = UserId("321")
    private val command = Command.DELETE
    private val initDeposit = Deposit(
        depositId = DepositId("123"),
        ownerId = userId,
        lock = DepositLock("123-234-abc-ABC"),
    )
    private val repo = DepositRepositoryMock(
        invokeReadDeposit = {
            DbDepositResponseOk(
                data = initDeposit,
            )
        },
        invokeDeleteDeposit = {
            if (it.id == initDeposit.depositId)
                DbDepositResponseOk(
                    data = initDeposit
                )
            else DbDepositResponseErr()
        }
    )
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor = DepositProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val depositToUpdate = Deposit(
            depositId = DepositId("123"),
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
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initDeposit.depositId, ctx.depositResponse.depositId)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
