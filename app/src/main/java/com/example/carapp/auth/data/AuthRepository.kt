package com.example.carapp.auth.data

import android.util.Log
import com.example.carapp.auth.data.remote.RemoteAuthDataSource
import com.example.carapp.core.Api
import com.example.carapp.core.Constants
import com.example.carapp.core.Result
import com.example.carapp.core.TAG

object AuthRepository {
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        Constants.instance()?.deleteValueString("token")
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = RemoteAuthDataSource.login(user)
        if (result is Result.Success<TokenHolder>) {
            setLoggedInUser(user, result.data)
            Constants.instance()?.storeValueString("token",result.data.token);
        }
        return result
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        AuthRepository.user=user
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
