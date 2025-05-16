package config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.otus.otuskotlin.yieldInsights.repo.postgresql.SqlProperties

@ConfigurationProperties(prefix = "psql")
data class DepositConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "yieldInsights-pass",
    var database: String = "yieldInsights_dep",
    var schema: String = "public",
    var table: String = "dep",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}

