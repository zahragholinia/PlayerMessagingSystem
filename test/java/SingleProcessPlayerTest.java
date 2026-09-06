import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleProcessPlayerTest {

    @Test
    void shouldCreatePlayerAsInitiator() {
        SingleProcessPlayer player = new SingleProcessPlayer(true);

        assertNotNull(player);
    }
}
