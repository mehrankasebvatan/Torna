package ir.kasebvatan.torna.data

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object Util {

    fun hideKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val activity = context as? Activity
        activity?.currentFocus?.let { view ->
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}