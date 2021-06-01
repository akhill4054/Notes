package com.internshala.notes.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.internshala.notes.R

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

            transitionBuilder(fm)
                .replace(containerView.id, fragment, fragment.javaClass.simpleName)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        private fun transitionBuilder(fm: FragmentManager): FragmentTransaction {
            return fm.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
        }
    }
}