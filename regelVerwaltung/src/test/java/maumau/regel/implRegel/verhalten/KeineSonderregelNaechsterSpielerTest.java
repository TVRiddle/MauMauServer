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

public class KeineSonderregelNaechsterSpielerTest {

    private KeineSonderregelNaechsterSpieler keineSonderregelNaechsterSpieler;
    private PlayerImpl p1;
    private PlayerImpl p2;
    private PlayerImpl p3;
    private PlayerImpl p4;
    private List<IPlayer> playerList;

    @Before
    public void init() {
        keineSonderregelNaechsterSpieler = new KeineSonderregelNaechsterSpieler();
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
    public void naechsterSpielerWennAchtLiegt() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERT8);

        IPlayer aktuellerPlayer = keineSonderregelNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p1, k1, 1);

        assertEquals("Player2", aktuellerPlayer.getName());
    }

    @Test
    public void naechsterSpielerWennAssLiegt() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERTAss);

        IPlayer aktuellerPlayer = keineSonderregelNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p1, k1, 1);

        assertEquals("Player2", aktuellerPlayer.getName());
    }

    // Die folgenden Funktionen Testen die DefaultMethode
    @Test
    public void aktuellenSpielerNachDurchitterierenWiederAufIndexNullSetzen() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERTKoenig);

        IPlayer aktuellerPlayer = keineSonderregelNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p4, k1, 1);

        assertEquals("Player1", aktuellerPlayer.getName());
    }

    @Test
    public void aktuellenSpielerNachDurchitterierenUndAchtAufIndexEinsSetzen() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERTKoenig);

        IPlayer aktuellerPlayer = keineSonderregelNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p4, k1, 2);

        assertEquals("Player2", aktuellerPlayer.getName());
    }

    @Test
    public void aktuellenSpielerNachDurchitterierenAufHoechstenIndexSetzen() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERTKoenig);

        IPlayer aktuellerPlayer = keineSonderregelNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, p1, k1, -1);

        assertEquals("Player4", aktuellerPlayer.getName());
    }

    @Test
    public void erstenSpielerSetzen() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERTKoenig);

        IPlayer aktuellerPlayer = keineSonderregelNaechsterSpieler
            .bestimmeNaechstenSpieler(playerList, null, k1, -1);

        assertEquals("Player1", aktuellerPlayer.getName());
    }

}
