package com.waigel.healthcardconnect.controllers

import com.google.common.collect.Sets
import com.waigel.healthcardconnect.events.CommunicationEvent
import com.waigel.healthcardconnect.events.WelcomeConnectorEvent
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger


@Component
class EventHandler {

    private val registeredClients: MutableSet<WebClient> = Sets.newConcurrentHashSet();
    private val logger = LoggerFactory.getLogger(EventHandler::class.java)

    fun registerClient(): SseEmitter {
        val emitter = SseEmitter(DEFAULT_TIMEOUT)
        val client = WebClient(emitter)
        emitter.onCompletion { registeredClients.remove(client) }
        emitter.onError { removeAndLogError(client) }
        emitter.onTimeout { removeAndLogError(client) }
        registeredClients.add(client)
        sendWelcomeToClient(client)
        logger.info("New client registered {}", client.id)
        return emitter
    }

    fun sendEventToAllClients(event: CommunicationEvent) {
        logger.info("Sending event to all clients: {}", registeredClients)
        registeredClients.forEach { client -> sendMessage(client, event) }
    }

    private fun removeAndLogError(client: WebClient) {
        logger.info("Error during communication. Unregister client {}", client.id)
        registeredClients.removeIf(client::equals)
    }

    private fun sendWelcomeToClient(client: WebClient) {
        val welcomeClientEvent = WelcomeConnectorEvent(client.id)
        sendMessage(client, welcomeClientEvent)
    }

    private fun sendMessage(client: WebClient, event: CommunicationEvent) {
        val sseEmitter: SseEmitter = client.sseEmitter()
        try {
            logger.info("Notify client {}", client.id)
            val eventId: Int = ID_COUNTER.incrementAndGet()
            val eventBuilder: SseEmitter.SseEventBuilder = SseEmitter.event()
                .id(eventId.toString())
                .data(event, MediaType.APPLICATION_JSON)
            sseEmitter.send(eventBuilder)
        } catch (e: IOException) {
            sseEmitter.completeWithError(e)

        }
    }

    companion object {
        private val ID_COUNTER: AtomicInteger = AtomicInteger(1)
        const val DEFAULT_TIMEOUT = Long.MAX_VALUE
    }
}