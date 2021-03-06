package maumau.aufrufer.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.aufrufer.services.JoinService;
import maumau.aufrufer.services.SpielLadenService;
import maumau.interPlayer.IPlayer;
import maumau.spiel.model.interSpiel.LogEntryRepository;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JoinApi {


    private final JoinService joinService;

    private final SpielLadenService spielLadenService;
    private final LogEntryRepository logEntryRepository;

    /**
     * Wird aufgerufen, wenn ein Spieler einem Spiel beitreten moechte
     *
     * @param spielId    des Spieles, dass der User betreten will
     * @param playerName der Name mit dem der User versucht dem Spiel beizutreten
     * @param response   zum Setzen des Cookies zur Spieleridentifizierung
     * @return Ansicht des laufenden Spiels
     */
    @PostMapping(value = {"/join"})
    public ModelAndView join(
        @RequestParam(required = false) Optional<Long> spielId,
        @RequestParam(required = false) Optional<String> playerName,
        HttpServletResponse response) {

        if (!spielId.isPresent() && !playerName.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("start");
            modelAndView.addObject("instructions",
                "Sowohl eine Id als auch ein Name muss angegeben werden");
            return modelAndView;
        }

        Spiel spiel = spielLadenService.spielLaden(spielId.get());
        Optional<IPlayer> player = joinService.registerPlayer(spiel, playerName.get());
        if (!player.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("start");
            modelAndView.addObject("instructions",
                "Entweder ist das Spiel schon voll oder der Name ist nicht vergeben");
            return modelAndView;
        }

        response.addCookie(new Cookie("PlayerCookieId", player.get().getCookieId()));
        ModelAndView modelAndView = new ModelAndView("wait");
        modelAndView.addObject("handkarten", player.get().getKarten());
        modelAndView.addObject("spiel", spiel);
        modelAndView.addObject("command",
            spiel.getAktuellerPlayer().getName() + " muss eine gültige Karte spielen");
        List<String> msg = logEntryRepository
            .findAllBySpielIdAndRundeIsNullOrderByTimestampDesc(spiel.getId())
            .stream().map(e -> e.getMsg())
            .collect(Collectors.toList());
        modelAndView.addObject("log", msg);
        log.debug("{} hat das Spiel betreten ", player.get().getName());
        return modelAndView;

    }
}
