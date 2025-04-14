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

class DepositSearchStubTest {

    private val processor = DepositProcessor()
    val filter = DepositFilter(searchString = "18")

    @Test
    fun read() = runTest {

        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            depositFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.depositsResponse.size > 1)
        val first = ctx.depositsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.rate.contains(filter.searchString))
        with(DepositStub.get()) {
            assertEquals(bankName, ctx.depositResponse.bankName)

        }
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            depositFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            depositFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            depositFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Deposit(), ctx.depositResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
    }
}
