package gameplay;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Deck {
    public static void main(String... args) {
        Deck testDeck = new Deck();
        testDeck.shuffleDeck();
        //testDeck.printDeck();
        testDeck.sortDeck();
        testDeck.printDeck();
    }
    private ArrayList<Card> cardDeck = new ArrayList<Card>(52);

    public Deck()
    {
        for ( Card.rank rankToCreate : Card.rank.values())
        {
            for (Card.suit suitToCreate : Card.suit.values())
            {
                cardDeck.add(new Card(suitToCreate, rankToCreate));
            }
        }
    }

    public void printDeck() {
        cardDeck.forEach(System.out::println);
    }

    public void sortDeck() {
        // dwa wyrazenia lambda sortujace najpierw po kolorze, a pozniej po numerze
        Comparator<Card> comp = (cardA, cardB) -> cardA.getRank().compareTo(cardB.getRank());
        comp = comp.thenComparing((cardA, cardB) -> cardA.getSuit().compareTo(cardB.getSuit()));

        cardDeck.sort(comp);
    }

    public void shuffleDeck() {
        Collections.shuffle(cardDeck);
    }

    public Card getCardFromDeck() {
        return cardDeck.remove(0);
    }
}

class CardRankComparator implements Comparator<Card> {
    public int compare(Card cardA, Card cardB) {
        return Card.compare(cardA, cardB);
    }
}