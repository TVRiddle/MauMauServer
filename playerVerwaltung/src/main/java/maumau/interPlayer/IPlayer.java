package maumau.interPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import maumau.karten.interKarten.module.Karte;

/**
 * Der menschliche Akteur im Spiel
 */
@Entity
@Getter
@Setter
public abstract class IPlayer {

    /**
     * Eindeutiger Identifier fuer die Datenbank
     */
    @Id
    @GeneratedValue
    protected Integer id;

    /**
     * Name der im Spiel angezeigt wird
     */
    protected String name;

    /**
     * Die Handkarten des Spielers
     */
    @OneToMany(cascade = {CascadeType.ALL})
    protected List<Karte> karten;

    /**
     * Eine id, die als cookie gesendet wird um den Player eindeutig zu identifizieren.
     */
    protected String cookieId;


    public IPlayer(String name) {
        this.name = name;
        this.karten = new ArrayList<>();
    }

    public IPlayer() {
        this.karten = new ArrayList<>();
    }

    /**
     * Gibt eine Liste aller Karten zurueck die egal nach welchen Regeln gespielt wird, immmer
     * gelegt werden kann
     *
     * @param aktuelleKarte
     * @return
     */
    protected List<Karte> immerPassendeKarte(Karte aktuelleKarte) {
        List<Karte> moeglicheKarten = this.karten.parallelStream().filter(k -> {
            if (k.getFarbe() == aktuelleKarte.getFarbe() || k.getWert() == aktuelleKarte
                .getWert()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return moeglicheKarten;
    }

}
