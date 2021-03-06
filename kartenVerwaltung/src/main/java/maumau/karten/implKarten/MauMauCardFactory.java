package maumau.karten.implKarten;

import java.util.ArrayList;
import java.util.List;
import maumau.karten.interKarten.ICardFactory;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;

/**
 * Stellt eine Fabrik dar die einem ein Sett fuer ein MauMauSpiel zur Verfuegung stellt
 */
public class MauMauCardFactory implements ICardFactory {

    @Override
    public List<Karte> createNewCardSet() {
        List<Karte> karten = new ArrayList<>();
        for (KartenFarbe kf : KartenFarbe.values()) {
            for (KartenWert kw : KartenWert.values()) {
                karten.add(new Karte(kf, kw));
            }
        }
        return karten;
    }
}
