package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import kotlinx.coroutines.test.runTest
import models.*
import DepositProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = DepositStub.get()

fun validationDepositNameCorrect(command: Command, processor: DepositProcessor) = runTest {
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
    assertEquals("abc", ctx.depositValidated.depositName)
}

fun validationDepositNameTrim(command: Command, processor: DepositProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = stub.depositId,
            depositName = " \n\t abc \t\n ",
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
    assertEquals("abc", ctx.depositValidated.depositName)
}

fun validationDepositNameEmpty(command: Command, processor: DepositProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = stub.depositId,
            depositName = "",
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
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("depositName", error?.field)
    assertContains(error?.message ?: "", "depositName")
}

fun validationDepositNameSymbols(command: Command, processor: DepositProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = stub.depositId,
            depositName = "!@#\$%^&*(),.{}",
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
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("depositName", error?.field)
    assertContains(error?.message ?: "", "depositName")
}
