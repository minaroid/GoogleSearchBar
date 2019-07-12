/*
 * Created by : Mina George ,  at :  12/7/2019.
 * Email : minageorge888@gmail.com
 */

package com.mina.googlesearchbar.utils

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT



object ViewUtils {

    fun showKeyboard(view: View) {
        val imm = (view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.showSoftInput(view, SHOW_IMPLICIT)
    }

    fun hideKeyboard(view: View) {
        val imm = (view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}