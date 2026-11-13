package com.example.pos_agent.domain.card.model

/** How the card account data reached the terminal. */
enum class CardEntryMode {
    CONTACT,
    CONTACTLESS,
    MAGSTRIPE,
    FALLBACK_MAGSTRIPE,
}
