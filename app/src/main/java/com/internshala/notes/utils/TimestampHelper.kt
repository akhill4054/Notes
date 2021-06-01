package com.internshala.notes.utils

import java.util.*

class TimestampHelper {
    companion object {
        @JvmStatic
        fun toDateAndMonthFormat(date: Date?): String {
            return if (date == null) {
                ""
            } else {
                val format = java.text.SimpleDateFormat("dd MMM yyyy", Locale.US)
                format.format(date)
            }
        }

        @JvmStatic
        fun toTimeDateAnyMonthFormat(date: Date?): String {
            return if (date == null) {
                ""
            } else {
                val format = java.text.SimpleDateFormat("hh a, dd MMM yyyy", Locale.US)
                format.format(date)
            }
        }
    }
}