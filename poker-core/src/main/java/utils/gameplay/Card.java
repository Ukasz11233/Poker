package utils.gameplay;

import java.util.Objects;

public class Card {


    public enum suit{ HEART, SPADE, CLUB, DIAMOND}
    public enum rank{ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE}

    public static int compare(Card cardA, Card cardB) {
        if (cardA._rank.ordinal() == cardB._rank.ordinal()) {
            return 0;
        } else if (cardA._rank.ordinal() > cardB._rank.ordinal()) {
            return 1;
        } else {
            return -1;
        }
    }
    public Card(suit tempSuit, rank tempRank)
    {
        _suit = tempSuit;
        _rank = tempRank;
    }

    public Card() {}
    private suit _suit;
    private rank _rank;

    public void setSuit(suit tempSuit) {
        _suit = tempSuit;
    }

    public void setRank(rank tempRank) {
        _rank = tempRank;
    }

    public suit getSuit() {
        return _suit;
    }

    public rank getRank() {
        return _rank;
    }

    public String toString() {
        return "Suit: " + _suit + " Rank: " + _rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return _suit == card._suit && _rank == card._rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_suit, _rank);
    }
}

