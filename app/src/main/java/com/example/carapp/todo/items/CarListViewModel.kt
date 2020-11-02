package com.example.carapp.todo.items

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.carapp.core.Result
import com.example.carapp.core.TAG
import com.example.carapp.todo.data.Car
import com.example.carapp.todo.data.CarRepository
import com.example.carapp.todo.data.local.CarDatabase
import com.example.carapp.todo.data.local.CarDatabase.Companion.getDatabase

class CarListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<Car>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val carRepository: CarRepository
    init {
        val carDao = CarDatabase.getDatabase(application,viewModelScope).carDao()
        carRepository = CarRepository(carDao)
        items = carRepository.items
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = carRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
