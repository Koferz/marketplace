import models.Deposit
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositFilterRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoDepositSearchTest {
    abstract val repo: IRepoDeposit

    protected open val initializedObjects: List<Deposit> = initObjects

    @Test
    fun searchRate() = runRepoTest {
        val result = repo.searchDeposit(DbDepositFilterRequest(rateFilter = SEARCHFILTER))
        assertIs<DbDepositsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.depositId.asString() }
        assertEquals(expected, result.data.sortedBy { it.depositId.asString() })
    }

    companion object: BaseInitDeposits("search") {

        const val SEARCHFILTER = "12"
        override val initObjects: List<Deposit> = listOf(
            createInitTestModel("ad1", null),
            createInitTestModel("ad2", rate = SEARCHFILTER)
        )
    }
}
