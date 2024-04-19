package com.example.surveyapp.data

data class SurveyQuestion(
    val questionText: String,
    val options: List<String>,
    var selectedOption: String? = null
)
