package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.yieldInsights.api.v1.models.DepositClosingRequest
import ru.otus.yieldInsights.api.v1.models.DepositClosingResponse
import ru.otus.yieldInsights.api.v1.models.DepositDebug
import ru.otus.yieldInsights.api.v1.models.DepositReadObject

suspend fun Client.closingDeposit(id: String?, debug: DepositDebug = debugStubV1): DepositClosingResponse = closingDeposit(id, debug = debug) {
    it should haveSuccessResult
    it
}

suspend fun <T> Client.closingDeposit(id: String?, debug: DepositDebug = debugStubV1, block: (DepositClosingResponse) -> T): T =
    withClue("searchClosingV1: $id") {
        val response = sendAndReceive(
            "deposit/closing",
            DepositClosingRequest(
                debug = debug,
                deposit = DepositReadObject(id = id),
            )
        ) as DepositClosingResponse

        response.asClue(block)
    }
