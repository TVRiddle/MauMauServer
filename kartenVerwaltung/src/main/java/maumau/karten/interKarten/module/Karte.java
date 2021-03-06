package maumau.karten.interKarten.module;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

/**
 * Spielkarte mit Farbe und Wert die von den Spielern gespielt werden kann
 */
@Data
@Entity
public class Karte {

    /**
     * Eindeutiger Identifier fuer die Datenbank
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Gibt die Farbe der Karte an
     */
    @Enumerated(EnumType.STRING)
    private KartenFarbe farbe;

    /**
     * Gibt den Wert der Farbe an
     */
    @Enumerated(EnumType.STRING)
    private KartenWert wert;

    /**
     * Gibt eine concatinierte Version fuer Visualisierungszwecke aus
     */
    private String ausschrift;

    public Karte() {
    }

    public Karte(KartenFarbe farbe, KartenWert wert) {
        this.farbe = farbe;
        this.wert = wert;
        this.ausschrift = farbe.getFarbe() + " " + wert.getWert();
    }
}
