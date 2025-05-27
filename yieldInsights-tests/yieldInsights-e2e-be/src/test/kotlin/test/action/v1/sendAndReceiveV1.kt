package ru.otus.otuskotlin.yieldInsights.e2e.be.test.action.v1

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import co.touchlab.kermit.Logger
import ru.otus.otuskotlin.yieldInsights.e2e.be.fixture.client.Client
import ru.otus.yieldInsights.api.v1.models.IRequest
import ru.otus.yieldInsights.api.v1.models.IResponse

private val log = Logger

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV1RequestSerialize(request)
    log.i { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.i { "Received\n$responseBody" }

    return apiV1ResponseDeserialize(responseBody)
}
