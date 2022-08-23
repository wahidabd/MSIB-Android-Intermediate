package com.wahidabd.dicodingstories.core

import com.wahidabd.dicodingstories.data.response.GenericResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject

class ErrorParser @Inject constructor(private val retrofit: Retrofit) {

    fun converterGenericError(error: ResponseBody): GenericResponse? {
        val converter: Converter<ResponseBody, GenericResponse> = retrofit
            .responseBodyConverter(GenericResponse::class.java, arrayOfNulls<Annotation>(0))
        return converter.convert(error)
    }

}