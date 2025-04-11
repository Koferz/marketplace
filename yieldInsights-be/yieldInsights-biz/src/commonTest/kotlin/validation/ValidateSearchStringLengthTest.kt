package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import kotlinx.coroutines.test.runTest
import models.DepositFilter
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = Context(state = State.RUNNING, depositFilterValidating = DepositFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = Context(state = State.RUNNING, depositFilterValidating = DepositFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = Context(state = State.RUNNING, depositFilterValidating = DepositFilter(searchString = "1"))
        chain.exec(ctx)
        assertEquals(State.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = Context(state = State.RUNNING, depositFilterValidating = DepositFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = Context(state = State.RUNNING, depositFilterValidating = DepositFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(State.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
