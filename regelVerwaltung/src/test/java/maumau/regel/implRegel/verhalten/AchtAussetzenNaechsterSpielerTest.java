package maumau.regel.implRegel.verhalten;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import maumau.implPlayer.PlayerImpl;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import org.junit.Before;
import org.junit.Test;

public class AchtAussetzenNaechsterSpielerTest {

    private AchtAussetzenNaechsterSpieler achtAussetzenNaechsterSpieler;
    private PlayerImpl p1;
    private PlayerImpl p2;
    private PlayerImpl p3;
    private PlayerImpl p4;
    private List<IPlayer> playerList;

    @Before
    public void init() {
        achtAussetzenNaechsterSpieler = new AchtAussetzenNaechsterSpieler();
        playerList = new ArrayList<>();
        p1 = mock(PlayerImpl.class);
        p2 = mock(PlayerImpl.class);
        p3 = mock(PlayerImpl.class);
        p4 = mock(PlayerImpl.class);
        when(p1.getName()).thenReturn("Player1");
        when(p2.getName()).thenReturn("Player2");
        when(p3.getName()).thenReturn("Player3");
        when(p4.getName()).thenReturn("Player4");
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
    }

    @Test
    public void uebernaechsterSpielerWennAchtLiegt() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERT8);

        IPlayer aktuellerPlayer = achtAussetzenNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p1, k1, 1);

        assertEquals("Player3", aktuellerPlayer.getName());
    }

    @Test
    public void naechsterSpielerWennAchtNichtLiegt() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERT9);

        IPlayer aktuellerPlayer = achtAussetzenNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p1, k1, 1);

        assertEquals("Player2", aktuellerPlayer.getName());
    }

    @Test
    public void spielerMussAussetzenAndereRichtung() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERT8);

        IPlayer aktuellerPlayer = achtAussetzenNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p1, k1, -1);

        assertEquals("Player3", aktuellerPlayer.getName());
    }


}
