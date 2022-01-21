package com.home.opencarshare.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.Throws

class PlainInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}