package com.github.judrummer.kithub.data

import com.github.judrummer.kithub.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


/**
 * Created by judrummer on 9/5/2560.
 */

fun appRetrofit(): Retrofit {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    val httpClient = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) httpClient.addInterceptor(logging)
    return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
}


inline fun <reified T> Retrofit.createApi() = create(T::class.java)

inline fun <reified T> RetrofitApi() = appRetrofit().createApi<T>()
