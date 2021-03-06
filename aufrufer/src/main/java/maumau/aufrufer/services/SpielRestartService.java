package maumau.aufrufer.services;

import lombok.extern.slf4j.Slf4j;
import maumau.spiel.model.interSpiel.ISpielService;
import maumau.spiel.model.interSpiel.LogEntryRepository;
import maumau.spiel.model.interSpiel.SpielRepository;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpielRestartService {

    @Autowired
    ISpielService spielService;
    @Autowired
    SpielRepository spielRepository;
    @Autowired
    LogEntryRepository logEntryRepository;

    /**
     * Setzt ein Spiel zurueck und verteilt die Karten neu
     *
     * @param spielId des Spiels, das neu gestartet werden soll
     * @return
     * @throws KeineKartenMehr
     */
    public Spiel restart(Long spielId) throws KeineKartenMehr {
        Spiel spiel = spielService.resetSpiel(spielRepository.getOne(spielId));
        logEntryRepository.updateLogRunde(spielId);
        log.debug("Spiel wird gespeichert");
        spielRepository.save(spiel);
        return spiel;
    }

}
