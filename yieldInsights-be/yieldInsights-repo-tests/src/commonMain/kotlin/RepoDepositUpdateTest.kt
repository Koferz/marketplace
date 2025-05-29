import models.Deposit
import models.DepositId
import models.DepositLock
import ru.otus.otuskotlin.marketplace.common.repo.*
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErr
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseErrWithData
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDepositUpdateTest {
    abstract val repo: IRepoDeposit
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = DepositId("ad-repo-update-not-found")
    protected val lockBad = DepositLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = DepositLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Deposit(
            depositId = updateSucc.depositId,
            rate = "update object",
            depositTerm = "update object description",
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Deposit(
        depositId = updateIdNotFound,
        rate = "update object not found",
        depositTerm = "update object not found description",
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        Deposit(
            depositId = updateConc.depositId,
            rate = "update object not found",
            depositTerm = "update object not found description",
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateDeposit(DbDepositRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbDepositResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbDepositResponseErrWithData)?.errors}")
        assertIs<DbDepositResponseOk>(result)
        assertEquals(reqUpdateSucc.depositId, result.data.depositId)
        assertEquals(reqUpdateSucc.rate, result.data.rate)
        assertEquals(reqUpdateSucc.depositTerm, result.data.depositTerm)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateDeposit(DbDepositRequest(reqUpdateNotFound))
        assertIs<DbDepositResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateDeposit(DbDepositRequest(reqUpdateConc))
        assertIs<DbDepositResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitDeposits("update") {
        override val initObjects: List<Deposit> = listOf(
            createInitTestModel("update", null),
            createInitTestModel("updateConc", null),
        )
    }
}
