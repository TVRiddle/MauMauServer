package maumau.regel.implRegel.verhalten;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import org.junit.Before;
import org.junit.Test;

public class NormalGueltigVerhaltenTest {

    NormalGueltigVerhalten normalGueltigVerhalten;

    @Before
    public void init() {
        normalGueltigVerhalten = new NormalGueltigVerhalten();
    }

    @Test
    public void wennGelegteKarteHatAnderenWertUndAndereFarbe() {
        Karte liegendeKarte = new Karte(KartenFarbe.HERZ, KartenWert.WERT8);
        Karte gelegteKarte = new Karte(KartenFarbe.KARO, KartenWert.WERT7);

        assertFalse(normalGueltigVerhalten.isGueltig(liegendeKarte, gelegteKarte));
    }

    @Test
    public void nullKarteGueltig() {
        assertTrue(
            normalGueltigVerhalten.isGueltig(new Karte(KartenFarbe.HERZ, KartenWert.WERT9), null));
    }

    @Test
    public void wertKarteGueltig() {
        Karte liegendeKarte = new Karte(KartenFarbe.HERZ, KartenWert.WERT9);
        Karte gelegteKarte = new Karte(KartenFarbe.PIK, KartenWert.WERT9);
        assertTrue(normalGueltigVerhalten.isGueltig(liegendeKarte, gelegteKarte));
    }

    @Test
    public void farbeKarteGueltig() {
        Karte liegendeKarte = new Karte(KartenFarbe.KARO, KartenWert.WERT8);
        Karte gelegteKarte = new Karte(KartenFarbe.KARO, KartenWert.WERT7);
        assertTrue(normalGueltigVerhalten.isGueltig(liegendeKarte, gelegteKarte));
    }

    @Test
    public void wennKartePasstNichtAberWertIstBube() {
        Karte liegendeKarte = new Karte(KartenFarbe.KARO, KartenWert.WERT8);
        Karte gelegteKarte = new Karte(KartenFarbe.KREUZ, KartenWert.WERTBube);
        assertTrue(normalGueltigVerhalten.isGueltig(liegendeKarte, gelegteKarte));
    }
}
