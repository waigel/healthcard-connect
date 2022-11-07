package com.waigel.healthcardconnect.exceptions

class CardTerminalDriverNotFoundException : Exception() {
    override val message: String
        get() = "Card terminal driver not found"
}