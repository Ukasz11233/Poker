import gameplay.Card;
import gameplay.Deck;
import gameplay.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestPlayer{

    private Player testedPlayer;
    @Before
    public void createTestedPlayer() {
        testedPlayer = new Player();
    }

    @Test
    public void testPlayerConstructor() {
        assertNotNull(testedPlayer);
    }

    @Test
    public void tesPlayerToEmptyString() {
        assertEquals("", testedPlayer.toString());
    }

    @Test
    public void testPlayerToString() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.TWO));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.FOUR));
        cards.add(new Card(Card.suit.CLUB, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        StringBuilder expect = new StringBuilder();
        int idx = 1;
        for (Card card : cards) {
            expect.append("\n").append(idx++).append(".").append(card);
        }
        testedPlayer.getFiveCards(cards);

        assertEquals(expect.toString(), testedPlayer.toString());
    }


    @Test
    public void testPlayerCountResultHighCard() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.TWO));
        cards.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cards.add(new Card(Card.suit.HEART, Card.rank.FOUR));
        cards.add(new Card(Card.suit.CLUB, Card.rank.FIVE));
        cards.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        testedPlayer.getFiveCards(cards);

        testedPlayer.countResult();

        int expectedResult = (int) (Math.pow(2, 0)*Math.pow(2,5));
        expectedResult += (int) (6*Math.pow(2, 4)); //SEVEN
        expectedResult += (int) (4*Math.pow(2, 3)); //FIVE
        expectedResult += (int) (3*Math.pow(2, 2)); //FOUR
        expectedResult += (int) (2*Math.pow(2, 1)); //THREE
        expectedResult += (int) (1*Math.pow(2, 0)); //TWO
        assertEquals(expectedResult, testedPlayer.getResult());
    }

    @Test
    public void testPlayerCountResultFlush() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.FIVE));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.SEVEN));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.EIGHT));
        cards.add(new Card(Card.suit.DIAMOND, Card.rank.KING));

        testedPlayer.getFiveCards(cards);

        testedPlayer.countResult();

        int expectedResult = (int) (Math.pow(2, 5)*Math.pow(2,5));  // flush ordinal == 5
        expectedResult += (int) (12*Math.pow(2, 4)); //KING
        expectedResult += (int) (7*Math.pow(2, 3)); //EIGHT
        expectedResult += (int) (6*Math.pow(2, 2)); //SEVEN
        expectedResult += (int) (4*Math.pow(2, 1)); //FIVE
        expectedResult += (int) (2*Math.pow(2, 0)); //THREE
        assertEquals(expectedResult, testedPlayer.getResult());
    }


    @Test
    public void testPutAsideAllCards() {
        Deck deck = new Deck();
        testedPlayer.getFiveCards(deck);
        testedPlayer.putAsideAllCards();
        assertEquals("", testedPlayer.toString());
    }

}
