package maumau.aufrufer.services;

import maumau.spiel.model.interSpiel.SpielRepository;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpielLadenService {

    @Autowired
    SpielRepository spielRepository;

    /**
     * Gibt ein Spiel als Object zurueck, dass zu der angegebenen Id passt
     *
     * @param spielId des angefragten Spiels
     * @return
     */
    public Spiel spielLaden(Long spielId) {
        return spielRepository.getOne(spielId);
    }
}
