/*
 * Copyright (c) 2017 - 2021  Byte Sculptor Software  - All Rights Reserved
 *
 * All information contained herein is and remains the property of Byte Sculptor Software.
 * Unauthorized copying of this file, via any medium, is strictly prohibited unless prior
 * written permission is obtained from Byte Sculptor Software.
 *
 * Romeo Rondinelli - bytesculptor@gmail.com
 *
 */

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