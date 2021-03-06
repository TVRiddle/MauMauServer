package maumau.regel.interRegel;

import java.util.List;
import lombok.Data;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.regel.interRegel.verhalten.IGueltigeKarteVerhalten;
import maumau.regel.interRegel.verhalten.IMauVerhalten;
import maumau.regel.interRegel.verhalten.INaechsterSpielerVerhalten;
import org.springframework.stereotype.Component;

@Component
@Data
public abstract class RegelServiceTemplate {

    /**
     * Bestimmt das Verhalten das gezeigt werden soll um den naechsten Spieler zurueckzugeben
     */
    private INaechsterSpielerVerhalten naechsterSpielerVerhalten;
    private IMauVerhalten mauVerhalten;
    private IGueltigeKarteVerhalten gueltigeKarteVerhalten;

    /**
     * Prueft ob Bedienkarte eine gueltige Karte ist
     *
     * @param letzteKarte    letzte Karte auf dem Ablagestapel
     * @param gespielteKarte die vom Spieler gespielte Karte
     * @return ob Karte gueltig
     */
    public boolean gueltigeKarte(Karte letzteKarte, Karte gespielteKarte) {
        return gueltigeKarteVerhalten.isGueltig(letzteKarte, gespielteKarte);
    }

    /**
     * Zeigt an ob Karte in den aktiveKartenStapel gelegt werden soll
     *
     * @param karte          die fragliche Karte
     * @param aktuelleKarten Liste aller bereits gespeicherten Karten
     * @return ob Karte fuer spaetere Verwendung gespeichert werden soll
     */
    abstract public boolean sollGespeichertWerden(Karte karte, List<Karte> aktuelleKarten);

    /**
     * Bestimmt den nächsten Spieler
     *
     * @param spieler          Liste aller noch im Spiel verbliebener Spieler
     * @param aktuellerSpieler der Spieler der aktuell an der Reihe ist
     * @param aktuelleKarte    die aktuell ausgelegte Karte
     * @param richtung         die aktuelle Spielrichtung im Spiel
     * @return den naechsten Player
     */
    abstract public IPlayer bestimmeNaechstenSpieler(List<IPlayer> spieler,
        IPlayer aktuellerSpieler, Karte aktuelleKarte, int richtung);

    /**
     * Bestimmt die Anzahl der Karten die gezogen werden muessen
     *
     * @param aktiveKarten alle aktiven Karten
     * @return Zahl der zu ziehenden Karten
     */
    abstract public int mussZiehen(List<Karte> aktiveKarten);

    /**
     * Laesst den Player sich gegebenenfalls etwas wuenschen
     *
     * @param karte die gepielte Karte
     * @return Die Karte die am Ende gueltig ist
     */
    abstract public boolean setGueltigeKarte(Karte karte);

    /**
     * Ueberprueft ob die Mausagenregel richtig eingehalten wurde
     *
     * @param player    der Spieler der dran war und "Mau" haette sagen muessen
     * @param maugesagt sagt an ob Spieler "Mau" gesagt hat
     * @return true = muss nicht ziehen, false = muss ziehen
     */
    abstract public boolean hatPlayerMauRichtigGesagt(IPlayer player, boolean maugesagt);
}
