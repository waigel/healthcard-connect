package com.waigel.healthcardconnect.events


class WelcomeConnectorEvent(id: String) : CommunicationEvent {
    override val eventType: String
        get() = "connected"
}