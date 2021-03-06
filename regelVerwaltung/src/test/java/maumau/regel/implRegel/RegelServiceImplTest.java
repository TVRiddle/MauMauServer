package maumau.regel.implRegel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import org.junit.Before;
import org.junit.Test;

public class RegelServiceImplTest {

    maumau.regel.interRegel.RegelServiceTemplate regel;

    @Before
    public void init() {
        regel = new RegelServiceImpl();
    }

    @Test
    public void karteSollNichtGespeichertWerden() {
        assertFalse(regel.sollGespeichertWerden(new Karte(KartenFarbe.HERZ, KartenWert.WERT9),
            Collections.emptyList()));
    }

    @Test
    public void nullWertSollNichtGespeichertWerden() {
        assertFalse(regel.sollGespeichertWerden(null,
            Collections.emptyList()));
    }

    @Test
    public void karteSollGespeichertWerden() {
        assertTrue(regel.sollGespeichertWerden(new Karte(KartenFarbe.HERZ, KartenWert.WERT7),
            Collections.emptyList()));
    }

    @Test
    public void anzahlZuZiehenderKartenBerechnen() {
        Karte k1 = new Karte(KartenFarbe.HERZ, KartenWert.WERT7);
        Karte k2 = new Karte(KartenFarbe.PIK, KartenWert.WERT7);
        Karte k3 = new Karte(KartenFarbe.KARO, KartenWert.WERT7);
        Karte k4 = new Karte(KartenFarbe.KREUZ, KartenWert.WERTAss);
        Karte k5 = new Karte(KartenFarbe.HERZ, KartenWert.WERTKoenig);
        List<Karte> kartenListe = Arrays.asList(k1, k2, k3, k4, k5);
        int kartenZuZiehen = regel.mussZiehen(kartenListe);
        assertEquals(6, kartenZuZiehen);
    }

    @Test
    public void wennKarteBubeDannFalse() {
        assertFalse(regel.setGueltigeKarte(new Karte(KartenFarbe.KARO, KartenWert.WERT9)));
    }

    @Test
    public void wennKarteBubeDannTrue() {
        assertTrue(regel.setGueltigeKarte(new Karte(KartenFarbe.KARO, KartenWert.WERTBube)));

    }

}
