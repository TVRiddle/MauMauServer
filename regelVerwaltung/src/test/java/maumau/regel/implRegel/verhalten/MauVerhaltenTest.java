package maumau.regel.implRegel.verhalten;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import maumau.implPlayer.PlayerImpl;
import maumau.karten.interKarten.module.Karte;
import org.junit.Before;
import org.junit.Test;

public class MauVerhaltenTest {

    private MauVerhalten mauVerhalten;
    private List<Karte> karten;
    private PlayerImpl player;

    @Before
    public void init() {
        mauVerhalten = new MauVerhalten();
        karten = new ArrayList<>();
        player = mock(PlayerImpl.class);
        when(player.getKarten()).thenReturn(karten);
    }

    @Test
    public void haetteMauSagenSollen() {
        karten.add(new Karte());
        karten.add(new Karte());
        assertFalse(mauVerhalten.hatRichtigMauGesagt(player, false));
    }

    @Test
    public void hatFalschGesagtWennLetzteKarteGespielt() {
        karten.add(new Karte());
        assertFalse(mauVerhalten.hatRichtigMauGesagt(player, true));
    }

    @Test
    public void hatFalschGesagtWennNochZuVieleKarten() {
        karten.add(new Karte());
        karten.add(new Karte());
        karten.add(new Karte());
        assertFalse(mauVerhalten.hatRichtigMauGesagt(player, true));
    }

    @Test
    public void hatAllesRichtigGemacht() {
        karten.add(new Karte());
        karten.add(new Karte());
        assertTrue(mauVerhalten.hatRichtigMauGesagt(player, true));
    }

}
