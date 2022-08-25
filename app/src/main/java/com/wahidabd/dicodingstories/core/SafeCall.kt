package com.wahidabd.dicodingstories.core

import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.utils.Constants.TIMEOUT_ERROR
import com.wahidabd.dicodingstories.utils.Constants.UNKNOWN_ERROR
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.SocketTimeoutException

class SafeCall {

    suspend fun <T> enqueue(
        converter: (ResponseBody) -> GenericResponse?,
        call: suspend () -> Response<T>
    ): Resource<T> =
        try {
            val res = call()
            val body = res.body()
            val errorBody = res.errorBody()

            if (res.isSuccessful && body != null) {
                Resource.success(body)
            } else if (errorBody != null) {
                val parsedError = converter(errorBody)
                Resource.error(parsedError?.message.toString(), null)
            } else {
                Resource.error(UNKNOWN_ERROR, null)
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> Resource.error(TIMEOUT_ERROR, null)
                else -> Resource.error(UNKNOWN_ERROR, null)
            }
        }

    suspend fun <T, R> enqueue(
        req: T,
        converter: (ResponseBody) -> GenericResponse?,
        call: suspend (T) -> Response<R>
    ): Resource<R> =
        try {
            val res = call(req)
            val body = res.body()
            val errorBody = res.errorBody()

            if (res.isSuccessful && body != null) {
                Resource.success(body)
            } else if (errorBody != null) {
                val parsedError = converter(errorBody)
                Resource.error(parsedError?.message.toString(), null)
            } else {
                Resource.error(UNKNOWN_ERROR, null)
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> Resource.error(TIMEOUT_ERROR, null)
                else -> Resource.error(UNKNOWN_ERROR, null)
            }
        }

    suspend fun <T, U, R> enqueue(
        req1: T,
        req2: U,
        converter: (ResponseBody) -> GenericResponse?,
        call: suspend (T, U) -> Response<R>
    ): Resource<R> =
        try {
            val res = call(req1, req2)
            val body = res.body()
            val errorBody = res.errorBody()

            if (res.isSuccessful && body != null) {
                Resource.success(body)
            } else if (errorBody != null) {
                val parsedError = converter(errorBody)
                Resource.error(parsedError?.message.toString(), null)
            } else {
                Resource.error(UNKNOWN_ERROR, null)
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> Resource.error(TIMEOUT_ERROR, null)
                else -> Resource.error(UNKNOWN_ERROR, null)
            }
        }

    suspend fun <T, U, S, V, R> enqueue(
        req1: T,
        req2: U,
        req3: S,
        req4: V,
        converter: (ResponseBody) -> GenericResponse?,
        call: suspend (T, U, S, V) -> Response<R>
    ): Resource<R> =
        try {
            val res = call(req1, req2, req3, req4)
            val body = res.body()
            val errorBody = res.errorBody()

            if (res.isSuccessful && body != null) {
                Resource.success(body)
            } else if (errorBody != null) {
                val parsedError = converter(errorBody)
                Resource.error(parsedError?.message.toString(), null)
            } else {
                Resource.error(UNKNOWN_ERROR, null)
            }
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> Resource.error(TIMEOUT_ERROR, null)
                else -> Resource.error(UNKNOWN_ERROR, null)
            }
        }

}