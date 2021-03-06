package maumau.regel.implRegel.verhalten;

import maumau.interPlayer.IPlayer;
import maumau.regel.interRegel.verhalten.IMauVerhalten;
import org.springframework.stereotype.Component;

@Component("noneMauMau")
public class KeinMauVerhalten implements IMauVerhalten {

    @Override
    public boolean hatRichtigMauGesagt(IPlayer player, boolean maugesagt) {
        return true;
    }
}
