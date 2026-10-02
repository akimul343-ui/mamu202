package com.example.data.models

data class Song(
    val id: String,
    val title: String,
    val movie: String,
    val singers: String,
    val lyricist: String,
    val duration: String,
    val lyricsSnippet: String,
    val youtubeUrl: String,
    val viewsCount: String
)

data class Dialogue(
    val id: String,
    val quote: String,
    val movie: String,
    val character: String,
    val context: String
)

data class NewsItem(
    val id: String,
    val title: String,
    val summary: String,
    val date: String,
    val category: String,
    val source: String,
    val url: String
)

data class GalleryItem(
    val id: String,
    val title: String,
    val tag: String,
    val drawableRes: Int,
    val description: String
)
