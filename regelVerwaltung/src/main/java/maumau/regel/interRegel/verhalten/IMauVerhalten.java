package maumau.regel.interRegel.verhalten;


import maumau.interPlayer.IPlayer;
import org.springframework.stereotype.Component;

/**
 * Interface fuer ein Spielverhalten. Prueft ob ein User mau sagen muss oder nicht
 */
@Component
public interface IMauVerhalten {


    boolean hatRichtigMauGesagt(IPlayer player, boolean maugesagt);
}
