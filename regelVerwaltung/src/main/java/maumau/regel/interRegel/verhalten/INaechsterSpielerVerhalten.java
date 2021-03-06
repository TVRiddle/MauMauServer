package maumau.regel.interRegel.verhalten;

import java.util.List;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import org.springframework.stereotype.Component;

/**
 * Interface fuer ein Spielverhalten. Prueft welches der naechste Player ist
 */
@Component
public interface INaechsterSpielerVerhalten {

    IPlayer bestimmeNaechstenSpieler(List<IPlayer> spieler, IPlayer aktuellerSpieler,
        Karte aktuelleKarte, int richtung);

    default IPlayer getRegulaerenNaechstenSpieler(List<IPlayer> spieler,
        IPlayer aktuellerSpieler, int richtung) {
        if (aktuellerSpieler == null) {
            return spieler.get(0);
        } else {
            int index = 0;
            for (IPlayer p : spieler) {
                if (p.getName().equals(aktuellerSpieler.getName())) {
                    break;
                }
                index += 1;
            }
            // Das geht irgendwie nicht, warum auch immer sagt er immer dass gleich der erste passt
            //int index = spieler.indexOf(aktuellerSpieler);
            int indexNaechsterSpieler = index + richtung;
            if (indexNaechsterSpieler == spieler.size()) {
                indexNaechsterSpieler = 0;
            }
            if (indexNaechsterSpieler == spieler.size() + 1) {
                indexNaechsterSpieler = 1;
            }
            if (indexNaechsterSpieler == -1) {
                indexNaechsterSpieler = spieler.size() - 1;
            }
            if (indexNaechsterSpieler == -2) {
                indexNaechsterSpieler = spieler.size() - 2;
            }
            return spieler.get(indexNaechsterSpieler);
        }
    }

}
