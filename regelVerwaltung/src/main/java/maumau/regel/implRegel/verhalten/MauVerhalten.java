package maumau.regel.implRegel.verhalten;

import maumau.interPlayer.IPlayer;
import maumau.regel.interRegel.verhalten.IMauVerhalten;
import org.springframework.stereotype.Component;

@Component("mauMau")
public class MauVerhalten implements IMauVerhalten {


    @Override
    public boolean hatRichtigMauGesagt(IPlayer player, boolean maugesagt) {
        if (player.getKarten().size() != 2 && maugesagt) {
            return false;
        }
        if (!maugesagt && player.getKarten().size() == 2) {
            return false;
        }
        return true;
    }
}
