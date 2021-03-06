package maumau.karten.interKarten.module;

import java.util.Arrays;

/**
 * Definiert welche Farben eine Karte haben kann
 */
public enum KartenFarbe {
    KARO("Karo"),
    HERZ("Herz"),
    PIK("Pik"),
    KREUZ("Kreuz");

    private final String farbe;

    private KartenFarbe(String farbe) {
        this.farbe = farbe;
    }

    public String getFarbe() {
        return this.farbe;
    }

    public static KartenFarbe getEnumByName(String name) {
        return Arrays.stream(KartenFarbe.values()).sequential()
            .filter(e -> e.getFarbe().equals(name))
            .findFirst().get();
    }

}
