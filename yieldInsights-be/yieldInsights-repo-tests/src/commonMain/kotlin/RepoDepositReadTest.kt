import models.Deposit
import models.DepositError
import models.DepositId
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositIdRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErr
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDepositReadTest {
    abstract val repo: IRepoDeposit
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readDeposit(DbDepositIdRequest(readSucc.depositId))

        assertIs<DbDepositResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readDeposit(DbDepositIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbDepositResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: DepositError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDeposits("read") {
        override val initObjects: List<Deposit> = listOf(
            createInitTestModel("read", null)
        )

        val notFoundId = DepositId("deposit-repo-read-notFound")

    }
}
