import models.Deposit
import models.DepositId
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositIdRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErr
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErrWithData
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoDepositDeleteTest {
    abstract val repo: IRepoDeposit
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = DepositId("Deposit-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteDeposit(DbDepositIdRequest(deleteSucc.depositId, lock = lockOld))
        assertIs<DbDepositResponseOk>(result)
        assertEquals(deleteSucc.rate, result.data.rate)
        assertEquals(deleteSucc.depositTerm, result.data.depositTerm)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readDeposit(DbDepositIdRequest(notFoundId, lock = lockOld))

        assertIs<DbDepositResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteDeposit(DbDepositIdRequest(deleteConc.depositId, lock = lockBad))

        assertIs<DbDepositResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitDeposits("delete") {
        override val initObjects: List<Deposit> = listOf(
            createInitTestModel("delete", null),
            createInitTestModel("deleteLock", null),
        )
    }
}
