package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.beValidId
import ru.otus.yieldInsights.api.v1.models.*

suspend fun Client.readDeposit(id: String?, debug: DepositDebug = debugStubV1): DepositResponseObject = readDeposit(id, debug = debug) {
    it should haveSuccessResult
    it.deposit shouldNotBe null
    it.deposit!!
}

suspend fun <T> Client.readDeposit(id: String?, debug: DepositDebug = debugStubV1, block: (DepositReadResponse) -> T): T =
    withClue("readDepositV1: $id") {
        id should beValidId

        val response = sendAndReceive(
            "deposit/read",
            DepositReadRequest(
                requestType = "read",
                debug = debug,
                deposit = DepositReadObject(id = id)
            )
        ) as DepositReadResponse

        response.asClue(block)
    }
