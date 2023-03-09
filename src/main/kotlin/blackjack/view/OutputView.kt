package blackjack.view

import blackjack.domain.BlackjackResult
import blackjack.domain.Card
import blackjack.domain.CardNumber
import blackjack.domain.CardResult
import blackjack.domain.Dealer
import blackjack.domain.MatchResult
import blackjack.domain.Participant
import blackjack.domain.Participants
import blackjack.domain.Player
import blackjack.domain.Suit

object OutputView {
    private const val SEPARATOR = ", "

    fun printFirstDrawnMessage(participants: Participants) {
        val dealer = participants.getDealer()
        val players = participants.getPlayers()
        println("${dealer.name}와 ${players.joinToString(SEPARATOR) { it.name }}에게 2장의 카드를 나누었습니다.")
    }

    fun printFirstOpenCards(participant: Participant) {
        printCards(participant.name, participant.getFirstOpenCards())
    }

    private fun printCards(name: String, cards: List<Card>) {
        println("$name 카드: ${cards.joinToString(SEPARATOR) { it.toText() }}")
    }

    fun printAllCards(participant: Participant) {
        when (participant) {
            is Dealer -> println("딜러는 16이하라 한장의 카드를 더 받았습니다.")
            is Player -> println(
                "${participant.name} 카드: ${participant.getCards().joinToString(SEPARATOR) { it.toText() }}"
            )
        }
    }

    fun printBlackjackResult(blackJackResult: BlackjackResult) {
        printCardResults(blackJackResult.cardResults)
        printFinalResult(blackJackResult.matchResults)
    }

    private fun printCardResults(cardResults: List<CardResult>) {
        printInterval()
        cardResults.forEach(::printScore)
    }

    private fun printScore(cardResult: CardResult) {
        with(cardResult) {
            println("${participant.name} 카드: ${cards.joinToString(SEPARATOR) { it.toText() }} - 결과: $scoreSum")
        }
    }

    private fun printFinalResult(matchResults: List<MatchResult>) {
        printInterval()
        println("## 최종 승패")
        printMatchResults(matchResults)
    }

    private fun printMatchResults(matchResults: List<MatchResult>) {
        matchResults.forEach(::printResult)
    }

    private fun printResult(matchResult: MatchResult) {
        if (matchResult.participant is Dealer) printDealerResult(matchResult)
        if (matchResult.participant is Player) printPlayerResult(matchResult)
    }

    private fun printDealerResult(result: MatchResult) {
        println("딜러: ${result.winCount}승 ${result.drawCount}무 ${result.loseCount}패")
    }

    private fun printPlayerResult(matchResult: MatchResult) {
        val playerName = matchResult.participant.name
        if (matchResult.winCount > 0) println("$playerName: 승")
        if (matchResult.drawCount > 0) println("$playerName: 무")
        if (matchResult.loseCount > 0) println("$playerName: 패")
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
