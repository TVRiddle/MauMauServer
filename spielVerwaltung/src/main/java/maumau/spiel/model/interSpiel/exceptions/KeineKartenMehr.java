package maumau.spiel.model.interSpiel.exceptions;

/**
 * Wird geworfen, wenn keine Karten mehr auf dem Nachziehstapel zur Verfuegung sind
 */
public class KeineKartenMehr extends Throwable {

    public KeineKartenMehr(String msg) {
        super(msg);
    }
}
