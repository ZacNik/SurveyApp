package com.example.surveyapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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
    private lateinit var databaseHelper: SurveyDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = SurveyDatabaseHelper(this)

        databaseHelper.writableDatabase

        setContent {
            SurveyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Display the survey screen
                    val surveyQuestions = databaseHelper.getSurveyQuestions()

                    if(surveyQuestions.isEmpty()){
                        databaseHelper.insertSurveyQuestion(
                            "How satisfied are you with your job?",
                            "Very Satisfied, Satisfied, Neutral, Unsatisfied, Very Unsatisfied"
                        )
                        databaseHelper.insertSurveyQuestion(
                            "How do you rate your current work environment?",
                            "Excellent, Good, Fair, Poor, Abysmal"
                        )
                    }
                    SurveyScreen(
                        questions = surveyQuestions,
                        onSubmit = {responses ->
                                println("Survey submitted with responses: $responses")
                        }
                    )
                }
            }
        }
    }
    class SurveyDatabaseHelper(context: Context) : SQLiteOpenHelper(context,
        DATABASE_NAME, null, DATABASE_VERSION){
        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "SurveyDatabase.db"
            private const val TABLE_SURVEY_QUESTIONS = "survey_questions"
            private const val COLUMN_ID = "id"
            private const val COLUMN_QUESTION_TEXT = "question_text"
            private const val COLUMN_OPTIONS = "options"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            val createTableQuery = "CREATE TABLE $TABLE_SURVEY_QUESTIONS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_QUESTION_TEXT TEXT," +
                    "$COLUMN_OPTIONS TEXT" +
                    ")"

            db?.execSQL(createTableQuery)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            // This method is called when the database version is upgraded.
            // You can handle any migration or schema changes here.
        }
        fun insertSurveyQuestion(questionText: String, options: String){
            val db = writableDatabase
            val contentValues = ContentValues().apply{
                put(COLUMN_QUESTION_TEXT, questionText)
                put(COLUMN_OPTIONS, options)
            }
            db.insert(TABLE_SURVEY_QUESTIONS, null, contentValues)
            db.close()
        }
        fun getSurveyQuestions(): List<SurveyQuestion>{
            val surveyQuestions = mutableListOf<SurveyQuestion>()
            val db = readableDatabase
            val cursor = db.query(TABLE_SURVEY_QUESTIONS, null, null, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val questionText = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_TEXT))
                    val options = cursor.getString(cursor.getColumnIndex(COLUMN_OPTIONS))
                    val surveyQuestion = SurveyQuestion(questionText, options.split(","))
                    surveyQuestions.add(surveyQuestion)
                } while (cursor.moveToNext())
                cursor.close()
            }
            db.close()
            return surveyQuestions
        }
    }
}
