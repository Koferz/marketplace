package ru.otus.otuskotlin.yieldInsights.common.repo

import models.Deposit
import models.DepositError

sealed interface IDbDepositsResponse: IDbResponse<List<Deposit>>

data class DbDepositsResponseOk(
    val data: List<Deposit>
): IDbDepositsResponse

@Suppress("unused")
data class DbAdsResponseErr(
    val errors: List<DepositError> = emptyList()
): IDbDepositsResponse {
    constructor(err: DepositError): this(listOf(err))
}
