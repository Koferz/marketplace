package ru.otus.otuskotlin.yieldInsights.app.rabbit.controllers

import Context
import apiV1Mapper
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import controllerHelper
import fromTransport
import helpers.asDepositError
import models.State
import ru.otus.otuskotlin.yieldInsights.api.v1.mappers.toTransport
import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.DepositAppSettings
import ru.otus.yieldInsights.api.v1.models.IRequest

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectControllerV1(
    private val appSettings: DepositAppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV1,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV1Mapper.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransport()
                apiV1Mapper.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            },
            this@RabbitDirectControllerV1::class,
            "rabbitmq-v1-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = Context()
        e.printStackTrace()
        context.state = State.FAILING
        context.errors.add(e.asDepositError())
        val response = context.toTransport()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}
