package maumau.interPlayer;

import java.util.List;
import java.util.stream.Collectors;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;

public abstract class Computer extends IPlayer {

    public Computer(String name) {
        super(name);
        this.cookieId = "foo";
    }

    public Computer() {
        super();
        this.cookieId = "foo";
    }

    /**
     * Gibt eine Auswahl an Karten die der Computer legen wollen wuerde gemessen an seinem
     * Schwierigkeitsgrad zurueck.
     *
     * @param spielstandsIndikator Wert von 0 - 3, 3 bedeutet liegt stark zurueck, 2 liegt zwar
     *                             zurueck aber andere Spieler haben noch genug Karten, 1 liegt
     *                             gleichauf oder fuehrt, 0 soll nur eine moeglichst passende Karte
     *                             gespielt werden
     * @return Liste aller Karten die der Computer gemaeß seines Algorythmus legen wollen wuerde
     */
    public abstract List<Karte> autoSelectCard(int spielstandsIndikator);

    /**
     * Entscheidet ob der Computer mau sagt
     *
     * @return
     */
    public abstract boolean mussMauSagen();

    /**
     * Gibt eine Farbe zurueck die ein Computer gemessen an seinem Schwierigkeitsgrad
     *
     * @return
     */
    public abstract String farbewuenschen();

    /**
     * Gibt eine Liste der Karten zurueck die auf der Hand des Computers sind und den KartenWerten
     * in der uebergebenen Liste entsprechen
     *
     * @param gewuenschteKartenWerte gewuenschte Kartenwerte
     * @return gefilterte Liste
     */
    protected List<Karte> listeVerfuegbarerKartenMitWerten(
        List<KartenWert> gewuenschteKartenWerte) {
        return this.karten.parallelStream()
            .filter(k -> gewuenschteKartenWerte.contains(k.getWert()))
            .collect(Collectors.toList());
    }

    /**
     * Gibt eine Liste der Karten zurueck die auf der Hand des Computers sind und den KartenWerten
     * in der uebergebenen Liste nicht entsprechen
     *
     * @param ungewuenschteKartenWerte ungewuenschte Kartenwerte
     * @return gefilterte Liste
     */
    protected List<Karte> listeVerfuegbarerKartenAusser(List<KartenWert> ungewuenschteKartenWerte) {
        return this.karten.parallelStream()
            .filter(k -> !ungewuenschteKartenWerte.contains(k.getWert()))
            .collect(Collectors.toList());
    }

    /**
     * Aus einer Liste von Karten werden nur die einer bestimmten angegebenen Farbe zurueckgegeben
     *
     * @param karten Liste an Karten aus denen gefiltert werden soll
     * @param farbe  die Farbe der Wahl
     * @return
     */
    protected List<Karte> listeVerfuegbartetKartenMitFrabe(List<Karte> karten, KartenFarbe farbe) {
        return karten.stream().filter(k -> k.getFarbe() == farbe).collect(Collectors.toList());
    }
}
