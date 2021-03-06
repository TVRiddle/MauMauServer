package maumau.regel.implRegel.verhalten;

import java.util.List;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.Karte;
import maumau.regel.interRegel.verhalten.INaechsterSpielerVerhalten;
import org.springframework.stereotype.Component;

@Component("noneNaechster")
public class KeineSonderregelNaechsterSpieler implements INaechsterSpielerVerhalten {

    @Override
    public IPlayer bestimmeNaechstenSpieler(List<IPlayer> spieler,
        IPlayer aktuellerSpieler, Karte aktuelleKarte, int richtung) {
        return getRegulaerenNaechstenSpieler(spieler, aktuellerSpieler, richtung);
    }

}
