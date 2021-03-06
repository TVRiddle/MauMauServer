package maumau.regel.interRegel.verhalten;

import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenWert;
import org.springframework.stereotype.Component;

/**
 * Interface fuer ein Spielverhalten. Prueft ob eine Karte gueltig ist
 */
@Component
public interface IGueltigeKarteVerhalten {

    /**
     * Normales Pruefen ob die Karte gueltig ist
     *
     * @param letzteKarte    die Karte auf dem Ablagestapel
     * @param gespielteKarte die vom Player gespielte Karte
     * @return ob gueltig <code>true</code> oder nicht <code>false</code>
     */
    default boolean isGueltig(Karte letzteKarte, Karte gespielteKarte) {

        if (!precheck(letzteKarte, gespielteKarte)) {
            return false;
        }
        if (gespielteKarte == null) {
            return true;
        }
        if (letzteKarte.getWert().equals(gespielteKarte.getWert())) {
            return true;
        }
        if (letzteKarte.getFarbe().equals(gespielteKarte.getFarbe())) {
            return true;
        }
        if (gespielteKarte.getWert().equals(KartenWert.WERTBube)) {
            return true;
        }
        return false;
    }

    /**
     * Zu implementierende Funktion fuer eventuelle Sonderregeln
     *
     * @param letzteKarte    die Karte auf dem Ablagestapel
     * @param gespielteKarte die vom Player gespielte Karte
     * @return ob gueltig <code>true</code> oder nicht <code>false</code>
     */
    boolean precheck(Karte letzteKarte, Karte gespielteKarte);

}
