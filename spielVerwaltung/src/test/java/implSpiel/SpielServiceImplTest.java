package implSpiel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import maumau.implPlayer.PlayerImpl;
import maumau.interPlayer.IPlayer;
import maumau.karten.implKarten.MauMauCardFactory;
import maumau.karten.interKarten.ICardFactory;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import maumau.regel.implRegel.RegelFactoryImpl;
import maumau.regel.implRegel.RegelServiceImpl;
import maumau.spiel.model.implSpiel.SpielServiceImpl;
import maumau.spiel.model.interSpiel.exceptions.AnzahlSpielerException;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse stellt alle Funktionen zur Verfuegung die benoetigt werden um ein Spiel am laufen zu
 * halten. Sie dient als Bindeglied zwischen den einzelnen Komponenten
 */
public class SpielServiceImplTest {

    private static SpielServiceImpl ssi;
    private static ICardFactory cardFactory;
    private static RegelFactoryImpl regelFactory;
    private static RegelServiceImpl regel;

    @Before
    public void erzeugeTestobjekt() {
        ssi = new SpielServiceImpl();

        cardFactory = mock(MauMauCardFactory.class);
        ssi.setCardFactory(cardFactory);

        regelFactory = mock(RegelFactoryImpl.class);
        regel = mock(RegelServiceImpl.class);

        List<Karte> karten = new ArrayList<>();
        for (KartenFarbe kf : KartenFarbe.values()) {
            for (KartenWert kw : KartenWert.values()) {
                karten.add(new Karte(kf, kw));
            }
        }
        when(cardFactory.createNewCardSet()).thenReturn(karten);
        when(regelFactory.getRegelService(any())).thenReturn(regel);
        ssi.setRegelFactory(regelFactory);
    }

    @Test
    public void erzeugeMauMauSpielTest() throws AnzahlSpielerException {
        Spiel s = ssi.erzeugeSpiel("Player 1", 2, 0, 0, 0, new ArrayList<>());
        assertEquals(32, s.getZiehStapel().size());
    }

    @Test
    public void mehrAlsSechSpielerHinzugefuegtUndZuvieleSpielerExceptionErwartet() {
        List<IPlayer> spielerList = new ArrayList<>();

        Spiel spiel = mock(Spiel.class);
        PlayerImpl spieler = mock(PlayerImpl.class);
        when(spiel.getPlayers()).thenReturn(spielerList);

        for (int i = 0; i < 6; i++) {
            spielerList.add(spieler);
        }
        AnzahlSpielerException ex = assertThrows(AnzahlSpielerException.class,
            () -> ssi.neuerSpieler(spiel, new PlayerImpl()));
        assertEquals("Zu es können nur maximal 6 mitspielen!", ex.getMessage());
    }

    @Test
    public void korreckteZahlAnSpielernHinzugefuegt()
        throws AnzahlSpielerException {
        List<IPlayer> spielerList = new ArrayList<>();

        Spiel spiel = mock(Spiel.class);
        PlayerImpl spieler = mock(PlayerImpl.class);
        when(spiel.getPlayers()).thenReturn(spielerList);
        ssi.neuerSpieler(spiel, spieler);
        ssi.neuerSpieler(spiel, spieler);
        ssi.neuerSpieler(spiel, spieler);

        assertEquals(3, spiel.getPlayers().size());
    }

    @Test
    public void kartenWerdenKorrecktVerteilt() throws KeineKartenMehr {
        PlayerImpl p1 = new PlayerImpl();
        PlayerImpl p2 = new PlayerImpl();
        Spiel spiel = new Spiel();
        List<Karte> kartenSp = new ArrayList<>();
        List<Karte> kartenSp1 = new ArrayList<>();
        List<Karte> ziehStapel = cardFactory.createNewCardSet();
        spiel.setPlayers(new ArrayList<>(Arrays.asList(p1, p2)));
        spiel.setZiehStapel(ziehStapel);
        p1.setKarten(kartenSp);
        p2.setKarten(kartenSp1);

        ssi.kartenVerteilen(spiel);
        for (IPlayer spieler : spiel.getPlayers()) {
            assertEquals(5, spieler.getKarten().size());
        }
    }

