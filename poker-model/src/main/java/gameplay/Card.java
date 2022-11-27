package gameplay;

import java.util.Objects;

public class Card {


    public enum suit{ HEART, SPADE, CLUB, DIAMOND}


    public enum rank{TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;}
    private suit cardSuit;
    private rank cardRank;
    public static int compare(Card cardA, Card cardB) {
        if (cardA.cardRank.ordinal() == cardB.cardRank.ordinal()) {
            return 0;
        } else if (cardA.cardRank.ordinal() > cardB.cardRank.ordinal()) {
            return 1;
        } else {
            return -1;
        }
    }

    public Card(suit tempSuit, rank tempRank)
    {
        cardSuit = tempSuit;
        cardRank = tempRank;
    }

    public int cardValue() {
        return cardRank.ordinal() * cardSuit.ordinal();
    }
    public Card() {}

    public void setSuit(suit tempSuit) {
        cardSuit = tempSuit;
    }

    public void setRank(rank tempRank) {
        cardRank = tempRank;
    }

    public suit getSuit() {
        return cardSuit;
    }

    public rank getRank() {
        return cardRank;
    }

    public String toString() {
        return "Suit: " + cardSuit + " Rank: " + cardRank;
    }


}

