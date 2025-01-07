package com.sdu.kmpbook.core.data

import com.sdu.kmpbook.core.domain.DataError.Remote
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext
import com.sdu.kmpbook.core.domain.Result

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, Remote> {
    val response = try {
        execute()
    } catch(e: SocketTimeoutException) {
        return Result.Error(Remote.REQUEST_TIMEOUT)
    } catch(e: UnresolvedAddressException) {
        return Result.Error(Remote.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(Remote.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, Remote> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                Result.Error(Remote.SERIALIZATION)
            }
        }
        408 -> Result.Error(Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(Remote.SERVER)
        else -> Result.Error(Remote.UNKNOWN)
    }
}