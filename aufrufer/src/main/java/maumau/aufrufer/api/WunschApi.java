package maumau.aufrufer.api;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.aufrufer.exception.GameOverException;
import maumau.aufrufer.exception.IrregularBehaviourException;
import maumau.aufrufer.exception.WunschInterceptionException;
import maumau.aufrufer.services.SpielAblaufService;
import maumau.aufrufer.services.SpielLadenService;
import maumau.spiel.model.interSpiel.LogEntryRepository;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WunschApi {

    private final SpielAblaufService spielAblaufService;
    private final LogEntryRepository logEntryRepository;
    private final SpielLadenService spielLadenService;

    /**
     * Gibt dem User die Moeglichkeit sich eine Farbe zu wuenschen
     *
     * @param spielId     des Spieles, dass der User betreten will
     * @param kartenValue den Wert der gewuenschten Farbe
     * @return WunschView
     * @throws KeineKartenMehr             geworfen wenn eine Karte von Nachziehstapel genommen
     *                                     werden soll, dieser jedoch leer ist und auch keine
     *                                     Ablagekarten liegen
     * @throws IrregularBehaviourException Unerwartete Exception
     */
    @PostMapping(value = {"/wunsch"})
    public ModelAndView setNewValue(
        @RequestParam("spielID") Long spielId,
        @RequestParam("kartenValue") String kartenValue)
        throws KeineKartenMehr, IrregularBehaviourException {

        Spiel spiel;
        try {
            spiel = spielAblaufService.wunschKarteAnpassen(spielId, kartenValue);
        } catch (GameOverException e) {
            log.info("Das Spiel ist zu Ende");
            ModelAndView modelAndView = new ModelAndView("restartGame");
            modelAndView.addObject("spielId", spielId);
            modelAndView.addObject("spiel", spielLadenService.spielLaden(spielId));
            modelAndView
                .addObject("log",
                    logEntryRepository.findAllBySpielIdAndRundeIsNullOrderByTimestampDesc(spielId));
            return modelAndView;
        } catch (WunschInterceptionException e) {
            log.error("Unerwarteter Fehler aufgetreten");
            throw new IrregularBehaviourException("This shouldn't happened");
        }

        ModelAndView modelAndView = new ModelAndView("spiel");
        modelAndView.addObject("spiel", spiel);
        modelAndView.addObject("command",
            kartenValue + " wurde gewünscht. " + spiel.getAktuellerPlayer().getName()
                + " ist am Zug.");
        List<String> msg = logEntryRepository.logKurz(spiel.getId())
            .stream().map(e -> e.getMsg())
            .collect(Collectors.toList());
        modelAndView.addObject("log", msg);
        log.debug("Spiel laeuft: {}", spiel);
        return modelAndView;
    }
}
