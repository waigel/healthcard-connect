package com.waigel.healthcardconnect.events


class CardReadErrorEvent() : CommunicationEvent {
    override val eventType: String
        get() = "card-read-error"
}