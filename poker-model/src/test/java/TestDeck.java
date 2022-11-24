import gameplay.Deck;
import gameplay.Card;
import org.junit.Test;

import javax.lang.model.type.DeclaredType;

import static org.junit.Assert.*;

public class TestDeck {
    static int numOfCards = 52;
    static int numOfSuits = 4;
    static int numOfRanks = 13;

    @Test
    public void testDeckConstructor() {
        Deck newDeck = new Deck();

        for (int i = 0; i < numOfCards; ++i) {
            Card topCardFromDeck = newDeck.getCardFromDeck();
            assertEquals(Card.suit.values()[i%numOfSuits], topCardFromDeck.getSuit());
            assertEquals(Card.rank.values()[i/numOfSuits], topCardFromDeck.getRank());
        }
    }
    @Test
    public void testDeckShuffle() {
        Deck sortedDeck = new Deck();
        Deck shuffledDeck = new Deck();
        shuffledDeck.shuffleDeck();
        int numOfCardsNotEqual = 0;

        for (int i = 0; i < numOfCards; ++i) {
            Card sortedCard = sortedDeck.getCardFromDeck();
            Card shuffledCard = shuffledDeck.getCardFromDeck();
            if (sortedCard.getSuit() != shuffledCard.getSuit() || sortedCard.getRank() != shuffledCard.getRank()) {
                numOfCardsNotEqual++;
            }
        }

        assertTrue(numOfCardsNotEqual > 13);
    }

    @Test
    public void testSortDeck() {
        Deck sortedDeck = new Deck();
        Deck shuffledThenSortedDeck = new Deck();
        shuffledThenSortedDeck.shuffleDeck();
        shuffledThenSortedDeck.sortDeck();
        for (int i = 0; i < numOfCards; ++i) {
            Card cardFromSortedDeck = sortedDeck.getCardFromDeck();
            Card cardFromShuffledDeck = shuffledThenSortedDeck.getCardFromDeck();
            assertEquals(cardFromSortedDeck.getSuit(), cardFromShuffledDeck.getSuit());
            assertEquals(cardFromShuffledDeck.getRank(), cardFromShuffledDeck.getRank());
        }
    }


}
