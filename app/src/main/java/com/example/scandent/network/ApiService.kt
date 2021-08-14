package com.example.scandent.network



import com.example.scandent.model.registerRequestModel
import com.example.scandent.model.registerResponseModel
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiService {
    @POST("register")
    suspend fun register(
        @Body registerModel: registerRequestModel
    ): Response<registerResponseModel>
}