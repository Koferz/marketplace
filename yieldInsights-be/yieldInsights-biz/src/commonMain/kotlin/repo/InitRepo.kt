package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import exceptions.DepositDbNotConfiguredException
import helpers.errorSystem
import helpers.fail
import models.WorkMode
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker

fun ICorChainDsl<Context>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        depositRepo = when {
            workMode == WorkMode.TEST -> corSettings.repoTest
            workMode == WorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != WorkMode.STUB && depositRepo == IRepoDeposit.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = DepositDbNotConfiguredException(workMode)
            )
        )
    }
}
