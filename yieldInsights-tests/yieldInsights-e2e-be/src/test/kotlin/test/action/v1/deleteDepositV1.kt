package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.beValidId
import ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.beValidLock
import ru.otus.yieldInsights.api.v1.models.*

suspend fun Client.deleteDeposit(deposit: DepositResponseObject, debug: DepositDebug = debugStubV1) {
    val id = deposit.id
    withClue("deleteDepositV1: $id) {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "deposit/delete",
            DepositDeleteRequest(
                debug = debug,
                deposit = DepositDeleteObject(id = id, lock = lock)
            )
        ) as DepositDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.deposit shouldBe deposit
        }
    }
}
