package com.example.carapp.todo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.carapp.todo.data.Car

@Dao
interface CarDao {
    @Query("SELECT * FROM cars ORDER BY name ASC")
    fun getAll(): LiveData<List<Car>>
    @Query("SELECT * FROM cars WHERE _id=:id")
    fun getById(id: String): LiveData<Car>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: Car)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(car: Car)

    @Query("DELETE FROM cars WHERE _id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM cars")
    suspend fun deleteAll()
}