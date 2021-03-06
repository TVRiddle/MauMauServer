package maumau.regel.implRegel;

import java.util.Arrays;
import java.util.List;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenWert;
import maumau.regel.interRegel.RegelServiceTemplate;

/**
 * Dies ist eine moegliche Implementierung der MauMauRegeln
 */
public class RegelServiceImpl extends RegelServiceTemplate {

    public static int STRAF_ZAHL = 2;
    private List<KartenWert> spezielleKarten = Arrays
        .asList(KartenWert.WERTAss, KartenWert.WERT7, KartenWert.WERT8);

    @Override
    public boolean sollGespeichertWerden(Karte karte, List<Karte> aktuelleListe) {
        if (karte == null) {
            return false;
        }
        return spezielleKarten.stream().filter(e -> e.equals(karte.getWert())).findFirst()
            .isPresent();
    }


    @Override
    public IPlayer bestimmeNaechstenSpieler(List<IPlayer> spieler,
        IPlayer aktuellerSpieler,
        Karte aktuelleKarte, int richtung) {

        return getNaechsterSpielerVerhalten()
            .bestimmeNaechstenSpieler(spieler, aktuellerSpieler, aktuelleKarte, richtung);
    }

    @Override
    public int mussZiehen(List<Karte> aktiveKarten) {
        return
            (int) aktiveKarten.stream().filter(ak -> ak.getWert().equals(KartenWert.WERT7)).count()
                * STRAF_ZAHL;
    }

    @Override
    public boolean setGueltigeKarte(Karte karte) {
        return karte.getWert().equals(KartenWert.WERTBube);
    }

    @Override
    public boolean hatPlayerMauRichtigGesagt(IPlayer player, boolean maugesagt) {
        return getMauVerhalten().hatRichtigMauGesagt(player, maugesagt);
    }
}
