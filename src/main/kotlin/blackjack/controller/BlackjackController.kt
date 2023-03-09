package blackjack.controller

import blackjack.domain.Blackjack
import blackjack.domain.CardDeck
import blackjack.domain.Player
import blackjack.view.InputView
import blackjack.view.OutputView

class BlackjackController {
    fun start() {
        initBlackjack().start(
            onStartFirstDrawn = OutputView::printFirstDrawnMessage,
            onFirstDrawn = OutputView::printFirstOpenCards,
            onDrawnMore = OutputView::printAllCards,
            onEndGame = OutputView::printBlackjackResult
        )
    }

    private fun initBlackjack(): Blackjack = Blackjack(CardDeck(), enrollPlayers())

    private fun enrollPlayers(): List<Player> = InputView.inputNames().map { name ->
        Player(name, needToDraw = {
            InputView.inputDrawCommand(name)
        })
    }
}
