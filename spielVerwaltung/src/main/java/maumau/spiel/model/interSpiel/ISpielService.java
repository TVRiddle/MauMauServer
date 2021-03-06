package maumau.spiel.model.interSpiel;

import java.util.List;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.regel.interRegel.module.RegelType;
import maumau.spiel.model.interSpiel.exceptions.AnzahlSpielerException;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;

/**
 * Interface fuer einen Service der Opperationen und Veraenderungen am Spiel ausfuehrt
 */
public interface ISpielService {

    /**
     * E
     *
     * @param anzahlSpieler Anzahl wie viele Spieler am Spiel teilnehmen
     * @param regeln        Liste aller gewaehlten Regeln
     * @return Spiel neues Spiel
     * @throws AnzahlSpielerException geworfen wenn zu viele Spieler fuer die menge Karten gewaehlt
     *                                werden
     */
    /**
     * Erstellt ein Spielobjekt und gibt dieses zurueck (new Spiel)
     *
     * @param spielername     Name des Spielers der das Spiel eroeffnet
     * @param anzahlSpieler   Anzahl wie viele Spieler am Spiel teilnehmen
     * @param computerEinfach Anzahl wie viele einfache Computer am Spiel teilnehmen
     * @param computerMittel  Anzahl wie viele mittlere Computer am Spiel teilnehmen
     * @param computerSchwer  Anzahl wie viele schwere Computer am Spiel teilnehmen
     * @param regeln          Liste aller gewaehlten Regeln
     * @return neues Spiel
     * @throws AnzahlSpielerException geworfen wenn zu viele Spieler fuer die menge Karten gewaehlt
     *                                *                                werden
     */
    Spiel erzeugeSpiel(String spielername, int anzahlSpieler, int computerEinfach,
        int computerMittel, int computerSchwer, List<RegelType> regeln)
        throws AnzahlSpielerException;


    /**
     * Fuegt neuen Player dem Spiel hinzu
     *
     * @param spiel  aktuelles Spiel
     * @param player der hinzugefuegt werden soll
     * @throws AnzahlSpielerException geworfen wenn mehr als zulaessig Player einem Spiel
     *                                hinzugefuegt werden
     */
    void neuerSpieler(Spiel spiel, IPlayer player) throws AnzahlSpielerException;

    /**
     * Vom noch vollstaendigen Ziehstapel werden x Karten an alle Player verteilt
     *
     * @param spiel aktuelles Spiel
     * @throws AnzahlSpielerException geworfen wenn nur ein Player im Spiel
     * @throws KeineKartenMehr        geworfen wenn keine Karten mehr im Nachziehstapel sind
     */
    void kartenVerteilen(Spiel spiel) throws AnzahlSpielerException, KeineKartenMehr;

    /**
     * 1 Karte von Ziehstapel in letzteAblageStapelKarte packen
     *
     * @param spiel aktuelles Spiel
     * @throws KeineKartenMehr geworfen wenn keine Karten mehr im Nachziehstapel sind
     */
    void spielVorbereiten(Spiel spiel) throws KeineKartenMehr;

    /**
     * Ermittelt den naechten Player der an der Reihe ist
     *
     * @param spiel aktuelles Spiel
     * @return Player der als nachster an der Reihe ist
     */
    IPlayer naechsterSpieler(Spiel spiel);

    /**
     * Spiel kann noch weiter gespielt werden (Mehr als ein Spieler im Spiel), oder Spiel nicht
     * hinreichend vorbereitet (Keine Karte auf dem Ablagestapel, oder Spieler haben keien Karten)
     *
     * @param spiel das geprueft werden soll
     * @return ob das spiel weiter gespielt werden kann
     */
    boolean istSpielLaufend(Spiel spiel);

    /**
     * Nimmt eine gespielte Karte prüft auf Gueltigkeit und verteilt sie auf die entsprechenden
     * Stapel
     *
     * @param spiel das aktuelle Spiel
     * @param karte die zu spielende Karte
     */
    void karteSpielen(Spiel spiel, Karte karte);

    /**
     * Gibt eine Karte vom Nachziehstapel zurueck. Sollte keine mehr vorhanden sein, so wird der
     * Ablagestapel neu gemischt
     *
     * @param spiel aktuelles Spiel
     * @return eine Karte vom Ablagestapel
     * @throws KeineKartenMehr geworfen wenn keine Karten mehr im Nachziehstapel sind
     */
    Karte zieheKarteVonZiehstapel(Spiel spiel) throws KeineKartenMehr;

    /**
     * Mischt alle Karten im Nachziehstapel
     *
     * @param spiel aktuelles Spiel
     */
    void ziehstapelMischen(Spiel spiel);

    /**
     * Zuletzt gelegte Karte um die Gültigkeit einer Ablage zu verifizieren
     *
     * @param spiel aktuelles Spiel
     * @return letzte Karte des Ablagestapel
     */
    Karte getLetzteAblageStapelKarte(Spiel spiel);

    /**
     * Laesst den aktuellen Player so viele Karten ziehen wie er muss (kann auch 0 betragen)
     *
     * @param spiel aktuelles Spiel
     * @return Menge der Karten die gezogen werden muss
     * @throws KeineKartenMehr geworfen wenn keine Karten mehr im Nachziehstapel sind
     */
    int playerMussZiehen(Spiel spiel) throws KeineKartenMehr;

    /**
     * Der Service prueft ob die vom Player gewaehlte Karte gueltig ist
     *
     * @param karte gespielte Karte
     * @param spiel aktuelles Spiel
     * @return ist gueltig oder nicht
     */
    boolean karteGueltig(Karte karte, Spiel spiel);

    /**
     * Check ob Player sich eine Farbe wuenschen darf und setzt diese.
     *
     * @param spiel aktuelles Spiel
     * @param karte die vom Player gespielte Karte
     * @return die Karte mit den Attributen, die sich der Player gewuenscht hat
     */
    boolean wuenschen(Spiel spiel, Karte karte);

    /**
     * Schaut ob Spieler noch mau gesagt hat und laesst ihn eine Karte ziehen, wenn er es getan hat
     * und mehr als eine Karte hat
     *
     * @param spiel     aktuelles Spiel
     * @param maugesagt gibt an ob Player "mau" gesagt hat
     * @throws KeineKartenMehr geworfen wenn keine Karten mehr vorhanden sind die der Spieler ziehen
     *                         koennte
     */
    boolean checkMau(Spiel spiel, boolean maugesagt) throws KeineKartenMehr;

    /**
     * Startet eine neue Runde dieses Spiels
     *
     * @param spiel aktuelles Spiel
     * @return ein Spiel, dass von vorn anfangen kann * @throws KeineKartenMehr geworfen wenn keine
     * Karten mehr vorhanden sind die der Spieler ziehen koennte
     */
    Spiel resetSpiel(Spiel spiel) throws KeineKartenMehr;

    /**
     * Laesst den aktuellen Spieler eine Karte vom Nachziestapel ziehen und setzt die aktiven Karten
     * auf null
     *
     * @param spiel aktuelles Spiel
     * @throws KeineKartenMehr geworfen wenn keine Karten mehr vorhanden sind die der Spieler ziehen
     *                         koennte
     */
    void spielerZiehtKarte(Spiel spiel) throws KeineKartenMehr;

    /**
     * Laesst einen Computer eine gueltige Karte auswaehlen
     *
     * @param spiel aktuelles Spiel
     * @return valide kartenid
     */
    Long lasseComputerKarteWaehlen(Spiel spiel);
}
