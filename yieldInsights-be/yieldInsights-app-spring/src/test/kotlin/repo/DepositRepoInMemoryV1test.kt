package repo

import DepositRepoInitialized
import com.ninjasquad.springmockk.MockkBean
import config.DepositConfig
import controllers.DepositControllerV1
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import ru.otus.otuskotlin.marketplace.common.repo.IRepoDeposit
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositFilterRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositIdRequest
import ru.otus.otuskotlin.yieldInsights.common.repo.DbDepositRequest
import ru.otus.otuskotlin.yieldInsights.repo.inmemory.DepositRepoInMemory
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(
    DepositControllerV1::class, DepositConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)
internal class DepositRepoInMemoryV1test : DepositRepoBaseV1Test() {

    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoDeposit

    @BeforeEach
    fun tearUp() {
        val slotAd = slot<DbDepositRequest>()
        val slotId = slot<DbDepositIdRequest>()
        val slotFl = slot<DbDepositFilterRequest>()
        val repo = DepositRepoInitialized(
            repo = DepositRepoInMemory(randomUuid = { uuidNew }),
            initObjects = DepositStub.prepareSearchList("xx", "18") + DepositStub.get()
        )
        coEvery { testTestRepo.createDeposit(capture(slotAd)) } coAnswers { repo.createDeposit(slotAd.captured) }
        coEvery { testTestRepo.readDeposit(capture(slotId)) } coAnswers { repo.readDeposit(slotId.captured) }
        coEvery { testTestRepo.updateDeposit(capture(slotAd)) } coAnswers { repo.updateDeposit(slotAd.captured) }
        coEvery { testTestRepo.deleteDeposit(capture(slotId)) } coAnswers { repo.deleteDeposit(slotId.captured) }
        coEvery { testTestRepo.searchDeposit(capture(slotFl)) } coAnswers { repo.searchDeposit(slotFl.captured) }
    }

    @Test
    override fun createDeposit() = super.createDeposit()

    @Test
    override fun readDeposit() = super.readDeposit()

    @Test
    override fun updateDeposit() = super.updateDeposit()

    @Test
    override fun deleteDeposit() = super.deleteDeposit()

    @Test
    override fun searchDeposit() = super.searchDeposit()

    @Test
    override fun closingDeposit() = super.closingDeposit()
}
