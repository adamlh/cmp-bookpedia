package com.plcoding.bookpedia.book.domain

// Only want to use raw Kotlin code in domain layer
// No serialized references.
data class Book(
    val id: String,
    val title: String,
    val imageUrl: String,
    val authors: List<String>,
    val description: String,
    val languages: List<String>,
    val firstPublisherYear: String?,
    val averageRating: Double?,
    val ratingCount: Int?,
    val numPages: Int?,
    val numEditions: Int
)