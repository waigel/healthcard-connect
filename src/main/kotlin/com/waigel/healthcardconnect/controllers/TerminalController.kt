package com.waigel.healthcardconnect.controllers

import com.waigel.healthcardconnect.exceptions.CardTerminalDriverNotFoundException
import com.waigel.healthcardconnect.models.TerminalResponseDTO
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import javax.smartcardio.TerminalFactory


@RestController
@RequestMapping("/v1/api")
class TerminalController() {

    private val logger = LoggerFactory.getLogger(TerminalController::class.java)

    @GetMapping("/terminals")
    fun getListOfTerminals(): List<TerminalResponseDTO> {
        logger.info("Getting list of terminals from system")
        try {
            val terminalFactory = TerminalFactory.getInstance("PC/SC", null);
            logger.info("Found terminals: ${terminalFactory.terminals().list()}")
            return terminalFactory.terminals().list().map { TerminalResponseDTO(it.name) }
        } catch (e: IOException) {
            logger.error("Error getting list of terminals from system", e)
            throw CardTerminalDriverNotFoundException()
        }
    }
}