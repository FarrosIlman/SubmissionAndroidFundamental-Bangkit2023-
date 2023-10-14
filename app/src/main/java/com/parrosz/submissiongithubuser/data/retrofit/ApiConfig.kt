package com.parrosz.submissiongithubuser.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import com.parrosz.submissiongithubuser.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val authInterceptor = Interceptor { client ->
                val req = client.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", BuildConfig.GITHUB_API_KEY)
                    .build()
                client.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.GITHUB_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}