package com.example.surveyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.surveyapp.ui.SurveyScreen
import com.example.surveyapp.data.SurveyQuestion
import com.example.surveyapp.ui.theme.SurveyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurveyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Define the survey questions
                    val surveyQuestions = listOf(
                        SurveyQuestion(
                            "How satisfied are you with your job?",
                            listOf("Very Satisfied", "Satisfied", "Neutral", "Unsatisfied", "Very Unsatisfied")
                        ),
                        SurveyQuestion(
                            "How do you rate the work environment?",
                            listOf("Excellent", "Good", "Fair", "Poor")
                        )
                    )

                    // Display the survey screen
                    SurveyScreen(
                        questions = surveyQuestions,
                        onSubmit = { responses ->
                            // Handle survey submission here
                            // For example, log responses, save them locally, or send them to a server
                            println("Survey submitted with responses: $responses")
                        }
                    )
                }
            }
        }
    }
}
