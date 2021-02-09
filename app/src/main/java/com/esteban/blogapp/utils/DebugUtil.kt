package com.esteban.blogapp.utils

import android.util.Log

object DebugUtil {
    private var charLimit = 2000
    @JvmStatic
    fun logResponse(tag: String?, message: String): Int {
        // If the message is less than the limit just show
        if (message.length < charLimit) {
            return Log.v(tag, message)
        }
        val sections = message.length / charLimit
        for (i in 0..sections) {
            val max = charLimit * (i + 1)
            if (max >= message.length) {
                Log.v(tag, message.substring(charLimit * i))
            } else {
                Log.v(tag, message.substring(charLimit * i, max))
            }
        }
        return 1
    }
}