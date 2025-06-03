package com.example.project_yum.model

data class User(
    val id: String? = null,
    val name: String,
    val lastName: String,
    val email: String,
    val username: String,
    val password: String,
    val favorites: List<Int> = emptyList()
)