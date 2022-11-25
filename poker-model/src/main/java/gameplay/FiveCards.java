package gameplay;

import java.util.ArrayList;
import java.util.Comparator;

public class FiveCards extends Deck {
    public ArrayList<Card> getFiveCards() {
        return fiveCards;
    }

    private ArrayList<Card> fiveCards = new ArrayList<>(5);



    public FiveCards(ArrayList<Card> cards) {
        for (Card card : cards) {
            fiveCards.add(card);
        }
    }

    public void sortFiveCards() {
        // dwa wyrazenia lambda sortujace najpierw po kolorze, a pozniej po numerze
        Comparator<Card> comp = Comparator.comparing(Card::getRank);
        comp = comp.thenComparing(Comparator.comparing(Card::getSuit));

        fiveCards.sort(comp);
    }


    //pamietaj o posortowaniu kart przed wywoalniem tej metody
    public boolean checkRule(rules rule) {
        if(rule == rules.ROYAL_FLUSH)
            return checkRoyalFlush();
        if(rule == rules.STRAIGHT_FLUSH)
            return checkStraightFlush();
        if(rule == rules.FOUR_OF_A_KIND)
            return checkFourOfAKind();
        if(rule == rules.FULL_HOUSE)
            return checkFullHouse();
        if (rule == rules.FLUSH)
            return checkFlush();
        if(rule == rules.STRAIGT)
            return checkStraight();
        if(rule == rules.THREE_OF_A_KIND)
            return checkThreeOfAKind();
        if(rule == rules.TWO_PAIR)
            return checkTwoPair();
        if(rule == rules.ONE_PAIR)
            return checkOnePair();
        if(rule == rules.HIGH_CARD)
            return true;

        return false;
    }

    private boolean checkRoyalFlush() {
        Card.suit suit = fiveCards.get(0).getSuit();
        Card.rank highestRank = fiveCards.get(4).getRank();
        if (highestRank != Card.rank.ACE) {
            return false;
        }
        int rankDown = 1;
        for (int i = 3; i >=0 ; --i) {
            if (fiveCards.get(i).getSuit() != suit || (fiveCards.get(i).getRank().ordinal()) != highestRank.ordinal() - rankDown) {
                return false;
            }
            rankDown++;
        }
        return true;
    }

    private boolean checkStraightFlush() {
        Card.suit suit = fiveCards.get(0).getSuit();
        Card.rank highestRank = fiveCards.get(4).getRank();
        int rankDown = 0;
        for (int i = 4; i >=0 ; --i) {
            if (fiveCards.get(i).getSuit() != suit || (fiveCards.get(i).getRank().ordinal()) != highestRank.ordinal() - rankDown) {
                return false;
            }
            rankDown++;
        }
        return true;
    }

    private boolean checkFourOfAKind() {
        //skoro sa posortowane, to musza byc 4 takie same obok siebie.
        int counter = 0;
        Card.rank rank = fiveCards.get(2).getRank();
        for (int i = 0; i < 5; ++i) {
            if(fiveCards.get(i).getRank().ordinal() == rank.ordinal())
                counter++;
        }

        return counter >= 4;
    }

    private boolean checkFullHouse() {
        Card.rank higherRank = fiveCards.get(4).getRank();
        Card.rank lowerRank = fiveCards.get(0).getRank();

        int counterHigherRank = 0;
        int counterLowerRank = 0;
        for(int i = 0; i < 5; ++i)
        {
            if(fiveCards.get(i).getRank().equals(higherRank))
                counterHigherRank++;
            if(fiveCards.get(i).getRank().equals(lowerRank))
                counterLowerRank++;
        }

        return (counterHigherRank == 2 && counterLowerRank == 3) || (counterHigherRank == 3 && counterLowerRank == 2);
    }

    private boolean checkFlush() {
        Card.suit suit = fiveCards.get(4).getSuit();
        Card.rank highestRank = fiveCards.get(4).getRank();

        for (int i = 3; i >= 0; --i) {
            if(fiveCards.get(i).getRank().ordinal() >= highestRank.ordinal() || !fiveCards.get(i).getSuit().equals(suit))
                return false;
        }
        return true;
    }

    private boolean checkStraight() {
        Card.rank highestRank = fiveCards.get(4).getRank();
        int rankDown = 0;
        for (int i = 4; i >=0 ; --i) {
            if ((fiveCards.get(i).getRank().ordinal()) != highestRank.ordinal() - rankDown) {
                return false;
            }
            rankDown++;
        }
        return true;
    }

    private boolean checkThreeOfAKind() {
        //skoro sa posortowane, to musza byc 3 takie same obok siebie.
        int counter = 0;
        Card.rank rank = fiveCards.get(2).getRank();
        for (int i = 0; i < 5; ++i) {
            if(fiveCards.get(i).getRank().ordinal() == rank.ordinal())
                counter++;
        }

        return counter == 3;
    }

    private boolean checkTwoPair() {
        Card.rank rank0 = fiveCards.get(0).getRank();
        Card.rank rank2 = fiveCards.get(2).getRank();
        Card.rank rank4 = fiveCards.get(4).getRank();
        int rank0Counter = 0;
        int rank2Counter = 0;
        int rank4Counter = 0;

        for (int i = 0; i < 5; ++i) {
            if(fiveCards.get(i).getRank().equals(rank0))
                rank0Counter++;
            if(fiveCards.get(i).getRank().equals(rank2))
                rank2Counter++;
            if(fiveCards.get(i).getRank().equals(rank4))
                rank4Counter++;
        }

        return (rank0Counter== 2 && rank2Counter == 2) || (rank0Counter == 2 && rank4Counter == 2) || (rank2Counter == 2 && rank4Counter == 2);
    }

    private boolean checkOnePair() {

        for(int i = 0 ; i < 4; i++)
        {
            if(fiveCards.get(i).getRank().equals(fiveCards.get(i+1).getRank()))
                return true;
        }
        return false;
    }

}
