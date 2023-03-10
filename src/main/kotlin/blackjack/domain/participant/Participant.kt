package blackjack.domain.participant

import blackjack.domain.card.Card
import blackjack.domain.card.Cards
import blackjack.domain.result.GameResult

abstract class Participant(val name: String) {
    private val cards = Cards()

    abstract fun getFirstOpenCards(): List<Card>

    abstract fun canDraw(): Boolean

    fun getTotalScore(): Int = cards.calculateTotalScore()

    fun isBust(): Boolean = cards.isOverBlackjack()

    fun isStay(): Boolean = cards.isStay()

    infix fun judge(other: Participant): GameResult {
        val myScore = getTotalScore()
        val otherScore = other.getTotalScore()

        return when {
            isBust() && other.isBust() -> GameResult.DRAW
            isBust() -> GameResult.LOSE
            other.isBust() || (myScore > otherScore) -> GameResult.WIN
            myScore == otherScore -> GameResult.DRAW
            else -> GameResult.LOSE
        }
    }

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun getCards(): List<Card> = cards.items

    fun getFirstCard(): Card = cards.getFirstCard()
}
