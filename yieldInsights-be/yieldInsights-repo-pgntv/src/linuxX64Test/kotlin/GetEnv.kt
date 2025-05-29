package ru.otus.otuskotlin.yieldInsights.repo.postgresql

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
fun getEnv(name: String): String? = getenv(name)?.toKString()
