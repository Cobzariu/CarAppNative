package com.example.carapp.todo.data.remote

import retrofit2.http.*
import com.example.carapp.core.Api
import com.example.carapp.todo.data.Car
import retrofit2.Response

object CarApi {
    interface Service {
        @GET("/api/car")
        suspend fun find(): List<Car>

        @GET("/api/car/{id}")
        suspend fun read(@Path("id") itemId: String): Car;

        @Headers("Content-Type: application/json")
        @POST("/api/car")
        suspend fun create(@Body item: Car): Car

        @Headers("Content-Type: application/json")
        @PUT("/api/car/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Car): Car

        @DELETE("/api/car/{id}")
        suspend fun delete(@Path("id") itemId: String): Response<Unit>
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}