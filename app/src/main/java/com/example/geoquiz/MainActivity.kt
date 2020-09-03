package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

//used in logging messages
private const val TAG = "MainActivity"

/**
 * A constant that will be the key for the key-value pair that will be stored in the bundle.  Used
 * to save UI state data and use it to reconstruct the activity so that the user never
 * even knows the activity was destroyed (by storing data in a saved instance state).
 */
private const val KEY_INDEX = "index"

//used to determine if a user has answered a particular question yet
private const val KEY_ANSWERED = "answered"

//used to determine whether the user chose to view the answer
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView


    /**
     * A ViewModel is related to one particular screen and is a great place to put logic
     * involved in formatting the data to display on that screen.  A ViewModel holds on to a
     * model object and "decorates" the model - adding functionality to display onscreen that
     * you might not want in the model itself.  Using a ViewModel aggregates all the data the
     * screen needs in one place, formats the data, and makes it easy to access the end result.
     *
     * Using by lazy allows you to make the quizViewModel property a val instead of a var.  This
     * also means the quizViewModel calculation and assignment will not happen until the first time
     * you access quizViewModel.
     *
     * The ViewModelProviders class provides instances of the ViewModelProvider class.  Your
     * call to ViewModelProviders.of(this) creates and returns a ViewModelProvider associated
     * with the activity.
     *
     * Calling provider.get(QuizViewModel::class.java) returns an instance of
     * QuizViewModel.
     */
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    /**
     * Create a boolean array to keep track of questions that the user has answered.  At creation,
     * the user has answered no questions, so initialize the array to contain false at each index.
     */
    //private var questionsAnswered = BooleanArray(quizViewModel.getQuestionBank().size) //{false}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        //The '?:' operator takes the right-hand value if the left-hand value is null
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val questionsAnswered = savedInstanceState?.getBooleanArray(KEY_ANSWERED) ?:
                BooleanArray(quizViewModel.questionsAnswered.size)
        quizViewModel.currentIndex = currentIndex
        quizViewModel.questionsAnswered = questionsAnswered

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            //created mod function because Math.floorMod requires minimum API level of 24
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        /**
         * You are using an intent to tell the ActivityManager which activity to start, so you
         * will use this constructor: Intent(packageContext: Context, class: Class<?>).
         *
         * The Class argument you pass to the Intent constructor specifies the activity class
         * that the ActivityManager should start.  The Context argument tells the
         * ActivityManager which application package the activity class can be found in.
         */
        cheatButton.setOnClickListener {
            //start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    /**
     * One way to save UI state data and use it to reconstruct the activity so that the user never
     * knows the activity was destroyed is to store data in saved instance state.  Saved instance
     * state is data the OS temporarily stores outside of the activity.  You can add values to
     * saved instance state by overriding Activity.onSaveInstanceState(Bundle).
     *
     * A bundle is a structure that maps string keys to values of certain limited types.  We save
     * the currentIndex value across process death by overriding onSaveInstanceState(Bundle) to save
     * additional data to the bundle, which can then be read back in onCreate(Bundle?).
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putBooleanArray(KEY_ANSWERED, quizViewModel.questionsAnswered)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        if(!quizViewModel.questionsAnswered[quizViewModel.currentIndex]) {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        } else {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        quizViewModel.questionsAnswered[quizViewModel.currentIndex] = true
        trueButton.isEnabled = false
        falseButton.isEnabled = false

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

}
