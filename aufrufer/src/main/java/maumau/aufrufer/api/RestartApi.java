package maumau.aufrufer.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.aufrufer.services.SpielAblaufService;
import maumau.aufrufer.services.SpielRestartService;
import maumau.spiel.model.interSpiel.LogEntryRepository;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RestartApi {

    private final SpielRestartService spielRestartService;
    private final SpielAblaufService spielAblaufService;
    private final LogEntryRepository logEntryRepository;

    /**
     * Gibt den Spielern die Moeglichkeit ein Spiel mit den gleichen Regeln neu zu starten
     *
     * @param spielId des Spiels, das neu gestartet werden soll
     * @return Ansicht des neugestarteten Spiels
     * @throws KeineKartenMehr kann an dieser Stelle nicht geworfen werden
     */
    @PostMapping(value = {"/restart"})
    public ModelAndView restart(
        @RequestParam("spielId") Long spielId) throws KeineKartenMehr {
        Spiel spiel = spielRestartService.restart(spielId);
        ModelAndView modelAndView = new ModelAndView("spiel");
        modelAndView.addObject("command",
            spiel.getAktuellerPlayer().getName() + " muss eine gültige Karte spielen");
        modelAndView.addObject("spiel", spiel);
        log.info("Spiel wurde neu gestartet: {}", spiel.getId());
        return modelAndView;
    }

    @GetMapping(value = {"/restart/{spielId}"})
    public ModelAndView catchOtherPlayers(@PathVariable Long spielId) {
        ModelAndView modelAndView = new ModelAndView("restartGame");
        modelAndView.addObject("spielId", spielId);
        modelAndView.addObject("spiel", spielAblaufService.getSpiel(spielId));
        modelAndView
            .addObject("log",
                logEntryRepository.findAllBySpielIdAndRundeIsNullOrderByTimestampDesc(spielId));
        return modelAndView;
    }
}
