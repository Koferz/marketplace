package ru.otus.otuskotlin.yieldInsights.app.rabbit.controllers

import Context
import apiV2RequestDeserialize
import apiV2ResponseSerialize
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import controllerHelper
import helpers.asDepositError
import mapper.fromTransport
import models.State
import ru.otus.otuskotlin.yieldInsights.api.v2.mappers.toTransportAd
import ru.otus.otuskotlin.yieldInsights.app.rabbit.config.DepositAppSettings
import ru.otus.yieldInsights.api.v2.models.IRequest

class RabbitDirectControllerV2(
    private val appSettings: DepositAppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV2,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {

    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV2RequestDeserialize<IRequest>(String(message.body))
                fromTransport(req)
            },
            {
                val res = toTransportAd()
                apiV2ResponseSerialize(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
                }
            },
            RabbitDirectControllerV2::class,
            "rabbitmq-v2-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = Context()
        e.printStackTrace()
        context.state = State.FAILING
        context.errors.add(e.asDepositError())
        val response = context.toTransportAd()
        apiV2ResponseSerialize(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
        }
    }
}
