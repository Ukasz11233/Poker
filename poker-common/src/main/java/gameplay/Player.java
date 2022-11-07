package gameplay;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> cards;

    private String username;
    private int coins;

    private boolean isPlaying;

    // Zmienna do gameplay-u
    public boolean hasFolded;

    public Player(String _username) {
        username = _username;
        coins = 400;
        cards = new ArrayList<>();
        hasFolded = false;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        int idx = 1;
        for (Card cartToStr : cards) {
            result.append("\n").append(idx++).append(".").append(cartToStr);
        }
        return result.toString();
    }

    public void getFiveCards(Deck deck) {
        for (int i = 0; i < 5; i++) {
            cards.add(deck.getCardFromDeck());
        }
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
}
