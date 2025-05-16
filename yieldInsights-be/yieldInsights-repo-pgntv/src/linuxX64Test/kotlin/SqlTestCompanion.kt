package ru.otus.otuskotlin.yieldInsights.repo.postgresql

import DepositRepoInitialized
import IRepoDepositInitializable
import com.benasher44.uuid.uuid4
import models.Deposit
import ru.otus.otuskotlin.yieldInsights.repo.postgresql.ntv.RepoDepositSql

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "yieldInsights-pass"
    private val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<Deposit> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IRepoDepositInitializable = DepositRepoInitialized(
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
}

