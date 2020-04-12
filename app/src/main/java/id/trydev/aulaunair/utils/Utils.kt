package id.trydev.aulaunair.utils

import android.view.View

object Utils {

    fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hideView(view: View) {
        view.visibility = View.GONE
    }
}