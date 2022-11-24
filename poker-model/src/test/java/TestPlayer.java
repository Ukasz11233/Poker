import gameplay.Player;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPlayer{

    private Player testedPlayer;
    @Before
    public void createTestedPlayer() {
        testedPlayer = new Player("Player");
    }

    @Test
    public void testPlayerConstructor() {
        assertNotNull(testedPlayer);
    }

    @Test
    public void tesPlayerToString() {
        assertEquals("", testedPlayer.toString());
    }

}
