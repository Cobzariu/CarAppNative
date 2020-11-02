package com.example.carapp.todo.data

import com.example.carapp.core.Result
import com.example.carapp.todo.data.remote.CarApi

object CarRepository {
    private var cachedItems: MutableList<Car>? = null;

    suspend fun loadAll(): Result<List<Car>> {
        if (cachedItems != null) {
            return Result.Success(cachedItems as List<Car>);
        }
        try {
            val items = CarApi.service.find()
            cachedItems = mutableListOf()
            cachedItems?.addAll(items)
            return Result.Success(cachedItems as List<Car>)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun load(itemId: String): Result<Car> {
        val item = cachedItems?.find { it._id == itemId }
        if (item != null) {
            return Result.Success(item)
        }
        try {
            return Result.Success(CarApi.service.read(itemId))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun save(item: Car): Result<Car> {
        try {
            val createdItem = CarApi.service.create(item)
            cachedItems?.add(createdItem)
            return Result.Success(createdItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: Car): Result<Car> {
        try {
            val updatedItem = CarApi.service.update(item._id, item)
            val index = cachedItems?.indexOfFirst { it._id == item._id }
            if (index != null) {
                cachedItems?.set(index, updatedItem)
            }
            return Result.Success(updatedItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
    suspend fun delete(itemId: String): Result<Boolean>
    {
        try {

            val index = cachedItems?.indexOfFirst { it._id == itemId }
            if (index!=null)
            {
                cachedItems?.removeAt(index)
            }
            return Result.Success(true)
        }
        catch (e: Exception) {
            return Result.Error(e)
        }
    }
}