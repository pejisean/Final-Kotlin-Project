package com.example.kotlin_final_project

object UserDatabase {
    val users = mutableListOf<User>()
    var currentUserEmail: String? = null
}
