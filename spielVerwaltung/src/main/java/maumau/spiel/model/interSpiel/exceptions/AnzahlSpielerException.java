package maumau.spiel.model.interSpiel.exceptions;

/**
 * Geworfen wenn zu viele oder zu wenige Player fuer ein Spiel gewaehlt worden
 */
public class AnzahlSpielerException extends Exception {

    public AnzahlSpielerException(String msg) {
        super(msg);
    }

}
