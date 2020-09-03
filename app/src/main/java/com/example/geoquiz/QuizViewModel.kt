package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var isCheater = false

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_continental_shift, true),
        Question(R.string.question_mt_thor, true),
        Question(R.string.question_north_hemisphere, false),
        Question(R.string.question_california, false),
        Question(R.string.question_dead_sea, true)
    )

    var questionsAnswered = BooleanArray(questionBank.size)
    var userAnswers = BooleanArray(questionBank.size)

    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer

    val currentQuestionText: Int get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = mod(currentIndex + 1, questionBank.size)
    }

    fun moveToPrev() {
        currentIndex = mod(currentIndex - 1, questionBank.size)
    }

    private fun mod(x: Int, y: Int): Int {
        val result = x % y

        return if (result < 0) {
            result + y
        } else {
            result
        }
    }

}