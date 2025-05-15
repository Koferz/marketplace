package ru.otus.otuskotlin.yieldInsights.common.repo

import models.Deposit
import models.DepositError

sealed interface IDbDepositResponse: IDbResponse<Deposit>

data class DbDepositResponseOk(
    val data: Deposit
): IDbDepositResponse

data class DbDepositResponseErr(
    val errors: List<DepositError> = emptyList()
): IDbDepositResponse {
    constructor(err: DepositError): this(listOf(err))
}

data class DbDepositResponseErrWithData(
    val data: Deposit,
    val errors: List<DepositError> = emptyList()
): IDbDepositResponse {
    constructor(deposit: Deposit, err: DepositError): this(deposit, listOf(err))
}
