package gameplay;

import java.util.ArrayList;

public class Table {
    private int numOfPlayers;
    private ArrayList<Player> players;
    private final Deck deck;
    private int potOnTable;

    private final int ante;

    // Pola do gameplay-u
    public int numOfRounds;
    public boolean wasChanged;
    public boolean wasRaised;
    public int playerToStop;  //[0..4]

    private int minimalPot;

    public Table(int anteToSet) {
        players = new ArrayList<>();
        deck = new Deck();
        //deck.shuffleDeck(); //latwiej debugowac
        numOfPlayers = 0;
        potOnTable = 0;
        ante = anteToSet;
        minimalPot = ante;

        numOfRounds = 0;
        wasChanged = false;
        wasRaised = false;
        playerToStop = 0; // ? a moze -1

    }

    public void startGame() {
        deck.shuffleDeck();
        for (Player player : players) {
            addPlayerToTable(player);
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
        String [] playerMoveArray = playerMove.split("\\s", 2);
        if (playerMoveArray[0].equalsIgnoreCase("fold")) {
            players.get(playerNumber).setPlaying(false);
            return true;
        }
        if (!wasRaised) {
            if (playerMoveArray[0].equalsIgnoreCase("Raise")) {
                wasRaised = true;
                minimalPot = Integer.parseInt(playerMoveArray[1]);
                potOnTable += Integer.parseInt(playerMoveArray[1]);
                players.get(playerNumber).addCoins(-1 *Integer.parseInt(playerMoveArray[1]));
                return true;
            } else if (playerMoveArray[0].equalsIgnoreCase("check")) {
                players.get(playerNumber).setHasChecked(true);
                return true;
            }
        }
        else{
            if (playerMoveArray[0].equalsIgnoreCase("Raise")) {
                minimalPot = Integer.parseInt(playerMoveArray[1]);
                potOnTable += Integer.parseInt(playerMoveArray[1]);
                players.get(playerNumber).addCoins(-1 *Integer.parseInt(playerMoveArray[1]));
                return true;
            } else if (playerMoveArray[0].equalsIgnoreCase("call")) {
                potOnTable += minimalPot;
                players.get(playerNumber).addCoins(-1 *minimalPot);
                return true;
            }
        }
        return false;
    }



    public String tellWhatMoves(int playerNumber) {
        String result;
        if(!players.get(playerNumber).isPlaying())
            result = "You Folded your cards. Need to wait.";
        else if (!wasRaised) {
            result = "You can:\nFold\nCheck\nRaise (minimum:" + (minimalPot+10) + ")" + "\nStatus";
        }else {
            result = "You can:\nFold\nCall (cost:" + minimalPot + ")\nRaise (minimum:" + (minimalPot+10) + ")" + "\nStatus";
        }

        return result;
    }

    public int endRound() {
        int winner = getRoundWinner();
        players.get(winner).addCoins(potOnTable);
        resetAllStatistics();
        numOfRounds++;
        return winner;
    }

    public void resetAllStatistics() {
        potOnTable = 0;
        minimalPot = ante;
        wasChanged = false;
        wasRaised = false;
        deck.resetDeck();
        for (Player player : players) {
            player.setHasChecked(false);
            player.setPlaying(true);
            player.putAsideAllCards();
            addPlayerToTable(player);
        }
    }

    private void addPlayerToTable(Player player) {
        player.setPlaying(player.getCoins() > ante);
        if (player.isPlaying()) {
            potOnTable += ante;
            player.addCoins(-1 * ante);
            player.getFiveCards(deck);
        }
    }
    public int getRoundWinner() {
        int maxVal = -1;
        int winner = -1;
        for (int i = 0; i < players.size(); ++i) {
            // jesli fold to nie moze wygrac
            if(!players.get(i).isPlaying())
                continue;
            if (maxVal < players.get(i).getResult()) {
                maxVal = players.get(i).getResult();
                winner = i;
            }
        }
        return winner;
    }

    public boolean isEndOFRound(int playerIdx) {
        return isEveryoneExceptCurrPlayerFolded(playerIdx) || hasEveryoneChecked();
    }

    private boolean hasEveryoneChecked() {
        for (Player player : players) {
            if(!player.isHasChecked())
                return false;
        }
        return true;
    }

    private boolean isEveryoneExceptCurrPlayerFolded(int currPlayerIdx) {
        Player currPlayer = players.get(currPlayerIdx);
        for (Player player : players) {
            if(player.equals(currPlayer)) {
                continue;
            }
            if(player.isPlaying()) {
                return false;
            }
        }
        return true;
    }
    public void addPlayer(Player playerToAdd) {
        players.add(playerToAdd);
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
