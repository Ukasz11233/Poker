import gameplay.Card;
import gameplay.Player;
import gameplay.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestTable {
    private Table table;
    private Player player1;
    private Player player2;

    @Test
    public void TestTableConstructor() {
        Table table = new Table(10);
        assertNotNull(table);
    }

    @Before
    public void CreateTableAnd2PlayersAnte20() {
        table = new Table(20);
        player1 = new Player();
        player2 = new Player();

        table.addPlayer(player1);
        table.addPlayer(player2);
        table.startGame();
        table.setWasRaised(true);

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
        table.setWasRaised(true);
        assertTrue(table.readPlayerMove("call", 0));
        assertEquals(360, player1.getCoins());
    }


    @Test
    public void TestReadPlayerMoveCheckAnte20WasRaisedFalse() {
        table.setWasRaised(false);
        assertTrue(table.readPlayerMove("check", 0));
    }

    @Test
    public void TestReadPlayerMoveRaise30WasRaisedFalse() {
        table.setWasRaised(false);
        assertTrue(table.readPlayerMove("raise 30", 0));
        assertEquals(350, player1.getCoins());
    }

    @Test
    public void TestEndOfRoundWasRaisedFalsePlayer1playingPlayer2notPlaying() {
        table.setWasRaised(false);
        player1.setPlaying(true);
        player2.setPlaying(false);
        assertFalse(table.isEndOFRound(1));
    }

    @Test
    public void TestEndOFRoundWasRaisedFalsePlayer1checkedPlayer2checked() {
        table.setWasRaised(false);
        player1.setHasChecked(true);
        player2.setHasChecked(true);
        assertTrue(table.isEndOFRound(1));
    }

    @Test
    public void TestEndOFRoundWasRaisedFalsePlayer1checkedPlayer2notChecked() {
        table.setWasRaised(false);
        player1.setHasChecked(true);
        player2.setHasChecked(false);
        assertFalse(table.isEndOFRound(1));
    }

    @Test
    public void TestEndOFRoundWasRaisedFalsePlayer1checkedPlayer2Fold() {
        table.setWasRaised(false);
        player1.setHasChecked(true);
        player2.setPlaying(false);
        assertTrue(table.isEndOFRound(0));
    }

    @Test
    public void TestTellWhatMovesPlayerHasFolded() {
        player1.setPlaying(false);
        String expectedString = "You have folded your cards. Need to wait for your move.";
        assertEquals(expectedString, table.tellWhatMoves(0).toString());
    }

    @Test
    public void TestTellWhatMovesDefault() {
        table.setWasRaised(false);
        String expectedString = "You can:\nFold\nCheck\nRaise (minimum:" + (20+10) + ")" + "\nStatus";
        assertEquals(expectedString, table.tellWhatMoves(0).toString());
    }

    @Test
    public void TestTellWhatMovesWasRaised() {
        String expectedString = "You can:\nFold\nCall (cost:" + 20 + ")\nRaise (minimum:" + (20+10) + ")" + "\nStatus";
        assertEquals(expectedString, table.tellWhatMoves(0).toString());
    }


    @Test
    public void TestToString() {
        player1.setHasChecked(true);
        player2.setPlaying(false);

        StringBuilder expectedString = new StringBuilder();

        expectedString.append("Players status: ");
        expectedString.append("\nId: ").append(0).append(" status: ").append(player1.status());
        expectedString.append("\nId: ").append(1).append(" status: ").append(player2.status());
        expectedString.append("\nPot on table: ").append(40);
        assertEquals(expectedString.toString(), table.toString());
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

    @Test
    public void TestResetAllStatistics() {
        table.setWasRaised(true);
        table.resetAllStatistics();

        StringBuilder expectedString = new StringBuilder();

        expectedString.append("Players status: ");
        expectedString.append("\nId: ").append(0).append(" status: ").append(player1.status());
        expectedString.append("\nId: ").append(1).append(" status: ").append(player2.status());
        expectedString.append("\nPot on table: ").append(0);

        assertEquals(expectedString.toString(), table.toString());
    }
    @Test
    public void TestRemovePlayer() {
        table.removePlayer(1);

        StringBuilder expectedString = new StringBuilder();

        expectedString.append("Players status: ");
        expectedString.append("\nId: ").append(0).append(" status: ").append(player1.status());
        expectedString.append("\nPot on table: ").append(40);
        assertEquals(expectedString.toString(), table.toString());

    }

    @Test
    public void TestEndRound() {
        ArrayList<Card> cardsFlush = new ArrayList<>();
        cardsFlush.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cardsFlush.add(new Card(Card.suit.DIAMOND, Card.rank.FIVE));
        cardsFlush.add(new Card(Card.suit.DIAMOND, Card.rank.SEVEN));
        cardsFlush.add(new Card(Card.suit.DIAMOND, Card.rank.EIGHT));
        cardsFlush.add(new Card(Card.suit.DIAMOND, Card.rank.KING));

        ArrayList<Card> cardsOnePair = new ArrayList<>();
        cardsOnePair.add(new Card(Card.suit.DIAMOND, Card.rank.THREE));
        cardsOnePair.add(new Card(Card.suit.SPADE, Card.rank.THREE));
        cardsOnePair.add(new Card(Card.suit.HEART, Card.rank.FIVE));
        cardsOnePair.add(new Card(Card.suit.CLUB, Card.rank.SIX));
        cardsOnePair.add(new Card(Card.suit.SPADE, Card.rank.SEVEN));

        player1.putAsideAllCards();
        player1.getFiveCards(cardsFlush);
        player2.putAsideAllCards();
        player2.getFiveCards(cardsOnePair);

        assertEquals(0, table.endRound());
        assertEquals(420, player1.getCoins());
    }

}
