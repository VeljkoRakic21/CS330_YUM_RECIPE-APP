package com.example.project_yum.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.project_yum.model.User
import com.example.project_yum.retro.UserRetrofitInstance
import com.example.project_yum.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var nameError by mutableStateOf<String?>(null)
    var lastNameError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var usernameError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    var nameTouched by mutableStateOf(false)
    var lastNameTouched by mutableStateOf(false)
    var emailTouched by mutableStateOf(false)
    var usernameTouched by mutableStateOf(false)
    var passwordTouched by mutableStateOf(false)

    val allValid: Boolean
        get() = listOf(
            nameError, lastNameError, emailError, usernameError, passwordError
        ).all { it == null }

    fun validate() {
        nameError = when {
            name.isBlank() -> "First name is required"
            !isAlpha(name) -> "Only letters allowed"
            else -> null
        }
        lastNameError = when {
            lastName.isBlank() -> "Last name is required"
            !isAlpha(lastName) -> "Only letters allowed"
            else -> null
        }
        emailError = when {
            email.isBlank() -> "Email is required"
            !isValidEmail(email) -> "Invalid email"
            else -> null
        }
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

    fun registerUser(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existingUsers = UserRetrofitInstance.api.getUserByUsername(user.username)
                if (existingUsers.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        onError("Account with that username already exists.")
                    }
                } else {
                    UserRetrofitInstance.api.registerUser(user).enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                onSuccess()
                            } else {
                                onError("Registration failed: ${response.code()}")
                            }
                        }
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            onError("Network error: ${t.message}")
                        }
                    })
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Network error or processing: ${e.localizedMessage}")
                }
            }
        }
    }
}