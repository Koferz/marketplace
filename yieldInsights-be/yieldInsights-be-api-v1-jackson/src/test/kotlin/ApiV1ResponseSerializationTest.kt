import ru.otus.yieldInsights.api.v1.models.DepositCreateResponse
import ru.otus.yieldInsights.api.v1.models.DepositResponseObject
import ru.otus.yieldInsights.api.v1.models.IResponse
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApiV1ResponseSerializationTest {
    private val response = DepositCreateResponse(
        deposit = DepositResponseObject(
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
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"depositName\":\\s*\"deposit1\""))
        assertContains(json, Regex("\"depositTerm\":\\s*\"180\""))
        assertContains(json, Regex("\"depositOperation\":\\s*\"close\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as DepositCreateResponse

        assertEquals(response, obj)
    }
}