package com.waigel.healthcardconnect.events


class CardRemovedEvent() : CommunicationEvent {
    override val eventType: String
        get() = "card-removed"
}