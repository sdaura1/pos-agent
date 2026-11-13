package com.example.pos_agent.domain.terminal.model

/**
 * Terminal characteristics needed to assemble the NIBSS POS data code (DE123)
 * and the POS entry mode (DE22).
 *
 * These are properties of the terminal and its configuration, not of a single
 * card capture.
 */
data class TerminalTraits(
    val pinEntryCapability: PinEntryCapability = PinEntryCapability.UNKNOWN,
    val maxPinLength: Int? = null,
    val operator: TerminalOperator = TerminalOperator.CARD_ACCEPTOR,
    val terminalTypeCode: String = "01",
    val attended: Boolean = true,
    val cardholderPresent: Boolean = true,
) {
    init {
        require(maxPinLength == null || maxPinLength in 4..12) {
            "Maximum PIN length must be between 4 and 12"
        }
        require(terminalTypeCode.length == 2 && terminalTypeCode.all(Char::isDigit)) {
            "Terminal type code must be two digits"
        }
    }
}
