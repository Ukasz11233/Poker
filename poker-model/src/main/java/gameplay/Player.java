package gameplay;

import java.util.ArrayList;
import java.util.List;


public class Player {
    private List<Card> cards;

    private int coins;

    // Zmienna do gameplay-u
    private boolean isPlaying;

    private boolean hasChecked;

    private int result;


    public Player() {
        coins = 400;
        cards = new ArrayList<>();
        isPlaying = false;
        hasChecked = false;
        result = 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int idx = 1;
        if(cards != null)
        {
            for (Card cardToStr : cards) {
                stringBuilder.append("\n").append(idx++).append(".").append(cardToStr);
            }
        }
        return stringBuilder.toString();
    }

    public void getFiveCards(Deck deck) {
        for (int i = 0; i < 5; i++) {
            cards.add(deck.getCardFromDeck());
        }
        result = countResult();
    }

    //Metoda na potrzeby testów
    public void getFiveCards(List<Card> arrayCards) {
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
        int currentResult = 0;
        int tempResult;
        FiveCards fiveCards = new FiveCards(cards);
        fiveCards.sortFiveCards();
        for (Deck.rules rule : Deck.rules.values()) {
            if (fiveCards.checkRule(rule)) {
                tempResult = (int) (Math.pow(2, (rule.ordinal()))*Math.pow(2, 5));
                currentResult =  Math.max(currentResult, tempResult);
            }
        }
        for(int i = 4; i >=0; --i)
        {
            currentResult += (fiveCards.getCurrCards().get(i).getRank().ordinal() + 1)*Math.pow(2, i);
        }

        return currentResult;
    }

    public String status() {
        if(!isPlaying) return "Folded";
        else if(hasChecked) return "Checked";
        else return "plays";
    }



    public int getCoins() {
        return coins;
    }

    public void addCoins(int coinsToAdd){
        coins += coinsToAdd;
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
