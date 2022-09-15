package com.example.testseamfix.utils

import com.example.testseamfix.model.ApiResult
import com.squareup.moshi.JsonEncodingException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.reflect.KClass

object SafeResult {

    suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>
    ): ApiResult<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody == null) {
                    ApiResult.Error(RuntimeException("No response body available"))
                } else ApiResult.Success(responseBody)
            } else if (response.code() in 500..599) {
                ApiResult.Error(Exception("Internal server error"))
            } else {
                ApiResult.Error(Exception("An error has occured"))
            }
        } catch (e: Throwable) {
            println("throwgin $e")
            val networkExceptions: List<KClass<out IOException>> =
                listOf(
                    ConnectException::class,
                    UnknownHostException::class
                )
            if (e::class in networkExceptions) {
                ApiResult.Error(Exception("Network connection problem"))
            } else if (e is SocketTimeoutException) {
                ApiResult.Error(Exception("Time out"))
            } else if (e is JsonEncodingException) {
                ApiResult.Error(Exception("Error mapping: check your input"))
            } else ApiResult.Error(Exception("Something went wrong"))
        }
    }
}