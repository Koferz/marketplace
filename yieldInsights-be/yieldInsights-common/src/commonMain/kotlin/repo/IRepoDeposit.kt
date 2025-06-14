package ru.otus.otuskotlin.marketplace.common.repo

import ru.otus.otuskotlin.yieldInsights.common.repo.*

interface IRepoDeposit {
    suspend fun createDeposit(rq: DbDepositRequest): IDbDepositResponse
    suspend fun readDeposit(rq: DbDepositIdRequest): IDbDepositResponse
    suspend fun updateDeposit(rq: DbDepositRequest): IDbDepositResponse
    suspend fun deleteDeposit(rq: DbDepositIdRequest): IDbDepositResponse
    suspend fun searchDeposit(rq: DbDepositFilterRequest): IDbDepositsResponse
    companion object {
        val NONE = object : IRepoDeposit {
            override suspend fun createDeposit(rq: DbDepositRequest): IDbDepositResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateDeposit(rq: DbDepositRequest): IDbDepositResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteDeposit(rq: DbDepositIdRequest): IDbDepositResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchDeposit(rq: DbDepositFilterRequest): IDbDepositsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
