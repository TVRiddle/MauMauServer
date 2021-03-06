package maumau.regel.implRegel.verhalten;

import maumau.karten.interKarten.module.Karte;
import maumau.regel.interRegel.verhalten.IGueltigeKarteVerhalten;
import org.springframework.stereotype.Component;

@Component("normalGueltig")
public class NormalGueltigVerhalten implements IGueltigeKarteVerhalten {

    @Override
    public boolean precheck(Karte letzteKarte, Karte gespielteKarte) {
        return true;
    }
}
