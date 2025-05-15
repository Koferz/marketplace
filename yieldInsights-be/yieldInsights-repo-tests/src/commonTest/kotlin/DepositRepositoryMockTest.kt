package ru.otus.otuskotlin.marketplace.backend.repo.tests

import DepositRepositoryMock
import kotlinx.coroutines.test.runTest
import models.Deposit
import ru.otus.otuskotlin.yieldInsights.common.repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DepositRepositoryMockTest {
    private val repo = DepositRepositoryMock(
        invokeCreateDeposit = { DbDepositResponseOk(DepositStub.prepareResult { rate = "create" }) },
        invokeReadDeposit = { DbDepositResponseOk(DepositStub.prepareResult { rate = "read" }) },
        invokeUpdateDeposit = { DbDepositResponseOk(DepositStub.prepareResult { rate = "update" }) },
        invokeDeleteDeposit = { DbDepositResponseOk(DepositStub.prepareResult { rate = "delete" }) },
        invokeSearchDeposit = { DbDepositsResponseOk(listOf(DepositStub.prepareResult { rate = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createDeposit(DbDepositRequest(Deposit()))
        assertIs<DbDepositResponseOk>(result)
        assertEquals("create", result.data.rate)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readDeposit(DbDepositIdRequest(Deposit()))
        assertIs<DbDepositResponseOk>(result)
        assertEquals("read", result.data.rate)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateDeposit(DbDepositRequest(Deposit()))
        assertIs<DbDepositResponseOk>(result)
        assertEquals("update", result.data.rate)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteDeposit(DbDepositIdRequest(Deposit()))
        assertIs<DbDepositResponseOk>(result)
        assertEquals("delete", result.data.rate)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchDeposit(DbDepositFilterRequest())
        assertIs<DbDepositsResponseOk>(result)
        assertEquals("search", result.data.first().rate)
    }

}
