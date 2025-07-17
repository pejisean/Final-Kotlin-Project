package com.example.kotlin_final_project

data class User(
    val name: String,
    val email: String,
    val passwordHash: String,
    val creationDate: String,
    val notes: MutableList<Note> = mutableListOf()
)
