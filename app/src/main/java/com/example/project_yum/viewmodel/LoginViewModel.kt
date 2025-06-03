package com.example.project_yum.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_yum.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.project_yum.model.User
import com.example.project_yum.retro.UserRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var usernameError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    var usernameTouched by mutableStateOf(false)
    var passwordTouched by mutableStateOf(false)

    var loggedUser by mutableStateOf<User?>(null)
    var loginError by mutableStateOf<String?>(null)


    val allValid: Boolean
        get() = usernameError == null && passwordError == null

    fun validate() {
        usernameError = when {
            username.isBlank() -> "Username is required"
            !isMinLength(username, 6) -> "At least 6 characters"
            else -> null
        }
        passwordError = when {
            password.isBlank() -> "Password is required"
            !isMinLength(password, 6) -> "At least 6 characters"
            else -> null
        }
    }

    fun login(
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val users = UserRetrofitInstance.api.loginUser(username, password)
                if (users.isNotEmpty()) {
                    val user = users.first()
                    loggedUser = user
                    withContext(Dispatchers.Main) {
                        onSuccess(user)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("Wrong username or password")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.localizedMessage}")
                }
            }
        }
    }
}