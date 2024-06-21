package com.example.freshscanapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        private const val BASE_URL_1 = "https://freshcan-project-426108.et.r.appspot.com/"
        private const val BASE_URL_2 = "https://ml-freshcan-fw3nhsnjsa-et.a.run.app/"

        private fun getRetrofit(baseUrl: String): Retrofit {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        fun getApiService1(): ApiService {
            return getRetrofit(BASE_URL_1).create(ApiService::class.java)
        }

        fun getApiService2(): ApiService {
            return getRetrofit(BASE_URL_2).create(ApiService::class.java)
        }
    }
}
