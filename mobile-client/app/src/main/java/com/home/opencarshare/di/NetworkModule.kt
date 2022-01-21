package com.home.opencarshare.di

import android.content.Context
import com.home.opencarshare.R
import com.home.opencarshare.network.IApi
import com.home.opencarshare.network.MockInterceptor
import com.home.opencarshare.network.PlainInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO keep an eye on component for DI, each component requires own definition ()
@Module
@InstallIn(ViewModelComponent::class)
open class NetworkModule() {

    @Provides
    fun provideInterceptor(@ApplicationContext context: Context): Interceptor {
//        return MockInterceptor(context)
        return PlainInterceptor()
    }

    @Provides
    fun provideClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideApi(@ApplicationContext context: Context, client: OkHttpClient): IApi {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(context.getString(R.string.server_url))
            .build()
            .create(IApi::class.java)
    }
}