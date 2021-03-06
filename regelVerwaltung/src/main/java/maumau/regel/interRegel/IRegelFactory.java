package maumau.regel.interRegel;

import java.util.List;
import maumau.regel.interRegel.module.RegelType;

/**
 * Interface fuer eine Regelfabrick.
 */
public interface IRegelFactory {

    /**
     * Erstellt eine Regeltemplate, dass Zuege validieren kann
     *
     * @param regeln Liste aller angewandten Regeln fuer ein Spiel
     * @return Regeltemplate
     */
    RegelServiceTemplate getRegelService(List<RegelType> regeln);
}
