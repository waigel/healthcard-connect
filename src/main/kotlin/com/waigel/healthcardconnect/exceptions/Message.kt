package com.waigel.healthcardconnect.exceptions

import java.util.Locale

enum class Message {
    TERMINAL_NOT_FOUND;

    val code: String
        get() = name.uppercase(Locale.getDefault())
}
