package com.example.kotlin_final_project

import java.io.Serializable
import java.util.UUID // Import UUID for generating unique IDs

data class Note(
    val id: String, //Database ID
    var title: String,    // Make var if you intend to directly modify in object (less common for data classes)
    var content: String,  // Make var if you intend to directly modify in object
    val date: String
) : Serializable