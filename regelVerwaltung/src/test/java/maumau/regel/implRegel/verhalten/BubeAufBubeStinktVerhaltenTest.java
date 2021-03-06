package maumau.regel.implRegel.verhalten;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import org.junit.Before;
import org.junit.Test;

public class BubeAufBubeStinktVerhaltenTest {

    BubeAufBubeStinktVerhalten bubeAufBubeStinktVerhalten;

    @Before
    public void init() {
        bubeAufBubeStinktVerhalten = new BubeAufBubeStinktVerhalten();
    }

    @Test
    public void bubeAufBubeStinkt() {
        Karte k1 = new Karte(KartenFarbe.KARO, KartenWert.WERTBube);
        Karte k2 = new Karte(KartenFarbe.HERZ, KartenWert.WERTBube);

        assertFalse(bubeAufBubeStinktVerhalten.isGueltig(k1, k2));
    }

    @Test
    public void normaleKarteAufBubeTrueErwaret() {
        Karte k1 = new Karte(KartenFarbe.KARO, KartenWert.WERTBube);
        Karte k2 = new Karte(KartenFarbe.KARO, KartenWert.WERTKoenig);

        assertTrue(bubeAufBubeStinktVerhalten.isGueltig(k1, k2));
    }
}
