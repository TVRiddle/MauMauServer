package maumau.karten.interKarten;

import java.util.List;
import maumau.karten.interKarten.module.Karte;

/**
 * Interface fuer eine Fabrik, die einem ein Sett fuer ein bestimmtes Kartenspiel zur Verfuegung
 * stellt
 */
public interface ICardFactory {

    /**
     * Liefert ein komplettes KartenSet
     *
     * @return eine Liste an Spielkarten
     */
    List<Karte> createNewCardSet();
}
