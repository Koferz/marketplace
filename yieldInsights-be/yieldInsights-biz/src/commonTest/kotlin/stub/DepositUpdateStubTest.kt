package ru.otus.otuskotlin.yieldInsights.biz.stub

import Context
import kotlinx.coroutines.test.runTest
import models.*
import DepositProcessor
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class DepositUpdateStubTest {

    private val processor = DepositProcessor()
    val bankName = DepositOrgName.NONE
    val depositName = "deposit1"
    val openingDate = "2025-02-01"
    val depositTerm = "6"
    val depositAmount = "18"
    val depositOperation = DepositOperation.PROLONGATION

    @Test
    fun create() = runTest {

        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            depositRequest = Deposit(
                bankName = bankName,
                depositName = depositName,
                openingDate = openingDate,
                depositTerm = depositTerm,
                depositAmount = depositAmount ,
                depositOperation = depositOperation
            ),
        )
        processor.exec(ctx)
        assertEquals(DepositStub.get().bankName, ctx.depositResponse.bankName)
        assertEquals(depositName, ctx.depositResponse.depositName)
        assertEquals(bankName, ctx.depositResponse.bankName)
        assertEquals(openingDate, ctx.depositResponse.openingDate)
        assertEquals(depositTerm, ctx.depositResponse.depositTerm)
        assertEquals(depositAmount, ctx.depositResponse.depositAmount)
        assertEquals(depositOperation, ctx.depositResponse.depositOperation)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_DEPOSIT_TERM,
            depositRequest = Deposit(
                bankName = bankName,
                depositName = depositName,
                openingDate = openingDate,
                depositAmount = depositAmount ,
                depositOperation = depositOperation
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("depositTerm", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badOpeningDate() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_OPENING_DATE,
            depositRequest = Deposit(
                bankName = bankName,
                depositName = depositName,
                openingDate = "",
                depositAmount = depositAmount ,
                depositOperation = depositOperation
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("opening_date", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            depositRequest = Deposit(
                bankName = bankName,
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            depositRequest = Deposit(
                bankName = bankName,
                depositName = depositName,
                openingDate = openingDate,
                depositAmount = depositAmount ,
                depositOperation = depositOperation
            ),
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
