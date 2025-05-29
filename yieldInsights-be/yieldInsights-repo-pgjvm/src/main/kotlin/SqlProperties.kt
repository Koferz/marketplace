package ru.otus.otuskotlin.yieldInsights.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "yieldInsights-pass",
    val database: String = "yieldInsights_dep",
    val schema: String = "public",
    val table: String = "dep",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
