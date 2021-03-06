package maumau.implPlayer;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.persistence.Entity;
import maumau.interPlayer.Computer;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;

/**
 * Der einfache Computer Gegner.
 * <p>
 * Mau: Er vergisst zu 15% mau zu sagen und sagt zu nie mau obwohl er es nicht sagen soll.
 * <p>
 * Kartenlegen: <br> SpielstandsIndikator 3: versucht eine 7, 8 oder Ass zu legen <br>
 * SpielstandsIndikator 2: Spart sich seine Buben auf, kann auch 7,8 oder Ass liegen <br>
 * SpielstandsIndikator 1: versucht eine zufaellige Karte zu legen <br>
 * <p>
 * Wunsch: Waehlt eine Kartenfarbe, die er noch auf der Hand hat, aber kein Bube ist.
 */
@Entity
public class ComputerMittel extends Computer {

    private static final List<KartenWert> GEWUENSCHT_INDICATOR3 = List
        .of(KartenWert.WERT7, KartenWert.WERT8, KartenWert.WERTAss);
    private static final List<KartenWert> UNGEWUENSCHT_INDICATOR2 = List.of(KartenWert.WERTBube);


    public ComputerMittel(String name) {
        super(name);

    }

    public ComputerMittel() {
        super();
    }

    @Override
    public List<Karte> autoSelectCard(int spielstandsIndikator) {
        if (spielstandsIndikator == 3) {
            return listeVerfuegbarerKartenMitWerten(GEWUENSCHT_INDICATOR3);
        }
        if (spielstandsIndikator == 2) {
            return listeVerfuegbarerKartenAusser(UNGEWUENSCHT_INDICATOR2);
        }
        if (spielstandsIndikator == 1) {
            return this.karten;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean mussMauSagen() {
        if (this.karten.size() == 2) {
            if (new Random().nextInt(100) < 15) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String farbewuenschen() {
        for (Karte k : this.karten) {
            if (k.getWert() != KartenWert.WERTBube) {
                return k.getFarbe().getFarbe();
            }
        }
        return KartenFarbe.values()[new Random().nextInt(4)].getFarbe();
    }
}
