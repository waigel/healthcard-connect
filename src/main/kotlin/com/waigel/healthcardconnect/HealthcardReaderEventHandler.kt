package com.waigel.healthcardconnect

import com.waigel.healthcard.HealthCardGeneration
import com.waigel.healthcard.HealthCardReaderEvents
import com.waigel.healthcard.data.insurance.InsuranceData
import com.waigel.healthcard.data.patient.PatientData
import com.waigel.healthcardconnect.controllers.EventHandler
import com.waigel.healthcardconnect.events.CardInsertedEvent
import com.waigel.healthcardconnect.events.CardReadErrorEvent
import com.waigel.healthcardconnect.events.CardReadSuccessfullyEvent
import com.waigel.healthcardconnect.events.CardRemovedEvent
import org.slf4j.LoggerFactory


open class HealthcardReaderEventHandler(
    private val eventHandler: EventHandler
) : HealthCardReaderEvents {
    private val logger = LoggerFactory.getLogger(HealthcardReaderEventHandler::class.java)
    override fun onCardInserted() {
        logger.info("Card inserted")
        eventHandler.sendEventToAllClients(CardInsertedEvent())
    }

    override fun onCardReadDataSuccessfully(
        patient: PatientData?,
        insurance: InsuranceData?,
        generation: HealthCardGeneration?
    ) {
        logger.info("Card read successfully")
        eventHandler.sendEventToAllClients(
            CardReadSuccessfullyEvent(
                patient,
            )
        )
    }

    override fun onCardReadError(e: Exception) {
        logger.error("Error reading card", e)
        eventHandler.sendEventToAllClients(CardReadErrorEvent())
    }

    override fun onCardRemoved() {
        logger.info("Card removed")
        eventHandler.sendEventToAllClients(CardRemovedEvent())
    }

}