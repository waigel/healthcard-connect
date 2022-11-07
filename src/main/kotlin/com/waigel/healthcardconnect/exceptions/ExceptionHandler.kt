package com.waigel.healthcardconnect.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(CardTerminalDriverNotFoundException::class)
    fun handleCardTerminalDriverNotFoundException(e: CardTerminalDriverNotFoundException): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ErrorResponseBody(Message.TERMINAL_NOT_FOUND.code, null), HttpStatus.NOT_FOUND)
    }



}