    @Test
    public void spielVorbereitenKarteLiegtAufAblagestapel() throws KeineKartenMehr {
        ArrayList<Karte> ablageStapel = new ArrayList<>();
        ArrayList<Karte> ziehStapel = new ArrayList<>(cardFactory.createNewCardSet());
        Spiel spiel = new Spiel();

        spiel.setAblageStapel(ablageStapel);
        spiel.setZiehStapel(ziehStapel);

        ssi.spielVorbereiten(spiel);
        assertEquals(1, spiel.getAblageStapel().size());
        assertEquals(31, spiel.getZiehStapel().size());
    }

    @Test
    public void spielLauft() {
        List<IPlayer> spielerListe = new ArrayList<>();
        List<Karte> kartenListe = new ArrayList<>(Arrays.asList(new Karte()));

        Spiel spiel = mock(Spiel.class);
        PlayerImpl spieler = mock(PlayerImpl.class);
        when(spiel.getPlayers()).thenReturn(spielerListe);
        when(spiel.getAblageStapel()).thenReturn(kartenListe);
        when(spieler.getKarten()).thenReturn(kartenListe);

        for (int i = 0; i < 6; i++) {
            spielerListe.add(spieler);
        }
        assertTrue(ssi.istSpielLaufend(spiel));
    }

    @Test
    public void spielLauftNochNicht() {
        Spiel spiel = mock(Spiel.class);
        when(spiel.getAblageStapel()).thenReturn(Collections.emptyList());
        assertFalse(ssi.istSpielLaufend(spiel));
    }

    @Test
    public void spielLauftNichtMehrWeilSpielerKeineKartenHat() {
        Spiel spiel = mock(Spiel.class);
        PlayerImpl spieler = mock(PlayerImpl.class);
        when(spiel.getAblageStapel()).thenReturn(Arrays.asList(new Karte()));
        when(spiel.getPlayers()).thenReturn(Arrays.asList(spieler));
        when(spieler.getKarten()).thenReturn(Collections.emptyList());

        assertFalse(ssi.istSpielLaufend(spiel));
    }

    @Test
    public void spielLauftNichtMehrWeilNurEinSpielerUebrig()
        throws AnzahlSpielerException, KeineKartenMehr {
        List<Karte> ziehstapel = new ArrayList<>(
            Arrays.asList(new Karte(KartenFarbe.KARO, KartenWert.WERTBube)));
        List<Karte> ablagestapel = new ArrayList<>(
            Arrays.asList(new Karte(KartenFarbe.PIK, KartenWert.WERT9)));
        Spiel spiel = mock(Spiel.class);

        when(spiel.getAblageStapel()).thenReturn(ablagestapel);
        when(spiel.getZiehStapel()).thenReturn(ziehstapel);

        ssi.neuerSpieler(spiel, new PlayerImpl());
        ssi.spielVorbereiten(spiel);
        ssi.kartenVerteilen(spiel);
        assertFalse(ssi.istSpielLaufend(spiel));
    }

    @Test
    public void istGespielteKarteAufAblagestapel() {
        Karte karte = new Karte(KartenFarbe.HERZ, KartenWert.WERT7);
        Karte karte1 = new Karte(KartenFarbe.PIK, KartenWert.WERT9);
        List<Karte> testAblageList = new ArrayList<>(Arrays.asList(karte1));

        Spiel spiel = mock(Spiel.class);
        PlayerImpl spieler = mock(PlayerImpl.class);
        when(spiel.getAktuellerPlayer()).thenReturn(spieler);
        when(spiel.getAblageStapel()).thenReturn(testAblageList);
        when(spieler.getKarten()).thenReturn(new ArrayList<>(Arrays.asList(karte)));

        ssi.karteSpielen(spiel, karte);
        assertEquals(karte, testAblageList.get(testAblageList.size() - 1));
    }

    @Test
    public void istGespielteKarteAufGespeichertemStapel() {
        Karte karte = new Karte(KartenFarbe.HERZ, KartenWert.WERT7);
        List<Karte> testAblageList = new ArrayList<>(Arrays.asList(new Karte()));
        List<Karte> testAktiveKartenList = new ArrayList<>();

        Spiel spiel = mock(Spiel.class);
        PlayerImpl spieler = mock(PlayerImpl.class);
        when(spiel.getAktuellerPlayer()).thenReturn(spieler);
        when(spiel.getAblageStapel()).thenReturn(testAblageList);
        when(spiel.getAktiveKarten()).thenReturn(testAktiveKartenList);
        when(spieler.getKarten()).thenReturn(new ArrayList<>(Arrays.asList(karte)));
        when(regel.gueltigeKarte(any(), any())).thenReturn(true);
        when(regel.sollGespeichertWerden(any(), any())).thenReturn(true);

        ssi.karteSpielen(spiel, karte);
        assertEquals(karte, testAktiveKartenList.get(testAktiveKartenList.size() - 1));
    }

