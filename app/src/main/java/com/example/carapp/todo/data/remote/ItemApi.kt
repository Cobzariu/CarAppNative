package com.example.carapp.todo.data.remote

import retrofit2.http.*
import com.example.carapp.core.Api
import com.example.carapp.todo.data.Item

object ItemApi {
    interface Service {
        @GET("/api/car")
        suspend fun find(): List<Item>

        @GET("/api/car/{id}")
        suspend fun read(@Path("id") itemId: String): Item;

        @Headers("Content-Type: application/json")
        @POST("/api/car")
        suspend fun create(@Body item: Item): Item

        @Headers("Content-Type: application/json")
        @PUT("/api/car/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Item): Item
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}