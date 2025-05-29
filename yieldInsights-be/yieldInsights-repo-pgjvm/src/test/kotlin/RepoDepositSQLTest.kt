import com.benasher44.uuid.uuid4
import models.Deposit
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.otus.otuskotlin.yieldInsights.repo.postgresql.*
import java.io.File
import java.time.Duration
import kotlin.test.AfterTest
import kotlin.test.Ignore

@RunWith(Enclosed::class)
class RepoDepositSQLTest {

    class RepoDepositSQLCreateTest : RepoDepositCreateTest() {
        override val repo = repoUnderTestContainer(
            Companion.initObjects,
            randomUuid = { uuidNew.asString() },
        )

        @AfterTest
        fun tearDown(): Unit = repo.clear()
    }

    class RepoDepositSQLReadTest : RepoDepositReadTest() {
        override val repo = repoUnderTestContainer(Companion.initObjects)

        @AfterTest
        fun tearDown(): Unit = repo.clear()
    }

    class RepoDepositSQLUpdateTest : RepoDepositUpdateTest() {
        override val repo = repoUnderTestContainer(
            Companion.initObjects,
            randomUuid = { lockNew.asString() },
        )

        @AfterTest
        fun tearDown(): Unit = repo.clear()
    }

    class RepoDepositSQLDeleteTest : RepoDepositDeleteTest() {
        override val repo = repoUnderTestContainer(Companion.initObjects)

        @AfterTest
        fun tearDown(): Unit = repo.clear()
    }

    class RepoDepositSQLSearchTest : RepoDepositSearchTest() {
        override val repo = repoUnderTestContainer(Companion.initObjects)

        @AfterTest
        fun tearDown(): Unit = repo.clear()
    }

    @Ignore
    companion object {
        private const val PG_SERVICE = "psql"
        private const val MG_SERVICE = "liquibase"

        // val LOGGER = org.slf4j.LoggerFactory.getLogger(ComposeContainer::class.java)
        private val container: ComposeContainer by lazy {
            val res = this::class.java.classLoader.getResource("docker-compose-pg.yml")
                ?: throw Exception("No resource found")
            val file = File(res.toURI())
            //  val logConsumer = Slf4jLogConsumer(LOGGER)
            ComposeContainer(
                file,
            )
                .withExposedService(PG_SERVICE, 5432)
                .withStartupTimeout(Duration.ofSeconds(300))
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                )
        }

        private const val HOST = "localhost"
        private const val USER = "postgres"
        private const val PASS = "yieldInsights-pass"
        private val PORT by lazy {
            container.getServicePort(PG_SERVICE, 5432) ?: 5432
        }

        fun repoUnderTestContainer(
            initObjects: Collection<Deposit> = emptyList(),
            randomUuid: () -> String = { uuid4().toString() },
        ): IRepoDepositInitializable =
            DepositRepoInitialized(
                repo = RepoDepositSql(
                    SqlProperties(
                        host = HOST,
                        user = USER,
                        password = PASS,
                        port = PORT,
                    ),
                    randomUuid = randomUuid
                ),
                initObjects = initObjects,
            )

        @JvmStatic
        @BeforeClass
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun finish() {
            container.stop()
        }
    }
}