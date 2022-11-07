package com.waigel.healthcardconnect

import com.waigel.healthcard.HealthCardReader
import com.waigel.healthcardconnect.controllers.EventHandler
import com.waigel.healthcardconnect.events.CardInsertedEvent
import com.waigel.healthcardconnect.events.CardReadErrorEvent
import com.waigel.healthcardconnect.events.CardReadSuccessfullyEvent
import com.waigel.healthcardconnect.events.CardRemovedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.smartcardio.CardException
import javax.smartcardio.CardTerminals
import javax.smartcardio.TerminalFactory

@Component
class HealthcardReaderComponent(
    private val eventHandler: EventHandler
) {

    private val logger = LoggerFactory.getLogger(HealthcardReaderComponent::class.java)


    /**
     * We wait for terminal changes, if an exception occurs for example if no
     * terminal is connected, we just wait for 7 seconds and try again
     */
    private fun waitForChangesOrSleep(terminals: CardTerminals) {
        try {
            terminals.waitForChange(0)
        } catch (e: CardException) {
            //sleep for 7 second
            Thread.sleep(1000 * 7)
        }
    }

    private fun trackCardTerminals() {
        val factory = TerminalFactory.getInstance("PC/SC", null);
        val terminals: CardTerminals = factory.terminals()
        while (true) {
            try {
                for (terminal in terminals.list(CardTerminals.State.CARD_INSERTION)) {
                    // examine Card in terminal, return if it matches
                    logger.info("Card inserted in terminal: ${terminal.name}")
                    eventHandler.sendEventToAllClients(CardInsertedEvent())
                    try {
                        val channel = terminal.connect("T=1").basicChannel
                        val patient = HealthCardReader().readPatientData(channel)
                        eventHandler.sendEventToAllClients(
                            CardReadSuccessfullyEvent(
                                patient
                            )
                        )
                    } catch (e: Exception) {
                        logger.error("Error reading patient data", e)
                        eventHandler.sendEventToAllClients(CardReadErrorEvent())
                    }
                }
                for (terminal in terminals.list(CardTerminals.State.CARD_REMOVAL)) {
                    logger.info("Card removed from terminal: ${terminal.name}")
                    eventHandler.sendEventToAllClients(CardRemovedEvent())
                }
                this.waitForChangesOrSleep(terminals)
            } catch (e: CardException) {
                logger.error(
                    "Error getting list of terminals from system"
                )
                Thread.sleep(7000)
            }

        }
    }
    
    @Bean
    fun startHealthcardReaderFlow() {
        logger.info("Starting healthcard reader flow")
        Thread { trackCardTerminals() }.start()
    }
}