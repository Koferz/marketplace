import config.DepositConfig
import controllers.DepositControllerV1
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.yieldInsights.api.v1.models.*
import ru.otus.yieldInsights.biz.DepositProcessor
import ru.otus.otuskotlin.yieldInsights.api.v1.mappers.*
import kotlin.test.Test

@WebFluxTest(DepositControllerV1::class, DepositConfig::class)
@ContextConfiguration
@SpringBootTest
internal class DepositControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: DepositProcessor

    @Test
    fun createDeposit() = testStubDeposit(
        "/v1/deposit/create",
        DepositCreateRequest(),
        Context().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readDeposit() = testStubDeposit(
        "/v1/deposit/read",
        DepositReadRequest(),
        Context().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateDeposit() = testStubDeposit(
        "/v1/deposit/update",
        DepositUpdateRequest(),
        Context().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteDeposit() = testStubDeposit(
        "/v1/deposit/delete",
        DepositDeleteRequest(),
        Context().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchDeposit() = testStubDeposit(
        "/v1/deposit/search",
        DepositSearchRequest(),
        Context().toTransportSearch().copy(responseType = "search")
    )

    @Test
    fun closingDeposit() = testStubDeposit(
        "/v1/deposit/closing",
        DepositClosingRequest(),
        Context().toTransportClosing().copy(responseType = "closing")
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