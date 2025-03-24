import helpers.asDepositError
import ru.otus.otuskotlin.marketplace.app.common.IYieldInsightsAppSettings
import kotlinx.datetime.Clock
import models.State
import ru.otus.yieldInsights.marketplace.api.log1.mapper.toLog

import kotlin.reflect.KClass

suspend inline fun <T> IYieldInsightsAppSettings.controllerHelper(
    crossinline getRequest: suspend Context.() -> Unit,
    crossinline toResponse: suspend Context.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = Context(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = State.FAILING
        ctx.errors.add(e.asDepositError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
