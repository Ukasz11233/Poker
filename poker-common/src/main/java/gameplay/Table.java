package gameplay;

import java.util.ArrayList;

public class Table {
    private int numOfPlayers;
    private ArrayList<Player> players;
    private Deck deck;
    private int potOnTable;

    private int ante;

    public Table(int _ante) {
        players = new ArrayList<>();
        deck = new Deck();
        numOfPlayers = 0;
        potOnTable = 0;
        ante = _ante;

    }

    public void startGame() {
        deck.shuffleDeck();
        for (Player player : players) {
            player.setPlaying(player.getCoins() > ante);
            if (player.isPlaying()) {
                potOnTable += 10;
                player.addCoins(-10);
                player.getFiveCards(deck);
            }
        }

    }

    public String toString() {
        return "Liczba graczy: " + numOfPlayers
                + "\nPot na stole: " + potOnTable;
    }

    public String playerInfo(int idx) {
        return "Coins: " + players.get(idx).getCoins() + "\nCards:" + players.get(idx);
    }
    public void addPlayer(Player _player) {
        players.add(_player);
        numOfPlayers++;
    }

    public void removePlayer(int idx)
    {
        players.remove(idx);
        numOfPlayers--;
    }
}
