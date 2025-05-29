package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import models.*
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoPrepareUpdate(depositName: String) = worker {
    this.title = depositName
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == State.RUNNING }
    handle {
        depositRepoPrepare = depositRepoRead.deepCopy().apply {
            this.depositName = depositValidated.depositName
            rate = depositValidated.rate
            depositTerm = depositValidated.depositTerm
            depositAmount = depositValidated.depositAmount
            lock = depositValidated.lock
        }
    }
}
