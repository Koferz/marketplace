package ru.otus.otuskotlin.yieldInsights.biz.validation

import Context
import kotlinx.coroutines.test.runTest
import models.*
import DepositProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = DepositStub.get()

fun validationDepositAmountCorrect(command: Command, processor: DepositProcessor) = runTest {
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
    assertEquals("abc", ctx.depositValidated.depositAmount)
}

fun validationDepositAmountTrim(command: Command, processor: DepositProcessor) = runTest {
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
            depositAmount = "\n\t 180 \n\t",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.TRUE,
            lock = DepositLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("abc", ctx.depositValidated.depositAmount)
}

fun validationDepositAmountEmpty(command: Command, processor: DepositProcessor) = runTest {
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
            depositAmount = "",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.TRUE,
            lock = DepositLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "depositAmount")
}

fun validationDepositAmountSymbols(command: Command, processor: DepositProcessor) = runTest {
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
            depositAmount = "!@#$%^&*(),.{}",
            depositOperation = DepositOperation.PROLONGATION,
            isActive = DepositActive.TRUE,
            lock = DepositLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("depositAmount", error?.field)
    assertContains(error?.message ?: "", "depositAmount")
}
