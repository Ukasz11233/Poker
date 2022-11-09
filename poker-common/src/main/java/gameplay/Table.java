package gameplay;

import java.util.ArrayList;

public class Table {
    private int numOfPlayers;
    private ArrayList<Player> players;
    private Deck deck;
    private int potOnTable;

    private int ante;

    // Pola do gameplay-u
    public int numOfRounds;
    public boolean wasChanged;
    public boolean wasRaised;
    public int playerToStop;  //[0..4]

    private int minimalPot;

    public Table(int _ante) {
        players = new ArrayList<>();
        deck = new Deck();
        numOfPlayers = 0;
        potOnTable = 0;
        ante = _ante;
        minimalPot = ante;

        numOfRounds = 0;
        wasChanged = false;
        wasRaised = false;
        playerToStop = 0; // ? a moze -1

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

    public boolean readPlayerMove(String playerMove, int playerNumber) {
        boolean result = true;
        String [] playerMoveArray = playerMove.split("\\s", 2);
        if (playerMoveArray[0].equalsIgnoreCase("fold")) {

            players.get(playerNumber).setPlaying(true);
        }

        if (!wasRaised) {
            if (playerMove.equalsIgnoreCase("Raise")) {
                result = true;
            }
        }

        return result;
    }

    public String tellWhatMoves(int playerNumber) {
        String result = "I DONT KNOW!!! DEBUG ME";
        if(players.get(playerNumber).isPlaying())
            result = "You Folded your cards. Need to wait.";
        else if (!wasRaised) {
            result = "You can:\nFold\nCheck\nRaise (minimum:" + (minimalPot+10) + ")";
        } else {
            result = "You can:\nFold\nCall (cost:" + minimalPot + ")\nRaise (minimum:" + (minimalPot+10) + ")";
        }

        return result;
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

    public void setPlayerToStop(int idx) {
        playerToStop = idx;
    }


}
