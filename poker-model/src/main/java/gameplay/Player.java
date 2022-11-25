package gameplay;

import java.util.ArrayList;

import static java.util.Collections.max;

public class Player {
    private ArrayList<Card> cards;

    private String username;
    private int coins;

    // Zmienna do gameplay-u
    private boolean isPlaying;

    private boolean hasChecked;

    private int result;


    public Player(String _username) {
        username = _username;
        coins = 400;
        cards = new ArrayList<>();
        isPlaying = false;
        hasChecked = false;
        result = 0;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        int idx = 1;
        if(cards != null)
        {
            for (Card cartToStr : cards) {
                result.append("\n").append(idx++).append(".").append(cartToStr);
            }
        }
        return result.toString();
    }

    public void getFiveCards(Deck deck) {
        for (int i = 0; i < 5; i++) {
            cards.add(deck.getCardFromDeck());
        }
        result = countResult();
    }

    //Metoda na potrzeby testów
    public void getFiveCards(ArrayList<Card> arrayCards) {
        for (int i = 0; i < 5; i++) {
            cards.add(arrayCards.get(i));
        }
        result = countResult();
    }

    public void putAsideAllCards() {
        cards = new ArrayList<>(5);
        result = 0;
    }

    public int countResult() {
        int result = 0;
        int tempResult = 0;
        FiveCards fiveCards = new FiveCards(cards);
        fiveCards.sortFiveCards();
        for (Deck.rules rule : Deck.rules.values()) {
            if (fiveCards.checkRule(rule)) {
                tempResult = (int) (Math.pow(2, (rule.ordinal()))*Math.pow(2, 5));
                result =  Math.max(result, tempResult);
            }
        }
        for(int i = 4; i >=0; --i)
        {
            result += (fiveCards.getFiveCards().get(i).getRank().ordinal() + 1)*Math.pow(2, i);
        }

        return result;
    }





    public int getCoins() {
        return coins;
    }

    public void addCoins(int _coins){
        coins += _coins;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

    public int getResult() {
        return result;
    }
}
