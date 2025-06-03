package com.example.project_yum.util

fun isAlpha(text: String): Boolean = text.matches(Regex("^[a-zA-Z]+$"))
fun isValidEmail(email: String): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
fun isMinLength(text: String, length: Int): Boolean = text.length >= length