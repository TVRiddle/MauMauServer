package maumau.regel.implRegel.verhalten;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import maumau.implPlayer.PlayerImpl;
import maumau.karten.interKarten.module.Karte;
import org.junit.Before;
import org.junit.Test;

public class KeinMauVerhaltenTest {

    private KeinMauVerhalten keinMauVerhalten;
    private List<Karte> karten;
    private PlayerImpl player;

    @Before
    public void init() {
        keinMauVerhalten = new KeinMauVerhalten();
        karten = new ArrayList<>();
        player = mock(PlayerImpl.class);
        when(player.getKarten()).thenReturn(karten);

    }

    @Test
    public void keinSpeziellesVerhaltenErwartet() {
        assertTrue(keinMauVerhalten.hatRichtigMauGesagt(player, true));
        assertTrue(keinMauVerhalten.hatRichtigMauGesagt(player, false));
    }

}
