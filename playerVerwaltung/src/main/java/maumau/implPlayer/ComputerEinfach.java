package maumau.implPlayer;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.persistence.Entity;
import maumau.interPlayer.Computer;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;

/**
 * Der einfache Computer Gegner.
 * <p>
 * Mau: Er vergisst zu 30% mau zu sagen und sagt zu 10% mau obwohl er es nicht sagen soll.
 * <p>
 * Kartenlegen: Keine Strategie - zieht zu 10% ohne Grund eine Karte.
 * <p>
 * Wunsch: Beim Wuenschen waehlt er eine zufaellige Farbe.
 */
@Entity
public class ComputerEinfach extends Computer {

    public ComputerEinfach(String name) {
        super(name);
    }

    public ComputerEinfach() {
        super();
    }

    @Override
    public List<Karte> autoSelectCard(int spielstandsIndikator) {
        if (spielstandsIndikator == 0) {
            if (new Random().nextInt(100) < 10) {
                return Collections.emptyList();
            }
        }
        return this.karten;
    }

    @Override
    public boolean mussMauSagen() {
        if (this.karten.size() == 2) {
            if (new Random().nextInt(100) < 30) {
                return true;
            }
        }
        if (new Random().nextInt(100) < 10) {
            return true;
        }
        return false;
    }

    @Override
    public String farbewuenschen() {
        return KartenFarbe.values()[new Random().nextInt(4)].getFarbe();
    }

}
