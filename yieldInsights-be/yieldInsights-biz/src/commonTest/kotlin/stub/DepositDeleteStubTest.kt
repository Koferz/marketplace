package ru.otus.otuskotlin.yieldInsights.biz.stub

import Context
import kotlinx.coroutines.test.runTest
import models.*
import ru.otus.yieldInsights.biz.DepositProcessor
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class DepositDeleteStubTest {

    private val processor = DepositProcessor()
    val id = DepositId("666")

    @Test
    fun delete() = runTest {

        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            depositRequest = Deposit(
                depositId = id,
            ),
        )
        processor.exec(ctx)

        val stub = DepositStub.get()
        assertEquals(stub.depositId, ctx.depositResponse.depositId)
        assertEquals(stub.bankName, ctx.depositResponse.bankName)
        assertEquals(stub.depositName, ctx.depositResponse.depositName)
        assertEquals(stub.rate, ctx.depositResponse.rate)
        assertEquals(stub.openingDate, ctx.depositResponse.openingDate)
        assertEquals(stub.depositTerm, ctx.depositResponse.depositTerm)
        assertEquals(stub.depositAmount, ctx.depositResponse.depositAmount)
        assertEquals(stub.depositOperation, ctx.depositResponse.depositOperation)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            depositRequest = Deposit(),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            depositRequest = Deposit(
                depositId = id
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.DELETE,
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
