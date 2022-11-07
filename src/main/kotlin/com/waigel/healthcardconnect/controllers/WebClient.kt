package com.waigel.healthcardconnect.controllers


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.UUID


class WebClient(
    private val sseEmitter: SseEmitter
) {

    fun sseEmitter(): SseEmitter {
        return sseEmitter
    }

    val id: String = UUID.randomUUID().toString()

    fun sendEvent(event: String) {
        sseEmitter.send(event)
    }
}