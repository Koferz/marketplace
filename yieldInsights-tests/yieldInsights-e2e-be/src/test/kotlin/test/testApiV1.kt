package ru.otus.otuskotlin.yieldInsights.e2e.be.test

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1.*
import ru.otus.yieldInsights.api.v1.models.DepositDebug
import ru.otus.yieldInsights.api.v1.models.DepositUpdateObject

fun FunSpec.testApiV1(client: Client, prefix: String = "", debug: DepositDebug = debugStubV1) {
    context("${prefix}v1") {
        test("Create Deposit ok") {
            client.createDeposit(debug = debug)
        }

        test("Read Deposit ok") {
            val created = client.createDeposit(debug = debug)
            client.readDeposit(created.id, debug = debug).asClue {
                it shouldBe created
            }
        }

        test("Update Deposit ok") {
            val created = client.createDeposit(debug = debug)
            val updateAd = DepositUpdateObject(
                bankName = created.id,
                lock = created.lock,
                depositName = "deposit1",
                rate = created.rate,
                openingDate = created.openingDate,
                depositTerm = created.depositTerm,
                depositAmount = created.depositAmount,
                depositOperation = created.PROLONGATION,
            )
            client.updateDeposit(updateAd, debug = debug)
        }

        test("Delete Deposit ok") {
            val created = client.createDeposit(debug = debug)
            client.deleteDeposit(created, debug = debug)
        }

        test("Search Deposit ok") {
            val created1 = client.createDeposit(someCreateDeposit.copy(depositName = "Deposit1"), debug = debug)

            withClue("Search Rate") {
                val results = client.searchDeposit(search = DepositSearchFilter(searchString = "18"), debug = debug)
                results shouldExist { it.rate == created1.rate }
            }

            withClue("Search Deposit") {
                client.searchDeposit(search = DepositSearchFilter(searchString = "18"), debug = debug)
                    .shouldExistInOrder({ it.rate == created1.rate })
            }
        }
    }

}
