package utils.gameplay;

import java.util.ArrayList;

public class Table {
    private int numOfPlayers;
    private ArrayList<Player> players;
    private int poleOnTable;

    public Table(int _numOfPlayers) {
        numOfPlayers = _numOfPlayers;
        poleOnTable = 0;
    }

    public void addPlayer(Player _player) {
        players.add(_player);
    }
}
