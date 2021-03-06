package maumau.spiel.model.interSpiel.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.regel.interRegel.module.RegelType;

/**
 * Das Spiel steuert das Spielgeschehen
 */
@Data
@Entity
public class Spiel {

    /**
     * Eindeutiger Identifier fuer die Datenbank
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Am Spiel teilnehmende Spieler
     */
    @OneToMany(cascade = {CascadeType.ALL})
    private List<IPlayer> players;

    /**
     * Spieler der an der Reihe ist
     */
    @OneToOne(cascade = {CascadeType.ALL})
    private IPlayer aktuellerPlayer;

    /**
     * Kartenstapel von dem neue Karten gezogen werden können
     */
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Karte> ziehStapel;

    /**
     * Kartenstapel auf dem die Karten abgelegt werden. Sollte der Ziehstapel leer sein werden die
     * Karten vom Ablagestapel in den Nachziehstapel gemischt
     */
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Karte> ablageStapel;

    /**
     * Die Karte mit der verglichen wird ob die gespielte Karte gueltig ist
     */
    @OneToOne(cascade = {CascadeType.ALL})
    private Karte gueltigeKarte;

    /**
     * Die letzten gueltigen gespielten Karten
     */
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Karte> aktiveKarten;

    /**
     * Die Regeln nach denen gespielt wird
     */
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<RegelType> regel;

    /**
     * Gibt die Richtung an in der gespielt wird. 1 für rechtsherum -1 für linksherum
     */

    private int richtung;

    public Spiel() {
        this.ziehStapel = new ArrayList<>();
        this.ablageStapel = new ArrayList<>();
        this.aktiveKarten = new ArrayList<>();
        this.players = new ArrayList<>();
        this.richtung = 1;
    }

}
