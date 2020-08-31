package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

//a constant for the extra's key
const val EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        //retrieve the value from the extra in onCreate(Bundle?) and store it in a member variable
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            /**
             * when replaces the switch operator of C-like languages.  when matches its argument
             * against all branches sequentially until some branch condition is satisfied.  The else
             * branch is evaluated if none of the other branch conditions are satisfied. If when
             * is used as an expression, the else branch is mandatory.
             */
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    /**
     * function to pass specific data back to MainActivity.  We create an Intent, put an extra on it,
     * and then call Activity.setResult(Int, Intent) to get that data into MainActivity's hands.
     */
    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    /**
     * A companion object allows you to access functions without having an instance of a class,
     * similar to static functions in Java.  Using a newIntent(...) function inside a companion
     * object like this for your activity subclasses will make it easy for other code to properly
     * configure their launching intents.
     */
    companion object {
        /**
         * This function allows you to create an Intent properly configured with the extras
         * CheatActivity will need.  The answerIsTrue argument, a Boolean, is put into the intent
         * with a private name using the EXTRA_ANSWER_IS_TRUE constant.
         */
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}