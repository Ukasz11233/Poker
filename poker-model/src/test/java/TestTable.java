import gameplay.Player;
import gameplay.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestTable {
    private Table table;
    private Player player1;
    private Player player2;

    @Test
    public void TestTableConstructor() {
        Table table = new Table(10);

        assertEquals(0, table.numOfRounds);
        assertFalse(table.wasChanged);
        assertFalse(table.wasRaised);
        assertEquals(0, table.playerToStop);
    }

    @Before
    public void CreateTableAnd2PlayersAnte20() {
        table = new Table(20);
        player1 = new Player();
        player2 = new Player();

        table.addPlayer(player1);
        table.addPlayer(player2);
        table.startGame();
        table.wasRaised = true;

    }
    @Test
    public void TestStartGameAnte20() {
        assertEquals(380, player1.getCoins());
        assertEquals(380, player2.getCoins());
    }

    @Test
    public void TestReadPlayerMoveFoldAnte20() {
        assertTrue(table.readPlayerMove("fold", 0));
    }

    @Test
    public void TestReadPlayerMoveRaise20Ante20() {
        assertTrue(table.readPlayerMove("raise 20", 0));
        assertEquals(360, player1.getCoins());

    }

    @Test
    public void TestReadPlayerMoveRaise100Ante20() {
        assertTrue(table.readPlayerMove("raise 100", 1));
        assertEquals(280, player2.getCoins());
    }


    @Test
    public void TestReadPlayerMoveCallAnte20() {
        assertTrue(table.readPlayerMove("call", 0));
        assertEquals(360, player1.getCoins());
    }


    @Test
    public void TestReadPlayerMoveCheckAnte20() {
        table.wasRaised = false;
        assertTrue(table.readPlayerMove("check", 0));
    }


    @Test
    public void TestEndOfRoundWasRaisedFalsePlayer1playingPlayer2notPlaying() {
        table.wasRaised = false;
        player1.setPlaying(true);
        player2.setPlaying(false);
        assertFalse(table.isEndOFRound(1));
    }

    @Test
    public void TestEndOFRoundWasRaisedFalsePlayer1checkedPlayer2checked() {
        table.wasRaised = false;
        player1.setHasChecked(true);
        player2.setHasChecked(true);
        assertTrue(table.isEndOFRound(1));
    }

    @Test
    public void TestEndOFRoundWasRaisedFalsePlayer1checkedPlayer2notChecked() {
        table.wasRaised = false;
        player1.setHasChecked(true);
        player2.setHasChecked(false);
        assertFalse(table.isEndOFRound(1));
    }

    @Test
    public void TestEndOFRoundWasRaisedFalsePlayer1checkedPlayer2Fold() {
        table.wasRaised = false;
        player1.setHasChecked(true);
        player2.setPlaying(false);
        assertTrue(table.isEndOFRound(0));
    }


    @Test
    public void TestGetNextPlayerMove() {
        Player player3 = new Player();
        Player player4 = new Player();
        table.addPlayer(player3);
        table.addPlayer(player4);

        player1.setPlaying(true);
        player2.setPlaying(false);
        player3.setPlaying(true);
        player4.setPlaying(true);

        player1.setHasChecked(true);
        player2.setHasChecked(false);
        player3.setHasChecked(false);
        player4.setHasChecked(false);

        assertEquals(2, table.getNextPlayerMove(1));
    }




}
