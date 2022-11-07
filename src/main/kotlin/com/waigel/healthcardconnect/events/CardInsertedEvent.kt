package com.waigel.healthcardconnect.events


class CardInsertedEvent() : CommunicationEvent {
    override val eventType: String
        get() = "card-inserted"
}