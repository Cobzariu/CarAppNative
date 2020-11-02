package com.example.carapp.todo.data

import androidx.lifecycle.LiveData
import com.example.carapp.core.Result
import com.example.carapp.todo.data.local.CarDao
import com.example.carapp.todo.data.remote.CarApi

class CarRepository(private val carDao: CarDao) {
    val items = carDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val items = CarApi.service.find()
            for (item in items) {
                carDao.insert(item)
            }
            return Result.Success(true)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<Car> {
        return carDao.getById(itemId)
    }

    suspend fun save(item: Car): Result<Car> {
        try {
            val createdItem = CarApi.service.create(item)
            carDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: Car): Result<Car> {
        try {
            val updatedItem = CarApi.service.update(item._id, item)
            carDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun delete(itemId: String): Result<Boolean> {
        try {

            CarApi.service.delete(itemId)
            carDao.delete(id = itemId)
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}