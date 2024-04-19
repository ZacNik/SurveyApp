package com.example.surveyapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.surveyapp.data.SurveyQuestion

@Composable
fun SurveyScreen(
    questions: List<SurveyQuestion>,
    onSubmit: (List<SurveyQuestion>) -> Unit,
    modifier: Modifier = Modifier
) {
    var responses by remember { mutableStateOf(questions) }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(responses.size) { index ->
            val question = responses[index]
            SurveyQuestionItem(
                question = question,
                onOptionSelected = { option ->
                    responses = responses.toMutableList().apply {
                        this[index] = question.copy(selectedOption = option)
                    }
                }
            )
        }

        item {
            Button(
                onClick = { onSubmit(responses) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Submit Survey")
            }
        }
    }
}

@Composable
fun SurveyQuestionItem(
    question: SurveyQuestion,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = question.questionText)

        question.options.forEach { option ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                RadioButton(
                    selected = option == question.selectedOption,
                    onClick = { onOptionSelected(option) }
                )
                Text(text = option, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
