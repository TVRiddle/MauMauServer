package maumau.karten.interKarten.module;

/**
 * Definiert welche Werte eine Karte annehmen kann
 */
public enum KartenWert {
    WERT7("7"),
    WERT8("8"),
    WERT9("9"),
    WERT10("10"),
    WERTBube("Bube"),
    WERTDame("Dame"),
    WERTKoenig("König"),
    WERTAss("Ass");

    private final String wert;

    private KartenWert(String wert) {
        this.wert = wert;
    }

    public String getWert() {
        return this.wert;
    }
}
