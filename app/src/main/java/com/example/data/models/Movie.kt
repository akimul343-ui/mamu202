package com.example.data.models

data class Movie(
    val id: String,
    val titleBangla: String,
    val titleEnglish: String,
    val year: String,
    val director: String,
    val coStar: String,
    val genre: String,
    val boxOffice: String,
    val rating: String,
    val synopsis: String,
    val iconicDialogue: String,
    val trailerUrl: String,
    val posterDrawable: Int? = null,
    val isBlockbuster: Boolean = true
)
