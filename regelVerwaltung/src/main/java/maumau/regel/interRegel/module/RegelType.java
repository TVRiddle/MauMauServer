package maumau.regel.interRegel.module;

/**
 * Auflistung untschiedlicher Regeltypen
 */
public enum RegelType {

    ACHT_AUSSETZEN("achtAussetzen"),
    ASS_NOCHMAL("assNochmal"),
    BUBE_BUBE("bubeBube"),
    MAU_REGEL("mauRegel");

    private String name;

    RegelType(String name) {
        this.name = name;
    }

    /**
     * Gibt den Enum-Wert zu einem passenden String (namen) zurueck
     *
     * @param name des Enum-Wertes
     * @return Enum-Wert
     */
    public static RegelType getValueByName(String name) {
        for (RegelType regelType : RegelType.values()) {
            if (regelType.name.equalsIgnoreCase(name)) {
                return regelType;
            }
        }
        return null;
    }
}
