package com.example.kotlin_final_project

import java.io.Serializable

data class Note(
    val title: String,
    val content: String,
    val date: String
) : Serializable
