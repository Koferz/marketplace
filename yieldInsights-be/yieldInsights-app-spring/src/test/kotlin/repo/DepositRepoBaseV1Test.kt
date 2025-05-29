package repo

import Context
import models.Deposit
import models.DepositId
import models.DepositLock
import models.State
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.yieldInsights.api.v1.mappers.*
import ru.otus.yieldInsights.api.v1.models.*
import toTransportCreate
import toTransportRead
import toTransportUpdate
import kotlin.test.Test

internal abstract class DepositRepoBaseV1Test {
    protected abstract var webClient: WebTestClient
    private val debug = DepositDebug(mode = DepositRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createDeposit() = testRepoDeposit(
        "create",
        DepositCreateRequest(
            deposit = DepositStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(DepositStub.prepareResult {
            depositId = DepositId(uuidNew)
            lock = DepositLock(uuidNew)
        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readDeposit() = testRepoDeposit(
        "read",
        DepositReadRequest(
            deposit = DepositStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(DepositStub.get())
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updateDeposit() = testRepoDeposit(
        "update",
        DepositUpdateRequest(
            deposit = DepositStub.prepareResult { depositName = "new" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(DepositStub.prepareResult { depositName = "add"; lock = DepositLock(uuidNew) })
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deleteDeposit() = testRepoDeposit(
        "delete",
        DepositDeleteRequest(
            deposit = DepositStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(DepositStub.get())
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchDeposit() = testRepoDeposit(
        "search",
        DepositSearchRequest(
            depositFilter = DepositSearchFilter(searchString = ""),
            debug = debug,
        ),
        Context(
            state = State.RUNNING,
            depositsResponse = DepositStub.prepareSearchList("xx", "18")
                //.onEach { it.permissionsClient.clear() }
                .sortedBy { it.depositId.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    open fun closingDeposit() = testRepoDeposit(
        "offers",
        DepositClosingRequest(
            deposit = DepositStub.get().toTransportRead(),
            debug = debug,
        ),
        Context(
            state = State.RUNNING,
            depositResponse = DepositStub.prepareResult { rate },
            depositsResponse = DepositStub.prepareSearchList("xx", "18")
                .sortedBy { it.depositId.asString() }
                .toMutableList()
        )
            .toTransportClosing().copy(responseType = "closing")
    )

    private fun prepareCtx(deposit: Deposit) = Context(
        state = State.RUNNING,
        depositResponse = deposit.apply {
            // Пока не реализована эта функциональность
            rate
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoDeposit(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/ad/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is DepositSearchResponse -> it.copy(deposits = it.deposits?.sortedBy { it.id })
                    is DepositClosingResponse -> it.copy(deposits = it.deposits?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
