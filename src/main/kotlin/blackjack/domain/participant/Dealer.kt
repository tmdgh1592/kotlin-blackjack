package blackjack.domain.participant

import blackjack.domain.card.Card

class Dealer : Participant(DEALER_NAME) {
    override fun getFirstOpenCards(): List<Card> = listOf(getFirstCard())

    override fun canDraw(): Boolean = !isStay()

    companion object {
        private const val DEALER_NAME = "딜러"
    }
}
