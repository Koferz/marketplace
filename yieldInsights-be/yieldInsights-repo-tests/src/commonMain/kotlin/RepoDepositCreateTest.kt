import models.Deposit
import models.DepositId
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositResponseOk
import kotlin.test.*


abstract class RepoDepositCreateTest {
    abstract val repo: IRepoDepositInitializable
    protected open val uuidNew = DepositId("10000000-0000-0000-0000-000000000001")

    private val createObj = Deposit(
        depositName = "deposit1",
        rate = "18",
        openingDate = "2025-03-02",
        depositTerm = "180",
        depositAmount = "100",
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createDeposit(DbDepositRequest(createObj))
        val expected = createObj
        assertIs<DbDepositResponseOk>(result)
        assertEquals(expected.depositAmount, result.data.depositAmount)
        assertEquals(expected.depositName, result.data.depositName)
        assertEquals(expected.rate, result.data.rate)
        assertEquals(expected.openingDate, result.data.openingDate)
        assertNotEquals(expected.depositTerm, result.data.depositTerm)
    }

    companion object : BaseInitDeposits("create") {
        override val initObjects: List<Deposit> = emptyList()
    }
}
