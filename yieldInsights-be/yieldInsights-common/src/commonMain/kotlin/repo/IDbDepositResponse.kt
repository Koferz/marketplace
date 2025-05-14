package ru.otus.otuskotlin.yieldInsights.common.repo

import models.Deposit
import models.DepositError

sealed interface IDbDepositResponse: IDbResponse<Deposit>

data class DbDepositResponseOk(
    val data: Deposit
): IDbDepositResponse

data class DbAdResponseErr(
    val errors: List<DepositError> = emptyList()
): IDbDepositResponse {
    constructor(err: DepositError): this(listOf(err))
}

data class DbAdResponseErrWithData(
    val data: Deposit,
    val errors: List<DepositError> = emptyList()
): IDbDepositResponse {
    constructor(ad: Deposit, err: DepositError): this(ad, listOf(err))
}
