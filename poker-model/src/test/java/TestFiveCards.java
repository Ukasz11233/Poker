import gameplay.Card;
import gameplay.FiveCards;
import gameplay.Deck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class TestFiveCards {
    private FiveCards fiveCards;
    private Deck deck;
    private ArrayList<Card> cards;

    @Before
    public void createFiveCards() {
        deck = new Deck();
        deck.shuffleDeck();
        cards = new ArrayList<>(5);
    }

    @Test
    public void testRoyalFlushExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.TEN));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.JACK));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.QUEEN));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.KING));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.ACE));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.ROYAL_FLUSH));

    }

    @Test
    public void testRoyalFlushExpectFalse() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.NINE));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.JACK));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.QUEEN));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.KING));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.ACE));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.ROYAL_FLUSH));

    }

    @Test
    public void testStraightFlushExpectTrue() {
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.FOUR));
        cards.add(new Card(Card.suit.SPADE, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SIX));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.STRAIGHT_FLUSH));

    }

    @Test
    public void testStraightFlushExpectFalse() {
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.FOUR));
        cards.add(new Card(Card.suit.SPADE, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SIX));
        cards.add(new Card(Card.suit.SPADE, Card.rank.EIGHT));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.STRAIGHT_FLUSH));

    }
    @Test
    public void testFourOfAKindExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.THREE));
        cards.add(new Card(Card.suit.CLUB, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.FOUR_OF_A_KIND));
    }

    @Test
    public void testFourOfAKindExpectFalse() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.THREE));
        cards.add(new Card(Card.suit.CLUB, Card.rank.FOUR));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.FOUR_OF_A_KIND));
    }


    @Test
    public void testFullHouseExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.THREE));
        cards.add(new Card(Card.suit.CLUB, Card.rank.SEVEN));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.FULL_HOUSE));
    }

    @Test
    public void testFullHouseExpectFalse() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.THREE));
        cards.add(new Card(Card.suit.CLUB, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.FULL_HOUSE));
    }

    @Test
    public void testFlushExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.FIVE));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.SEVEN));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.EIGHT));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.KING));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.FLUSH));
    }

    @Test
    public void testFlushExpectFalse() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.FIVE));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.EIGHT));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.KING));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.FLUSH));
    }

    @Test
    public void testStraightExpectTrue() {
        cards.add(new Card(Card.suit.CLUB, Card.rank.FOUR));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SIX));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));
        cards.add(new Card(Card.suit.HEART, Card.rank.EIGHT));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.STRAIGT));
    }

    @Test
    public void testStraightExpectFalse() {
        cards.add(new Card(Card.suit.CLUB, Card.rank.FOUR));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SIX));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));
        cards.add(new Card(Card.suit.HEART, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.STRAIGT));
    }

    @Test
    public void testThreeOfAKindExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.THREE));
        cards.add(new Card(Card.suit.CLUB, Card.rank.SIX));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.THREE_OF_A_KIND));
    }

    @Test
    public void testThreeOfAKindExpectFalse() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.FOUR));
        cards.add(new Card(Card.suit.CLUB, Card.rank.FOUR));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.THREE_OF_A_KIND));
    }

    @Test
    public void testTwoPairExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.SIX));
        cards.add(new Card(Card.suit.CLUB, Card.rank.SIX));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.TWO_PAIR));
    }

    @Test
    public void testTwoPairExpectFalse() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.FOUR));
        cards.add(new Card(Card.suit.CLUB, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.TWO_PAIR));
    }

    @Test
    public void testOnePairExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.FIVE));
        cards.add(new Card(Card.suit.CLUB, Card.rank.SIX));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.ONE_PAIR));
    }

    @Test
    public void testOnePairExpectFalse() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.TWO));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.FOUR));
        cards.add(new Card(Card.suit.CLUB, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertFalse(fiveCards.checkRule(Deck.rules.ONE_PAIR));
    }

    @Test
    public void testHighCardExpectTrue() {
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.TWO));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.FOUR));
        cards.add(new Card(Card.suit.CLUB, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        fiveCards = new FiveCards(cards);
        assertTrue(fiveCards.checkRule(Deck.rules.HIGH_CARD));
    }

}
