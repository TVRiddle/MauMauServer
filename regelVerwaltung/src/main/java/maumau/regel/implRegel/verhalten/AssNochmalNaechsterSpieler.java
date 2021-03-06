package maumau.regel.implRegel.verhalten;

import java.util.List;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenWert;
import maumau.regel.interRegel.verhalten.INaechsterSpielerVerhalten;
import org.springframework.stereotype.Component;

@Component("assNochmal")
public class AssNochmalNaechsterSpieler implements INaechsterSpielerVerhalten {

    @Override
    public IPlayer bestimmeNaechstenSpieler(List<IPlayer> spieler,
        IPlayer aktuellerSpieler, Karte aktuelleKarte, int richtung) {
        if (aktuelleKarte != null) {
            if (aktuelleKarte.getWert().equals(KartenWert.WERTAss)) {
                return aktuellerSpieler;
            }
        }
        return getRegulaerenNaechstenSpieler(spieler, aktuellerSpieler, richtung);
    }

}
