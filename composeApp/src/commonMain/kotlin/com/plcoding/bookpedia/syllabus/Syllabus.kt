package com.plcoding.bookpedia.syllabus

data class Syllabus (
    val name: String,
)

data class TechniqueCategory(
    val name: String,
    val techniques: List<Technique>
)

data class Technique (
    val name: String,
    val description: String,
    val syllabus: Syllabus
)
