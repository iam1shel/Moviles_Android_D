package com.tecsup.murocampus.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AcceptJsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithHeader = if (originalRequest.header("Accept") == null) {
            originalRequest.newBuilder()
                .header("Accept", "application/json")
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(requestWithHeader)
    }
}