    @Test
    public void istGespielteKarteNichtGleichGespielterKartenImStapelLoescheAlleUndSpeichereNeueKarte() {
        Karte karte = new Karte(KartenFarbe.HERZ, KartenWert.WERT7);
        Karte karte2 = new Karte(KartenFarbe.PIK, KartenWert.WERTAss);
        List<Karte> testAktiveKartenList = new ArrayList<>(Arrays.asList(karte2));

        Spiel spiel = mock(Spiel.class);
        PlayerImpl spieler = mock(PlayerImpl.class);
        when(spiel.getAktuellerPlayer()).thenReturn(spieler);
        when(spiel.getAktiveKarten()).thenReturn(testAktiveKartenList);
        when(regel.sollGespeichertWerden(any(), any())).thenReturn(true);
        when(spieler.getKarten()).thenReturn(new ArrayList<>(Arrays.asList(karte)));

        ssi.karteSpielen(spiel, karte);
        assertEquals(1, testAktiveKartenList.size());
        assertEquals(KartenWert.WERT7, testAktiveKartenList.get(0).getWert());
        assertEquals(KartenFarbe.HERZ, testAktiveKartenList.get(0).getFarbe());
    }

    @Test
    public void karteWirdGezogen() throws KeineKartenMehr {

        Spiel s = new Spiel();
        s.setZiehStapel(ssi.getCardFactory().createNewCardSet());
        Karte k = ssi.zieheKarteVonZiehstapel(s);
        assertEquals(31, s.getZiehStapel().size());
        assertFalse(s.getZiehStapel().contains(k));
    }

    @Test
    public void karteWirdGezogenUndKeineKarteMehrVorhanden() throws KeineKartenMehr {
        Spiel s = new Spiel();
        s.setZiehStapel(ssi.getCardFactory().createNewCardSet());
        for (int i = 0; i < 32; i++) {
            ssi.zieheKarteVonZiehstapel(s);
        }
        KeineKartenMehr ex = assertThrows(KeineKartenMehr.class,
            () -> ssi.zieheKarteVonZiehstapel(s));
        assertEquals(ex.getMessage(), "Es sind keine Karten mehr auf vorhanden");
    }

    @Test
    public void karteWirdGezogenUndAblagestapelIstLeer() throws KeineKartenMehr {
        Spiel s = new Spiel();
        s.setZiehStapel(cardFactory.createNewCardSet());
        for (int i = 31; i >= 0; i--) {
            s.getAblageStapel().add(s.getZiehStapel().get(i));
            s.getZiehStapel().remove(i);
        }
        assertEquals(0, s.getZiehStapel().size());
        assertEquals(32, s.getAblageStapel().size());
        ssi.zieheKarteVonZiehstapel(s);
        assertEquals(30, s.getZiehStapel().size());
        assertEquals(1, s.getAblageStapel().size());


    }

    @Test
    public void kartenWerdenGemischt() {
        Spiel s = mock(Spiel.class);
        List<Karte> ziehstapel = cardFactory.createNewCardSet();
        when(s.getZiehStapel()).thenReturn(ziehstapel);
        List<Karte> vergleichsListe = new ArrayList<>(s.getZiehStapel());
        ssi.ziehstapelMischen(s);
        long isMixed = vergleichsListe
            .stream()
            .map((k -> {
                int indexVergleich = vergleichsListe.indexOf(k);
                int indexMixed = s.getZiehStapel().indexOf(k);
                return indexVergleich == indexMixed;
            }))
            .filter(res -> res == true)
            .count();
        assertTrue(isMixed < 10);
    }


    @Test
    public void karteSpielenWennKarteIstNull() {
        Spiel s = mock(Spiel.class);
        ssi.karteSpielen(s, null);
        verify(s, never()).getAktuellerPlayer();
    }

    @Test
    public void karteSpielenWennGewuenschteKarteIstNull() {
        Spiel s = mock(Spiel.class);
        Karte karte = mock(Karte.class);
        PlayerImpl spieler1 = mock(PlayerImpl.class);
        when(spieler1.getKarten()).thenReturn(new ArrayList<>(Arrays.asList(karte)));
        when(s.getAktuellerPlayer()).thenReturn(spieler1);
        ssi.karteSpielen(s, karte);
        verify(s, times(1)).setGueltigeKarte(karte);
    }

