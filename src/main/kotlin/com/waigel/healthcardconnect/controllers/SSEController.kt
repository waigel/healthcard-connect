package com.waigel.healthcardconnect.controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class SSEController(
    private val eventHandler: EventHandler
) {
    private val logger: Logger = LoggerFactory.getLogger(SSEController::class.java)

    @GetMapping("/register-client")
    @CrossOrigin
    fun sseEmitter(): SseEmitter {
        return eventHandler.registerClient()
    }

}