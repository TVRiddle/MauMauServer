package maumau.implPlayer;

import javax.persistence.Entity;
import lombok.Data;
import maumau.interPlayer.IPlayer;

/**
 * Der menschliche Akteur im Spiel
 */
@Data
@Entity
public class PlayerImpl extends IPlayer {

    public PlayerImpl(String name) {
        super(name);
    }

    public PlayerImpl() {
        super();
    }
}