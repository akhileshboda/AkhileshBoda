package com.example.a1_akhilboda_34396268_fit2081.fruity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FruityService {
    @GET("api/fruit/{name}")
    suspend fun getFruit(@Path("name") name: String): Fruit

    companion object {
        fun create(): FruityService {
            return Retrofit.Builder()
                .baseUrl("https://www.fruityvice.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FruityService::class.java)
        }
    }
}
