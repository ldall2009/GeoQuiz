package com.example.geoquiz

import androidx.annotation.StringRes

/** data keyword indicates that the class is meant to hold data, and the compiler
 *  does extra work for data classes such as automatically defining useful functions like
 *  equals(), hashCode(), and toString().
 *
 *  @StringRes annotation used to help the code inspector (Lint) verify that the
 *  usages of the constructor provide a valid string resource ID.  This prevents
 *  runtime crashes where the constructor is used with an invalid resource ID.
 *
 *  textResId will hold the resource ID (always an Int) of the string resource for a
 *  question.
 */
data class Question(@StringRes val textResId: Int, val answer: Boolean)