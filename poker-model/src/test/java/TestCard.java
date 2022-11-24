import gameplay.Card;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCard {

    @Test
    public void TestCardConstructor() {
        Card cardClubAce = new Card(Card.suit.CLUB, Card.rank.ACE);

        assertEquals(Card.suit.valueOf("CLUB"), cardClubAce.getSuit());
        assertEquals(Card.rank.valueOf("ACE"), cardClubAce.getRank());
    }

    @Test
    public void TestEqualCardsWithSameRanks()
    {
        Card cardHeartTwoFirst = new Card(Card.suit.HEART, Card.rank.TWO);
        Card cardHeartTwoSecond = new Card(Card.suit.HEART, Card.rank.TWO);

        assertEquals(0, Card.compare(cardHeartTwoFirst, cardHeartTwoSecond));
    }
    @Test
    public void TestEqualCardsWithDifferentRanks() {
        Card cardHeartQueen = new Card(Card.suit.HEART, Card.rank.QUEEN);
        Card cardDiamondQueen = new Card(Card.suit.DIAMOND, Card.rank.QUEEN);

        assertEquals(0, Card.compare(cardHeartQueen, cardDiamondQueen));
    }

    @Test
    public void TestNotEqualCardsWithSameRanks() {
        Card cardSpadeKing = new Card(Card.suit.SPADE, Card.rank.KING);
        Card cardSpadeAce = new Card(Card.suit.DIAMOND, Card.rank.ACE);

        assertEquals(1, Card.compare(cardSpadeAce, cardSpadeKing));
        assertEquals(-1, Card.compare(cardSpadeKing, cardSpadeAce));
    }

    @Test
    public void TestNotEqualCardsWithDifferentRanks() {
        Card cardSpadeThree = new Card(Card.suit.SPADE, Card.rank.THREE);
        Card cardDiamonJack = new Card(Card.suit.DIAMOND, Card.rank.JACK);

        assertEquals(1, Card.compare(cardDiamonJack, cardSpadeThree));
        assertEquals(-1, Card.compare(cardSpadeThree, cardDiamonJack));
    }


}


