import config.DepositConfig
import controllers.DepositControllerV2
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.yieldInsights.api.v2.mappers.*
import ru.otus.yieldInsights.api.v2.models.*
import ru.otus.yieldInsights.biz.DepositProcessor
import kotlin.test.Test

@WebFluxTest(DepositControllerV2::class, DepositConfig::class)
internal class DepositControllerV2Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: DepositProcessor

    @Test
    fun createDeposit() = testStubDeposit(
        "/v2/deposit/create",
        DepositCreateRequest(),
        Context().toTransportCreate()
    )

    @Test
    fun readDeposit() = testStubDeposit(
        "/v2/deposit/read",
        DepositReadRequest(),
        Context().toTransportRead()
    )

    @Test
    fun updateDeposit() = testStubDeposit(
        "/v2/deposit/update",
        DepositUpdateRequest(),
        Context().toTransportUpdate()
    )

    @Test
    fun deleteDeposit() = testStubDeposit(
        "/v2/deposit/delete",
        DepositDeleteRequest(),
        Context().toTransportDelete()
    )

    @Test
    fun searchDeposit() = testStubDeposit(
        "/v2/deposit/search",
        DepositSearchRequest(),
        Context().toTransportSearch()
    )

    @Test
    fun depositClosing() = testStubDeposit(
        "/v2/deposit/closing",
        DepositClosingRequest(),
        Context().toTransportClosing()
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubDeposit(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}