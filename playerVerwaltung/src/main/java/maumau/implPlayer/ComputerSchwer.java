package maumau.implPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import maumau.interPlayer.Computer;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import org.apache.commons.lang3.StringUtils;

/**
 * Der einfache Computer Gegner.
 * <p>
 * Mau: Er vergisst nie mau zu sagen und sagt zu nie mau obwohl er es nicht sagen soll.
 * <p>
 * Kartenlegen: <br> SpielstandsIndikator 3: versucht eine 7, 8 oder Ass zu legen <br>
 * SpielstandsIndikator 2: versucht eine Karte zu legen von dessen Farbe er am meisten hat
 * außgenommen Buben <br> SpielstandsIndikator 1: versucht eine normale Karte zu legen </br>
 * <p>
 * Wunsch: Waehlt die Kartenfarbe, die er am meisten auf der Hand hat (Buben nicht mitgezaehlt).
 */
@Entity
public class ComputerSchwer extends Computer {

    private static final List<KartenWert> GEWUENSCHT_INDICATOR3 = List
        .of(KartenWert.WERT7, KartenWert.WERT8, KartenWert.WERTAss);
    private static final List<KartenWert> UNGEWUENSCHT_INDICATOR2 = List.of(KartenWert.WERTBube);
    private static final List<KartenWert> UNGEWUENSCHT_INDICATOR1 = List
        .of(KartenWert.WERT7, KartenWert.WERT8, KartenWert.WERTAss, KartenWert.WERTBube);

    public ComputerSchwer(String name) {
        super(name);
    }

    public ComputerSchwer() {
        super();
    }

    @Override
    public List<Karte> autoSelectCard(int spielstandsIndikator) {
        if (spielstandsIndikator == 3) {
            return listeVerfuegbarerKartenMitWerten(GEWUENSCHT_INDICATOR3);
        }
        if (spielstandsIndikator == 2) {
            List<Karte> kartenGefiltert = listeVerfuegbarerKartenAusser(UNGEWUENSCHT_INDICATOR2);
            KartenFarbe farbe = meistAufkommendeFarbe(kartenGefiltert);
            return listeVerfuegbartetKartenMitFrabe(kartenGefiltert, farbe);

        }
        if (spielstandsIndikator == 1) {
            return listeVerfuegbarerKartenAusser(UNGEWUENSCHT_INDICATOR1);
        }
        return this.karten;
    }

    @Override
    public boolean mussMauSagen() {
        if (this.karten.size() == 2) {
            return true;
        }
        return false;
    }

    @Override
    public String farbewuenschen() {
        String farbe = meistAufkommendeFarbe(listeVerfuegbarerKartenAusser(UNGEWUENSCHT_INDICATOR2))
            .getFarbe();
        if (StringUtils.isNotBlank(farbe)) {
            return farbe;
        }
        return KartenFarbe.values()[new Random().nextInt(4)].getFarbe();
    }

    private KartenFarbe meistAufkommendeFarbe(List<Karte> kartenGefiltert) {
        KartenFarbe farbe;
        Map<KartenFarbe, Integer> farbenHaeufigkeit = new HashMap<>();
        List<KartenFarbe> kartenFarben = kartenGefiltert.stream().map(k -> k.getFarbe())
            .collect(Collectors.toList());
        List.of(KartenFarbe.values())
            .forEach(f -> farbenHaeufigkeit.put(f, Collections.frequency(kartenFarben, f)));
        farbe = Collections
            .max(farbenHaeufigkeit.entrySet(),
                (Entry<KartenFarbe, Integer> e1, Entry<KartenFarbe, Integer> e2) -> e1.getValue()
                    .compareTo(e2.getValue())).getKey();
        return farbe;
    }
}
