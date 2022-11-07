package com.waigel.healthcardconnect.events

import com.waigel.healthcard.data.patient.PatientData


class CardReadSuccessfullyEvent(
    val patientData: PatientData?,
) :
    CommunicationEvent {
    override val eventType: String
        get() = "card-read-successfully"
}