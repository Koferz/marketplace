package repo

import Context
import CorSettings
import DepositProcessor
import DepositRepositoryMock
import kotlinx.coroutines.test.runTest
import models.Deposit
import models.DepositId
import models.DepositLock
import models.State
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import ru.otus.otuskotlin.yieldInsights.common.repo.errorNotFound
import kotlin.test.assertEquals
import models.*

private val initDeposit = Deposit(
    depositId = DepositId("123"),
    depositName = "abc",
    rate = "18",
)
private val repo = DepositRepositoryMock(
        invokeReadDeposit = {
            if (it.id == initDeposit.depositId) {
                DbDepositResponseOk(
                    data = initDeposit,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = CorSettings(repoTest = repo)
private val processor = DepositProcessor(settings)

fun repoNotFoundTest(command: Command) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        depositRequest = Deposit(
            depositId = DepositId("12345"),
            depositName = "xyz",
            rate = "18",
            lock = DepositLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(State.FAILING, ctx.state)
    assertEquals(Deposit(), ctx.depositResponse)
    assertEquals(1, ctx.errors.size)
}
