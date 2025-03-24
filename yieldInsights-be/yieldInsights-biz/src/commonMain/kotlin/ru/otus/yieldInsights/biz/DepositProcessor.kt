package ru.otus.yieldInsights.biz

import Context
import CorSettings
import models.State

@Suppress("unused", "RedundantSuspendModifier")
class DepositProcessor(val corSettings: CorSettings) {
    suspend fun exec(ctx: Context) {
        ctx.depositResponse = DepositStub.get()
        ctx.depositsResponse = DepositStub.prepareSearchList("deposit search", "6").toMutableList()
        ctx.state = State.RUNNING
    }
}
