package maumau.aufrufer.api;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.aufrufer.services.SpielAblaufService;
import maumau.spiel.model.interSpiel.exceptions.AnzahlSpielerException;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NewGameApi {

    private final SpielAblaufService spielAblaufService;

    /**
     * Erstelt ein neues Spiel mit
     *
     * @param spielerName     name des ersten Spielers, der das Spiel erstellt
     * @param spielerAnzahl   wie viele echte Spieler nehmen teil
     * @param computerEinfach wie viele einfache Computer nehmen teil
     * @param computerMittel  wie viele mittlere Computer nehmen teil
     * @param computerSchwer  wie viele schwere Computer nehmen teil
     * @param achtAussetzen   soll bei einer 8 ausgesetzt werden
     * @param assNochmal      soll bei Ass noch mal gelegt werden
     * @param mauRegel        muss man "mau" sagen
     * @param bubeBube        darf man auf Buben einen Buben legen
     * @param response        zum Setzen des Cookies zur Spieleridentifizierung
     * @return Ansicht des laufenden Spiels
     */
    @PostMapping(value = {"/newGame"})
    public ModelAndView erstelleNeuesSpiel(
        @RequestParam(name = "spielerName") String spielerName,
        @RequestParam(name = "anzahlSpieler") Integer spielerAnzahl,
        @RequestParam(name = "computerEinfach") Integer computerEinfach,
        @RequestParam(name = "computerMittel") Integer computerMittel,
        @RequestParam(name = "computerSchwer") Integer computerSchwer,
        @RequestParam(name = "achtAussetzen") Optional<String> achtAussetzen,
        @RequestParam(name = "assNochmal") Optional<String> assNochmal,
        @RequestParam(name = "mauRegel") Optional<String> mauRegel,
        @RequestParam(name = "bubeBube") Optional<String> bubeBube,
        HttpServletResponse response) {
        Spiel spiel;
        try {
            spiel = spielAblaufService
                .erstelleNeuesSpiel(spielerName, spielerAnzahl, computerEinfach, computerMittel,
                    computerSchwer, achtAussetzen.isPresent(), assNochmal.isPresent(),
                    mauRegel.isPresent(), bubeBube.isPresent());
            String cookieId = spiel.getPlayers().stream()
                .filter(p -> !p.getCookieId().equals("foo")).findFirst().get().getCookieId();
            response.addCookie(new Cookie("PlayerCookieId", cookieId));
        } catch (KeineKartenMehr e) {
            log.error("Zu viele Spieler wurden ausgewählt");
            ModelAndView modelAndView = new ModelAndView("start");
            modelAndView.addObject("instructions",
                "Sie haben zuviele Spieler ausgewählt. Es dürfen maximal 6 Spieler gewählt werden");
            return modelAndView;
        } catch (AnzahlSpielerException e) {
            log.error("Es wurden eine nicht zulässige Menge an Spielern ausgewählt");
            ModelAndView modelAndView = new ModelAndView("start");
            modelAndView.addObject("instructions",
                "Sie haben eine nicht zulässige Menge an Spielern. Es müssen mindestens 2, maximal 6 Teilnehmer ausgewählt werden");
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("spiel");
        modelAndView.addObject("spiel", spiel);
        modelAndView.addObject("command",
            spiel.getAktuellerPlayer().getName() + " muss eine gültige Karte spielen");
        log.info("Starte Spiel: {}", spiel.getId());
        return modelAndView;
    }

}