    @Test
    public void spielerHatMauFalschGesagt() throws KeineKartenMehr {
        PlayerImpl player = new PlayerImpl();
        player.getKarten().add(new Karte());
        player.getKarten().add(new Karte());
        Spiel spiel = new Spiel();
        spiel.getZiehStapel().add(new Karte());
        spiel.setAktuellerPlayer(player);

        ssi.checkMau(spiel, true);
        assertEquals(3, player.getKarten().size());
    }

    @Test
    public void mauWirdGesagt() throws KeineKartenMehr {
        Spiel spiel = new Spiel();
        PlayerImpl player = new PlayerImpl();

        player.getKarten().add(new Karte());
        player.getKarten().add(new Karte());
        spiel.getZiehStapel().add(new Karte());
        spiel.setAktuellerPlayer(player);
        when(regel.hatPlayerMauRichtigGesagt(any(), anyBoolean())).thenReturn(true);

        ssi.checkMau(spiel, true);
        assertEquals(2, player.getKarten().size());
    }

    @Test
    public void getLetzteAblageStapelKarte() {
        Spiel spiel = new Spiel();
        List<Karte> ablageStapel = new ArrayList<>();
        ablageStapel.add(new Karte());

        spiel.setAblageStapel(ablageStapel);
        ssi.getLetzteAblageStapelKarte(spiel);
    }

    @Test
    public void playerMussZweiZiehen() throws KeineKartenMehr {
        Spiel spiel = new Spiel();
        PlayerImpl player = new PlayerImpl();
        List<Karte> ziehStapel = new ArrayList<>(Arrays.asList(new Karte(), new Karte()));

        spiel.setAktuellerPlayer(player);
        spiel.setZiehStapel(ziehStapel);
        when(regel.mussZiehen(any())).thenReturn(2);
        ssi.playerMussZiehen(spiel);

        assertEquals(2, player.getKarten().size());

    }

    @Test
    public void karteIstGuetig() {
        Karte karte = new Karte();
        Spiel spiel = new Spiel();

        spiel.setGueltigeKarte(karte);
        when(regel.gueltigeKarte(any(), any())).thenReturn(true);

        assertTrue(ssi.karteGueltig(karte, spiel));

    }

    @Test
    public void karteIstNichtGuetig() {
        Spiel spiel = mock(Spiel.class);
        Karte karte = new Karte();
        when(spiel.getGueltigeKarte()).thenReturn(karte);
        when(spiel.getRegel()).thenReturn(new ArrayList<>());
        when(regel.gueltigeKarte(any(), any())).thenReturn(false);
        assertFalse(ssi.karteGueltig(karte, spiel));
    }

    @Test
    public void resetSpielTest() throws AnzahlSpielerException, KeineKartenMehr {
        Spiel spiel = new Spiel();
        PlayerImpl player = new PlayerImpl();
        PlayerImpl player2 = new PlayerImpl();
        spiel.setZiehStapel(cardFactory.createNewCardSet());
        ssi.neuerSpieler(spiel, player);
        ssi.neuerSpieler(spiel, player2);
        player.getKarten().add(ssi.zieheKarteVonZiehstapel(spiel));
        player.getKarten().add(ssi.zieheKarteVonZiehstapel(spiel));
        player.getKarten().add(ssi.zieheKarteVonZiehstapel(spiel));
        player2.getKarten().add(ssi.zieheKarteVonZiehstapel(spiel));
        player2.getKarten().add(ssi.zieheKarteVonZiehstapel(spiel));
        player2.getKarten().add(ssi.zieheKarteVonZiehstapel(spiel));
        player2.getKarten().add(ssi.zieheKarteVonZiehstapel(spiel));

        assertEquals(25, spiel.getZiehStapel().size());
        ssi.resetSpiel(spiel);
        assertEquals(21, spiel.getZiehStapel().size());

    }

    @Test
    public void naechsterSpielerTest() throws AnzahlSpielerException {
        Spiel spiel = new Spiel();
        PlayerImpl player = new PlayerImpl();
        PlayerImpl player2 = new PlayerImpl();
        spiel.setRegel(new ArrayList<>());
        ssi.neuerSpieler(spiel, player);
        ssi.neuerSpieler(spiel, player2);
        ssi.naechsterSpieler(spiel);
    }

}
