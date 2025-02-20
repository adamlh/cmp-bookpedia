package com.plcoding.bookpedia.syllabus

data class Syllabus (
    val name: String,
)



data class Technique (
    val name: String,
    val description: String,
    val syllabus: Syllabus
)
