package utils.gameplay;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> cards = new ArrayList<Card>(5);

    private String username;


    public Player(String _username) {
        username = _username;
    }

    public String toString() {
        String result = new String();
        int idx = 1;
        for (Card cartToStr : cards) {
            result += (idx++) + "." +  cartToStr + " ";
        }
        return result;
    }

    public void getFiveCards(Deck deck) {
        for (int i = 0; i < 5; i++) {
            cards.add(deck.getCardFromDeck());
        }
    }


    public String getUsername() {
        return username;
    }
}
