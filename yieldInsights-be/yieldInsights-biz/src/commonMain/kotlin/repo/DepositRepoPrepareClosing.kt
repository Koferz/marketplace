package ru.otus.otuskotlin.yieldInsights.biz.repo

import Context
import models.State
import ru.otus.otuskotlin.yieldInsights.cor.ICorChainDsl
import ru.otus.otuskotlin.yieldInsights.cor.worker


fun ICorChainDsl<Context>.repoPrepareClosing(title: String) = worker {
    this.title = title
    description = "Готовим данные к поиску предложений в БД"
    on { state == State.RUNNING }
    handle {
        depositRepoPrepare = depositRepoRead.deepCopy()
        depositRepoDone = depositRepoRead.deepCopy()
    }
}
