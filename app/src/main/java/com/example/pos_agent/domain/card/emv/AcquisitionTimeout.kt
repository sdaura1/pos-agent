package com.example.pos_agent.domain.card.emv

/** Which deadline expired during acquisition. */
enum class AcquisitionTimeout {
    /** No card was presented within the session timeout. */
    CARD_WAIT,

    /** The PIN pad deadline elapsed. */
    PIN,

    /** The card was not presented again within the second-tap timeout. */
    SECOND_TAP,
}
