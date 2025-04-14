import kotlinx.coroutines.test.runTest
import mapper.fromTransport
import ru.otus.otuskotlin.marketplace.app.common.IYieldInsightsAppSettings
import ru.otus.yieldInsights.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerTest {
    private val request = DepositCreateRequest(
        deposit = DepositCreateObject(
            bankName = "bank1",
            depositName = "deposit1",
            rate = "18",
            openingDate = "2025-02-01",
            depositOperation = "",
        ),
        debug = DepositDebug(mode = DepositRequestDebugMode.STUB, stub = DepositRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IYieldInsightsAppSettings = object : IYieldInsightsAppSettings {
        override val corSettings: CorSettings = CorSettings()
        override val processor: DepositProcessor = DepositProcessor(corSettings)
    }

    private suspend fun createDepositSpring(request: DepositCreateRequest): DepositCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportCreate() as DepositCreateResponse },
            ControllerTest::class,
            "controller-test"
        )

    @Test
    fun springHelperTest() = runTest {
        val res = createDepositSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}