package ru.otus.otuskotlin.yieldInsights.app.rabbit.controllers

import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.RabbitExchangeConfiguration

interface IRabbitMqController {
    val exchangeConfig: RabbitExchangeConfiguration
    suspend fun process()
    fun close()
}

