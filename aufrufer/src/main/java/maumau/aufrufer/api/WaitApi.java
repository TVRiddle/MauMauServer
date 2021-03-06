package maumau.aufrufer.api;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.aufrufer.model.WaitResponce;
import maumau.aufrufer.services.SpielAblaufService;
import maumau.aufrufer.services.SpielLadenService;
import maumau.spiel.model.interSpiel.LogEntryRepository;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WaitApi {

    public final SpielLadenService spielLadenService;
    public final LogEntryRepository logEntryRepository;
    public final SpielAblaufService spielAblaufService;

    /**
     * Fragt ab ob der Spieler wieder an der Reihe ist
     *
     * @param spielId        des Spieles, dass der User betreten will
     * @param playerCookieId Identifizierung des Spielers
     * @return json object mit Informationen ueber das laufende Spiel
     */
    @GetMapping("/wait/{spielId}")
    public WaitResponce binIchAnDerReihe(@PathVariable Long spielId,
        @CookieValue("PlayerCookieId") String playerCookieId) {

        log.trace("Statusabfrage für {}", spielId);

        Spiel spiel = spielLadenService.spielLaden(spielId);
        boolean isRunning = spielAblaufService.isSpielLaufend(spiel);
        List<String> log = logEntryRepository.logKurz(spiel.getId())
            .stream().map(e -> e.getMsg())
            .collect(Collectors.toList());

        WaitResponce responce = WaitResponce.builder()
            .command(spiel.getAktuellerPlayer().getName() + " muss eine gültige Karte spielen")
            .playerId(spiel.getAktuellerPlayer().getCookieId())
            .log(log)
            .isRunning(isRunning)
            .build();
        return responce;
    }

}
