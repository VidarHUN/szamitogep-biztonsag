package com.example.myapplication.ui.admin.adapter

data class UserItem (
    val public_id: String,
    val username: String,
    val email: String,
    var password: String,
)