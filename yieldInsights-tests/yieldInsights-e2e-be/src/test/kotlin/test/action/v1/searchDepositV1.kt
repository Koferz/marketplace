package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.yieldInsights.api.v1.models.*

suspend fun Client.searchDeposit(search: DepositSearchFilter, debug: DepositDebug = debugStubV1): List<DepositResponseObject> = searchDeposit(search, debug = debug) {
    it should haveSuccessResult
    it.deposits ?: listOf()
}

suspend fun <T> Client.searchDeposit(search: DepositSearchFilter, debug: DepositDebug = debugStubV1, block: (DepositSearchResponse) -> T): T =
    withClue("searchDepositV1: $search") {
        val response = sendAndReceive(
            "deposit/search",
            DepositSearchRequest(
                requestType = "search",
                debug = debug,
                depositFilter = search,
            )
        ) as DepositSearchResponse

        response.asClue(block)
    }
