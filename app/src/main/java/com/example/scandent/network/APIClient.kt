package com.example.scandent.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIClient {


    private val baseURL: String = "https://reqres.in/api/"
    private var retofit: Retrofit? = null


    val client: Retrofit
        get() {
            if (retofit == null) {

                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

                retofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder()
                                .setLenient()
                                .create()
                        )
                    )
                    .build()
            }
            return retofit!!
        }


}