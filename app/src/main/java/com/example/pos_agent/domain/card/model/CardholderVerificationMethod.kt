package com.example.pos_agent.domain.card.model

/** Cardholder verification method used for the capture. */
enum class CardholderVerificationMethod {
    ONLINE_PIN,
    OFFLINE_PIN,
    UNKNOWN,
}
