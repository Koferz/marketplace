package controllers

import config.AppSettings
import controllerHelper
import fromTransport
import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.yieldInsights.api.v1.mappers.toTransport
import ru.otus.yieldInsights.api.v1.models.*
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/ad")
class DepositControllerV1(
    private val appSettings: AppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: DepositCreateRequest): DepositCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: DepositReadRequest): DepositReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: DepositUpdateRequest): DepositUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: DepositDeleteRequest): DepositDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: DepositSearchRequest): DepositSearchResponse =
        process(appSettings, request = request, this::class, "search")

    @PostMapping("offers")
    suspend fun  offers(@RequestBody request: DepositClosingRequest): DepositClosingResponse =
        process(appSettings, request = request, this::class, "closing")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: AppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransport() as R },
            clazz,
            logId,
        )
    }
}
