package gameplay;

import java.util.ArrayList;

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

    public void putAsideAllCards() {
        cards = new ArrayList<>(5);
        result = 0;
    }

    private int countResult() {
        int result = 0;
        for (Card card : cards) {
            result += card.cardValue();
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
