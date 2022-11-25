package gameplay;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Deck {

    private ArrayList<Card> cardDeck = new ArrayList<Card>(52);

    public enum rules{HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH};

    public Deck()
    {
       createDeck();
    }

    public void printDeck() {
        cardDeck.forEach(System.out::println);
    }

    public void sortDeck() {
        // dwa wyrazenia lambda sortujace najpierw po kolorze, a pozniej po numerze
        Comparator<Card> comp = Comparator.comparing(Card::getRank);
        comp = comp.thenComparing(Comparator.comparing(Card::getSuit));

        cardDeck.sort(comp);
    }

    public void shuffleDeck() {
        Collections.shuffle(cardDeck);
    }

    public Card getCardFromDeck() {
        return cardDeck.remove(0);
    }

    public void resetDeck() {
        cardDeck.removeAll(cardDeck);
        cardDeck = new ArrayList<>(52);
        createDeck();
    }

    private void createDeck() {
        for ( Card.rank rankToCreate : Card.rank.values())
        {
            for (Card.suit suitToCreate : Card.suit.values())
            {
                cardDeck.add(new Card(suitToCreate, rankToCreate));
            }
        }
    }

}


class CardRankComparator implements Comparator<Card> {
    public int compare(Card cardA, Card cardB) {
        return Card.compare(cardA, cardB);
    }
}