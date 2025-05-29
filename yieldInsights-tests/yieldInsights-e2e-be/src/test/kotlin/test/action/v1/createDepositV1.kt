package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.yieldInsights.api.v1.models.*

suspend fun Client.createDeposit(deposit: DepositCreateObject = someCreateDeposit, debug: DepositDebug = debugStubV1): DepositResponseObject = createDeposit(deposit, debug = debug) {
    it should haveSuccessResult
    it.deposit shouldNotBe null
    it.deposit?.apply {
        depositName shouldBe deposit.depositName
        rate shouldBe deposit.rate
        depositAmount shouldBe deposit.depositAmount
        depositTerm  shouldBe deposit.depositTerm
    }
    it.deposit!!
}

suspend fun <T> Client.createDeposit(deposit: DepositCreateObject = someCreateDeposit, debug: DepositDebug = debugStubV1, block: (DepositCreateResponse) -> T): T =
    withClue("createDepositV1: $deposit") {
        val response = sendAndReceive(
            "deposit/create", DepositCreateRequest(
                requestType = "create",
                debug = debug,
                deposit = deposit
            )
        ) as DepositCreateResponse

        response.asClue(block)
    }
