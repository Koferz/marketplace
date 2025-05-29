package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import ru.otus.otuskotlin.yieldInsights.e2e.be.test.TestDebug
import ru.otus.yieldInsights.api.v1.models.DepositCreateObject
import ru.otus.yieldInsights.api.v1.models.DepositDebug
import ru.otus.yieldInsights.api.v1.models.DepositRequestDebugMode
import ru.otus.yieldInsights.api.v1.models.DepositRequestDebugStubs


val debugStubV1 = DepositDebug(mode = DepositRequestDebugMode.STUB, stub = DepositRequestDebugStubs.SUCCESS)

val someCreateDeposit = DepositCreateObject(
    bankName = "Bank1",
    depositName = "deposit1",
    rate = "18",
    depositOperation = "PROLONGATION",
    depositTerm = "6",
    depositAmount = "10000",
)

fun TestDebug.toV1() = when(this) {
    TestDebug.STUB -> debugStubV1
    TestDebug.PROD -> DepositDebug(mode = DepositRequestDebugMode.PROD)
    TestDebug.TEST -> DepositDebug(mode = DepositRequestDebugMode.TEST)
}
