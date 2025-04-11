package ru.otus.otuskotlin.yieldInsights.biz.stub

import Context
import kotlinx.coroutines.test.runTest
import models.*
import DepositProcessor
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class DepositClosingStubTest {

    private val processor = DepositProcessor()
    val id = DepositId("1")

    @Test
    fun closing() = runTest {

        val ctx = Context(
            command = Command.CLOSING,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            depositRequest = Deposit(
                depositId = id,
            ),
        )
        processor.exec(ctx)

        assertEquals(id, ctx.depositResponse.depositId)

        with(DepositStub.get()) {
            assertEquals(depositId, ctx.depositResponse.depositId)
            assertEquals(bankName, ctx.depositResponse.bankName)
            assertEquals(depositName, ctx.depositResponse.depositName)
            assertEquals(rate, ctx.depositResponse.rate)
            assertEquals(openingDate, ctx.depositResponse.openingDate)
            assertEquals(depositTerm, ctx.depositResponse.depositTerm)
            assertEquals(depositAmount, ctx.depositResponse.depositAmount)
            assertEquals(depositOperation, ctx.depositResponse.depositOperation)
        }

        println(ctx.depositsResponse.size)
        assertTrue(ctx.depositsResponse.size >= 1)
        val first = ctx.depositsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.rate.contains(ctx.depositResponse.rate))
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.CLOSING,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            depositRequest = Deposit(
                depositId = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.CLOSING,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            depositRequest = Deposit(
                depositId = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.CLOSING,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_DEPOSIT_TERM,
            depositRequest = Deposit(
                depositId = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
