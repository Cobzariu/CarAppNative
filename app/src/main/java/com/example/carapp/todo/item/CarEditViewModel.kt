package com.example.carapp.todo.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.carapp.core.Result
import com.example.carapp.core.TAG
import com.example.carapp.todo.data.Car
import com.example.carapp.todo.data.CarRepository
import com.example.carapp.todo.data.local.CarDatabase

class CarEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val carRepository: CarRepository
    init {
        val carDao = CarDatabase.getDatabase(application,viewModelScope).carDao()
        carRepository= CarRepository(carDao)
    }

    fun getItemById(itemId: String): LiveData<Car> {
        Log.v(TAG, "getItemById...")
        return carRepository.getById(itemId)
    }

    fun saveOrUpdateItem(car: Car) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Car>
            if (car._id.isNotEmpty()) {
                result = carRepository.update(car)
            } else {
                result = carRepository.save(car)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
    fun deleteItem(itemId: String)
    {
        viewModelScope.launch {
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Boolean> = carRepository.delete(itemId)
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "delete succeeded");
//                    mutableItem.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "delete failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}
