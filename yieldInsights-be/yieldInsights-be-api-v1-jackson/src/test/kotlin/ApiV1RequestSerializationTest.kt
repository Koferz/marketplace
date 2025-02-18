import ru.otus.yieldInsights.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApiV1RequestSerializationTest {
    private val request = DepositCreateRequest(
        debug = DepositDebug(
            mode = DepositRequestDebugMode.STUB,
            stub = DepositRequestDebugStubs.BAD_DEPOSIT_AMOUNT
        ),
        deposit = DepositCreateObject(
            bankName = "bank1",
            depositName = "deposit1",
            rate = "18",
            openingDate = "2025-02-03",
            depositTerm = "180",
            depositAmount = "150000",
            depositOperation = "close",
            isActive = "true"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"bankName\":\\s*\"bank1\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badDepositAmount\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as DepositCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"deposit": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, DepositCreateRequest::class.java)

        assertEquals(null, obj.deposit)
    }
}