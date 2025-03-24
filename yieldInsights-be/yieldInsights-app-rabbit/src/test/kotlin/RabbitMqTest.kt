import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.yieldInsights.app.rabbit.RabbitApp
import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.DepositAppSettings
import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.yieldInsights.api.v1.models.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


//  тесты с использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
        const val RMQ_PORT = 5672

        private val container = run {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
            RabbitMQContainer("rabbitmq:latest").apply {
//                withExposedPorts(5672, 15672) // Для 3-management
                withExposedPorts(RMQ_PORT)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
//            println("CONTAINER PORT (15672): ${container.getMappedPort(15672)}")
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = DepositAppSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
//        corSettings = MkplCorSettings(loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }),
        controllersConfigV1 = RabbitExchangeConfiguration(
            keyIn = "in-v1",
            keyOut = "out-v1",
            exchange = exchange,
            queue = "v1-queue",
            consumerTag = "v1-consumer-test",
            exchangeType = exchangeType
        ),
        controllersConfigV2 = RabbitExchangeConfiguration(
            keyIn = "in-v2",
            keyOut = "out-v2",
            exchange = exchange,
            queue = "v2-queue",
            consumerTag = "v2-consumer-test",
            exchangeType = exchangeType
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun depositCreateTestV1() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV1) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(depositCreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, DepositCreateResponse::class.java)
                val expected = DepositStub.get()

                assertEquals(expected.depositName, response.deposit?.depositName)
                assertEquals(expected.rate, response.deposit?.rate)
                assertEquals(expected.depositTerm, response.deposit?.depositTerm)
            }
        }
    }

    private val depositCreateV1 = with(DepositStub.get()) {
        DepositCreateRequest(
            deposit = DepositCreateObject(
                depositName = depositName,
                rate = rate,
                depositTerm = depositTerm
            ),
            requestType = "create",
            debug = DepositDebug(
                mode = DepositRequestDebugMode.STUB,
                stub = DepositRequestDebugStubs.SUCCESS
            )
        )
    }
}
