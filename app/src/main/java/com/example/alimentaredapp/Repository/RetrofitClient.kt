package com.example.alimentaredapp.Repository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    const val URL_RENDER = "https://alimentared.onrender.com/api/"
    const val URL_LOCAL_EMULATOR = "http://10.0.2.2:8080/api/"

    var baseUrl: String = URL_RENDER
        private set

    private var _retrofitInstance: Retrofit? = null

    fun useLocalBackend(useLocal: Boolean) {
        baseUrl = if (useLocal) URL_LOCAL_EMULATOR else URL_RENDER
        _retrofitInstance = null // Reinicia la instancia para que tome la nueva URL
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    val instance: Retrofit
        get() {
            if (_retrofitInstance == null) {
                _retrofitInstance = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return _retrofitInstance!!
        }
}
