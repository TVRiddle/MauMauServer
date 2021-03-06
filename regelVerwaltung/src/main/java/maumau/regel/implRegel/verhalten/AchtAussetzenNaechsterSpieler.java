package maumau.regel.implRegel.verhalten;

import java.util.List;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenWert;
import maumau.regel.interRegel.verhalten.INaechsterSpielerVerhalten;
import org.springframework.stereotype.Component;

@Component("achtAussetzen")
public class AchtAussetzenNaechsterSpieler implements INaechsterSpielerVerhalten {

    @Override
    public IPlayer bestimmeNaechstenSpieler(List<IPlayer> spieler,
        IPlayer aktuellerSpieler, Karte aktuelleKarte, int richtung) {
        if (aktuelleKarte != null) {
            if (aktuelleKarte.getWert().equals(KartenWert.WERT8)) {
                richtung *= 2;
            }
        }
        return getRegulaerenNaechstenSpieler(spieler, aktuellerSpieler, richtung);
    }
}
