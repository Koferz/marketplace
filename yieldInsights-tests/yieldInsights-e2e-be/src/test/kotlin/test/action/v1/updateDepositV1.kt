package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.yieldInsights.api.v1.models.DepositDebug
import ru.otus.yieldInsights.api.v1.models.DepositResponseObject
import ru.otus.yieldInsights.api.v1.models.DepositUpdateObject
import ru.otus.yieldInsights.api.v1.models.DepositUpdateResponse

suspend fun Client.updateDeposit(deposit: DepositUpdateObject, debug: DepositDebug = debugStubV1): DepositResponseObject =
    updateDeposit(deposit, debug = debug) {
        it should haveSuccessResult
        it.deposit shouldNotBe null
        it.deposit?.apply {
            if (deposit.depositName != null)
                depositName shouldBe deposit.depositName
            if (deposit.depositAmount != null)
                depositAmount shouldBe deposit.depositAmount
            if (deposit.rate != null)
                rate shouldBe deposit.rate
            if (deposit.depositTerm != null)
                depositTerm shouldBe deposit.depositTerm
        }
        it.deposit!!
    }

suspend fun <T> Client.updateDeposit(deposit: DepositUpdateObject, debug: DepositDebug = debugStubV1, block: (DepositUpdateResponse) -> T): T {
    val id = deposit.id
    val lock = deposit.lock
    return withClue("updatedV1: $id, lock: $lock, set: $deposit") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "deposit/update", DepositUpdateRequest(
                debug = debug,
                ad = deposit.copy(id = id, lock = lock)
            )
        ) as DepositUpdateResponse

        response.asClue(block)
    }
}
