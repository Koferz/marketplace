package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import kotlinx.coroutines.test.runTest
import models.Deposit
import models.DepositFilter
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateDepositNameHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = Context(state = State.RUNNING, depositValidating = Deposit(depositName = ""))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = Context(state = State.RUNNING, depositValidating = Deposit(depositName = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(State.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = Context(state = State.RUNNING, depositFilterValidating = DepositFilter(searchString = "Ж"))
        chain.exec(ctx)
        assertEquals(State.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateDepositNameHasContent("")
        }.build()
    }
}
