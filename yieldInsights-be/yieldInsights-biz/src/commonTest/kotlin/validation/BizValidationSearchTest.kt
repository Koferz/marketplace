package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import kotlinx.coroutines.test.runTest
import models.Command
import models.DepositFilter
import models.State
import models.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = Command.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            depositFilterRequest = DepositFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(State.FAILING, ctx.state)
    }
}
