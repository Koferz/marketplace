package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import kotlinx.coroutines.test.runTest
import models.*
import DepositProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = DepositStub.get()

fun validationLockCorrect(command: Command, processor: DepositProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = stub.depositId,
            depositName = "abc",
            rate = "18",
            openingDate = "01-01-2025",
            depositTerm = "6",
            depositAmount = "180",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.TRUE,
            lock = DepositLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

fun validationLockTrim(command: Command, processor: DepositProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = stub.depositId,
            depositName = "abc",
            rate = "18",
            openingDate = "01-01-2025",
            depositTerm = "6",
            depositAmount = "180",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.TRUE,
            lock = DepositLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

fun validationLockEmpty(command: Command, processor: DepositProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = stub.depositId,
            depositName = "abc",
            rate = "18",
            openingDate = "01-01-2025",
            depositTerm = "6",
            depositAmount = "180",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.TRUE,
            lock = DepositLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: Command, processor: DepositProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = stub.depositId,
            depositName = "abc",
            rate = "18",
            openingDate = "01-01-2025",
            depositTerm = "6",
            depositAmount = "180",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.TRUE,
            lock = DepositLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
