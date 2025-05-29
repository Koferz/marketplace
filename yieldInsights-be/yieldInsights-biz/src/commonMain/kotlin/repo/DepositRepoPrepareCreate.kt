package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == State.RUNNING }
    handle {
        depositRepoPrepare = depositValidated.deepCopy()
        depositRepoPrepare.ownerId = DepositStub.get().ownerId
    }
}
