package com.internshala.notes.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class FragmentTransitionHelper {
    companion object {
        fun replaceFragment(
            activity: FragmentActivity,
            containerView: View,
            fragment: Fragment
        ) {
            val fm = activity.supportFragmentManager

            fm.beginTransaction()
                .replace(containerView.id, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        }

        fun addToBackStack(
            activity: FragmentActivity,
            containerView: View,
            fragment: Fragment
        ) {
            val fm = activity.supportFragmentManager

            fm.beginTransaction()
                .replace(containerView.id, fragment, fragment.javaClass.simpleName)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }
}