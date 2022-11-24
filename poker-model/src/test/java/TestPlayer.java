import gameplay.Player;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPlayer{

    @Test
    public void testPlayerConstructor() {
        Player testedPlayer = new Player("Player");

        assertNotNull(testedPlayer);
    }

    @Test
    public void tesPlayerToString() {
        Player testedPlayer = new Player("Player");
        assertEquals("", testedPlayer.toString());

    }
}
