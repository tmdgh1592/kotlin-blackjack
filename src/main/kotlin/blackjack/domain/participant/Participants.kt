package blackjack.domain.participant

import blackjack.domain.card.CardDeck
import blackjack.domain.money.Money
import blackjack.domain.result.GameResult

class Participants(private val participants: List<Participant>) {
    init {
        require(participants.size in MINIMUM_PARTICIPANTS..MAXIMUM_PARTICIPANTS) {
            "블랙잭은 딜러를 포함하여 최소 ${MINIMUM_PARTICIPANTS}명에서 최대 ${MAXIMUM_PARTICIPANTS}명의 플레이어가 참여 가능합니다. (현재 플레이어수 : ${participants.size}명)"
        }
    }

    fun drawFirst(
        deck: CardDeck,
        onFirstDrawn: (Participant) -> Unit = {},
    ): Participants {
        return Participants(
            dealerFirst()
                .map { participant -> participant.draw(deck.draw()).draw(deck.draw()) }
                .onEach(onFirstDrawn)
        )
    }

    fun takeTurns(deck: CardDeck, onDrawn: (Participant) -> Unit): Participants = Participants(
        playersFirst().map { participant -> drawUntilCanDraw(participant, deck, onDrawn) }
    )

    private fun drawUntilCanDraw(
        participant: Participant,
        deck: CardDeck,
        onDrawn: (Participant) -> Unit
    ): Participant {
        var drawnParticipant = participant
        if (!drawnParticipant.canDraw()) return drawnParticipant

        do {
            drawnParticipant = checkPlayerNeedToDraw(drawnParticipant)
            drawnParticipant = drawCard(drawnParticipant, deck, onDrawn)
        } while (drawnParticipant.canDraw())
        return drawnParticipant
    }

    private fun drawCard(
        participant: Participant,
        deck: CardDeck,
        onDrawn: (Participant) -> Unit
    ): Participant {
        var drawnParticipant = participant

        if (participant.canDraw()) {
            drawnParticipant = participant.draw(deck.draw())
            onDrawn(drawnParticipant)
        }
        return drawnParticipant
    }

    private fun checkPlayerNeedToDraw(drawnParticipant: Participant): Participant {
        if (drawnParticipant is Player) return playerNeedToDraw(drawnParticipant)
        return drawnParticipant
    }

    private fun playerNeedToDraw(player: Player): Participant {
        if (player.needToDraw()) return player
        return player.stay()
    }

    fun getGameResult(): List<GameResult> = dealerFirst().map { participant ->
        GameResult(
            participant.name,
            participant.getCards(),
            participant.getTotalScore(),
            getProfit(participant)
        )
    }

    private fun getProfit(participant: Participant): Money = when (participant) {
        is Player -> getPlayerMatchResults(participant)
        is Dealer -> getDealerMatchResult(participant)
        else -> throw IllegalArgumentException("올바르지 않은 참여자입니다.")
    }

    private fun getPlayerMatchResults(player: Player): Money = player.getProfit(getDealer())

    private fun getDealerMatchResult(dealer: Dealer): Money =
        Money(getPlayers().sumOf { dealer.getProfit(it).getAmount() })

    fun getPlayers(): List<Participant> = participants.filterIsInstance<Player>()

    fun getDealer(): Participant = participants.first { it is Dealer }

    private fun dealerFirst(): List<Participant> = listOf(getDealer()) + getPlayers()

    private fun playersFirst(): List<Participant> = getPlayers() + getDealer()

    companion object {
        private const val MINIMUM_PARTICIPANTS = 2
        private const val MAXIMUM_PARTICIPANTS = 8
    }
}
