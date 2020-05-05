package com.bytesculptor.applib.utilities

import java.util.*

class Converters {

    companion object {

        @JvmStatic
        fun timestampToDate(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @JvmStatic
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }
    }
}