package blackjack.view

import blackjack.domain.card.Card
import blackjack.domain.card.CardNumber
import blackjack.domain.card.Suit
import blackjack.domain.listener.BlackjackEventListener
import blackjack.domain.money.Money
import blackjack.domain.participant.Dealer
import blackjack.domain.participant.Participant
import blackjack.domain.participant.Participants
import blackjack.domain.participant.Player
import blackjack.domain.result.GameResult

object OutputView : BlackjackEventListener {
    private const val SEPARATOR = ", "

    override fun onStartDrawn(participants: Participants) {
        printFirstDrawnMessage(participants)
    }

    override fun onFirstDrawn(participant: Participant) {
        printFirstOpenCards(participant)
    }

    override fun onDrawnMore(participant: Participant) {
        printAllCards(participant)
    }

    override fun onEndGame(gameResults: List<GameResult>) {
        printBlackjackResult(gameResults)
    }

    private fun printFirstDrawnMessage(participants: Participants) {
        val dealer = participants.getDealer()
        val players = participants.getPlayers()
        println("${dealer?.name}와 ${players.joinToString(SEPARATOR) { it.name }}에게 2장의 카드를 나누었습니다.")
    }

    private fun printFirstOpenCards(participant: Participant) {
        printCards(participant.name, participant.getFirstOpenCards())
    }

    private fun printCards(name: String, cards: List<Card>) {
        println("$name 카드: ${cards.joinToString(SEPARATOR) { it.toText() }}")
    }

    private fun printAllCards(participant: Participant) {
        when (participant) {
            is Dealer -> println("딜러는 16이하라 한장의 카드를 더 받았습니다.")
            is Player -> println(
                "${participant.name} 카드: ${
                participant.getCards().joinToString(SEPARATOR) { it.toText() }
                }"
            )
        }
    }

    private fun printBlackjackResult(gameResults: List<GameResult>) {
        printCardResults(gameResults)
        printFinalResult(gameResults)
    }

    private fun printCardResults(gameResults: List<GameResult>) {
        printInterval()
        gameResults.forEach { printScore(it.participantName, it.cards, it.scoreSum) }
    }

    private fun printScore(name: String, cards: List<Card>, scoreSum: Int) {
        println("$name 카드: ${cards.joinToString(SEPARATOR) { it.toText() }} - 결과: $scoreSum")
    }

    private fun printFinalResult(gameResults: List<GameResult>) {
        printInterval()
        println("## 최종 수익")
        printMatchResults(gameResults)
    }

    private fun printMatchResults(gameResults: List<GameResult>) {
        gameResults.forEach { printParticipantResult(it.participantName, it.profit) }
    }

    private fun printParticipantResult(name: String, profit: Money) {
        println("$name: ${profit.getAmount()}")
    }

    private fun Card.toText(): String = "${number.toText()}${suit.toText()}"

    private fun CardNumber.toText(): String = when (this) {
        CardNumber.ACE -> "A"
        CardNumber.TWO -> "2"
        CardNumber.THREE -> "3"
        CardNumber.FOUR -> "4"
        CardNumber.FIVE -> "5"
        CardNumber.SIX -> "6"
        CardNumber.SEVEN -> "7"
        CardNumber.EIGHT -> "8"
        CardNumber.NINE -> "9"
        CardNumber.TEN -> "10"
        CardNumber.JACK -> "J"
        CardNumber.QUEEN -> "Q"
        CardNumber.KING -> "K"
    }

    private fun Suit.toText(): String = when (this) {
        Suit.SPADE -> "스페이드"
        Suit.HEART -> "하트"
        Suit.DIAMOND -> "다이아몬드"
        Suit.CLOVER -> "클로버"
    }

    private fun printInterval() = println()
}
