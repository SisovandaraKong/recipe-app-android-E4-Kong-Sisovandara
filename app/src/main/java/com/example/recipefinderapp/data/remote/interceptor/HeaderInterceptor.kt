package com.example.recipefinderapp.data.remote.interceptor

import com.example.recipefinderapp.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(Constants.DB_NAME_HEADER, Constants.YOUR_GUID)
            .build()
        return chain.proceed(request)
    }
}