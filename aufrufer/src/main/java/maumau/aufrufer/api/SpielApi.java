package maumau.aufrufer.api;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.aufrufer.exception.GameOverException;
import maumau.aufrufer.exception.WunschInterceptionException;
import maumau.aufrufer.model.WunschDto;
import maumau.aufrufer.services.SpielAblaufService;
import maumau.aufrufer.services.SpielLadenService;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.spiel.model.interSpiel.LogEntryRepository;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpielApi {

    private final SpielAblaufService spielAblaufService;
    private final LogEntryRepository logEntryRepository;
    private final SpielLadenService spielLadenService;

    /**
     * Schnittstelle fuer einen User der eine Karte spielen will
     *
     * @param spielId   des angefragten Spieles
     * @param karte     die der User spielen will
     * @param maugesagt zeigt an ob der User mau gesagt hat
     * @return View in den WarteView - wenn der Zug valide ist, RestartView - wenn das Spiel beendet
     * ist oder erneut den SpielView - wenn eine falsche Karte gespielt wurde
     * @throws KeineKartenMehr geworfen wenn eine Karte von Nachziehstapel genommen werden soll,
     *                         dieser jedoch leer ist und auch keine Ablagekarten liegen
     */
    @PostMapping(value = {"/spiel"})
    public ModelAndView evaluiereZug(
        @RequestParam("spielID") Long spielId,
        @RequestParam("karte") Long karte,
        @RequestParam(value = "mau") Optional<String> maugesagt,
        @CookieValue("PlayerCookieId") String playerCookieId) throws KeineKartenMehr {
        Spiel spiel;
        Integer aktuellerSpielerId;
        try {
            aktuellerSpielerId = spielLadenService.spielLaden(spielId).getAktuellerPlayer().getId();
            spiel = spielAblaufService.evaluiereZug(spielId, karte, maugesagt.isPresent());
        } catch (WunschInterceptionException e) {
            List<String> farbenListe = Arrays.asList(KartenFarbe.values()).stream()
                .map(KartenFarbe::getFarbe).collect(
                    Collectors.toList());
            WunschDto farbenDto = new WunschDto(spielId, farbenListe);
            ModelAndView modelAndView = new ModelAndView("wuenschen");
            modelAndView.addObject("farbenDto", farbenDto);
            modelAndView.addObject("spiel", spielLadenService.spielLaden(spielId));
            return modelAndView;
        } catch (GameOverException e) {
            log.info("Das Spiel ist zu Ende");
            ModelAndView modelAndView = new ModelAndView("restartGame");
            modelAndView.addObject("spielId", spielId);
            modelAndView.addObject("spiel", spielAblaufService.getSpiel(spielId));
            modelAndView
                .addObject("log",
                    logEntryRepository.findAllBySpielIdAndRundeIsNullOrderByTimestampDesc(spielId));
            return modelAndView;
        }
        ModelAndView modelAndView;
        if (aktuellerSpielerId == spiel.getAktuellerPlayer().getId()) {
            modelAndView = new ModelAndView("spiel");
        } else {
            modelAndView = new ModelAndView("wait");
        }
        getInfo(spiel, modelAndView, playerCookieId);
        log.debug("Spiel laeuft: {}", spiel);
        return modelAndView;
    }

    /**
     * Leitet aus dem WartenView weiter zum SpielView
     *
     * @param spielId        des angefragten Spiels
     * @param playerCookieId Identifizierung des Spielers
     * @return WaitView - wenn Spieler nicht an der Reihe, SpielView - wenn Spieler aufgefordert
     * wird eine Karte zu legen
     */
    @GetMapping("/spiel/{spielId}")
    public ModelAndView naechsterSpieler(@PathVariable Long spielId,
        @CookieValue(value = "PlayerCookieId") String playerCookieId) {

        Spiel spiel = spielLadenService.spielLaden(spielId);
        ModelAndView modelAndView;
        if (!spiel.getAktuellerPlayer().getCookieId().equals(playerCookieId)) {
            log.debug("Ein Spieler hat versucht zu cheaten");
            modelAndView = new ModelAndView("wait");
        } else {
            modelAndView = new ModelAndView("spiel");
        }
        getInfo(spiel, modelAndView, playerCookieId);
        log.debug("{} hat nun die Moeglichkeit eine Karte zu spielen",
            spiel.getAktuellerPlayer().getName());
        return modelAndView;
    }

    private void getInfo(Spiel spiel, ModelAndView modelAndView, String playerCookieId) {
        modelAndView.addObject("spiel", spiel);

        Optional<IPlayer> player = spiel.getPlayers().stream()
            .filter(e -> e.getCookieId().equals(playerCookieId)).findFirst();

        modelAndView.addObject("handkarten", player.get().getKarten());

        modelAndView.addObject("command",
            spiel.getAktuellerPlayer().getName() + " muss eine gültige Karte spielen");
        List<String> msg = logEntryRepository.logKurz(spiel.getId())
            .stream().map(e -> e.getMsg())
            .collect(Collectors.toList());

        modelAndView.addObject("log", msg);
    }


}

