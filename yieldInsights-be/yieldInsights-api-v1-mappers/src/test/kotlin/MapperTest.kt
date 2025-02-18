import models.*
import org.junit.Test
import ru.otus.yieldInsights.api.v1.models.*
import stubs.Stubs
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = DepositCreateRequest(
            debug = DepositDebug(
                mode = DepositRequestDebugMode.STUB,
                stub = DepositRequestDebugStubs.SUCCESS,
            ),
            deposit = DepositCreateObject(
                bankName = "bank1",
                depositName = "deposit1",
                rate = "18",
                openingDate = "2025-02-10",
                depositTerm = "6",
                depositAmount = "150000"
            ),
        )

        val context = Context()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("PROLONGATION", context.depositRequest.depositOperation.toString())
    }

    @Test
    fun toTransport() {
        val context = Context(
            requestId = RequestId("1234"),
            command = Command.CREATE,
            depositResponse = Deposit(
                depositName = "deposit1",
                rate = "18",
                openingDate = "2025-02-10",
                depositTerm = "6",
                depositAmount = "150000",
                depositOperation = DepositOperation.CLOSE
            ),
            errors = mutableListOf(
                DepositError(
                    code = "err",
                    group = "request",
                    field = "bankName",
                    message = "wrong bankName",
                )
            ),
            state = State.RUNNING,
        )

        val req = context.toTransport() as DepositCreateResponse

        assertEquals("deposit1", req.deposit?.depositName)
        assertEquals("2025-02-10", req.deposit?.openingDate)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("bankName", req.errors?.firstOrNull()?.field)
        assertEquals("wrong bankName", req.errors?.firstOrNull()?.message)
    }
}