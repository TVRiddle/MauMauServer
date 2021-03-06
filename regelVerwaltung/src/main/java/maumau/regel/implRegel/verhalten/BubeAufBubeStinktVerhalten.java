package maumau.regel.implRegel.verhalten;

import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenWert;
import maumau.regel.interRegel.verhalten.IGueltigeKarteVerhalten;
import org.springframework.stereotype.Component;

@Component("bubeBube")
public class BubeAufBubeStinktVerhalten implements IGueltigeKarteVerhalten {

    @Override
    public boolean precheck(Karte letzteKarte, Karte gespielteKarte) {
        if (letzteKarte.getWert() == KartenWert.WERTBube
            && gespielteKarte.getWert() == KartenWert.WERTBube) {
            return false;
        }
        return true;
    }
}
