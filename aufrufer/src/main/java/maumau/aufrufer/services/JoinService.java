package maumau.aufrufer.services;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.interPlayer.IPlayer;
import maumau.spiel.model.interSpiel.SpielRepository;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinService {

    private final SpielRepository spielRepository;

    /**
     * Schaut ob der Spielername in dem Spiel bereits vorhanden ist und laesst ihn ins Spiel zurueck
     * kehren. Sollte der Name nicht vorhanden sein, dann wird geschaut ob noch ein Platz frei ist.
     * Wenn ja, dann wird ein neuer Spieler erstellt
     *
     * @param spiel      in welches der User einsteigen moechte
     * @param playerName Name des Users unter dem er joinen moechte
     * @return einen Spieler in dem Spiel oder null wenn kein Spieler hinzugefuegt werden konnte
     */
    public Optional<IPlayer> registerPlayer(Spiel spiel, String playerName) {
        Optional<IPlayer> player = spiel.getPlayers().stream()
            .filter(p -> p.getName().equals(playerName)).findFirst();
        if (player.isEmpty()) {
            player = neuerSpieler(spiel, playerName);
        }
        return player;
    }

    private Optional<IPlayer> neuerSpieler(Spiel spiel, String name) {
        Optional<IPlayer> newPlayer = spiel.getPlayers().stream()
            .filter(p -> p.getCookieId() == null).findFirst();
        if (newPlayer.isPresent()) {
            newPlayer.get().setCookieId(UUID.randomUUID().toString());
            newPlayer.get().setName(name);
            spielRepository.save(spiel);
            log.debug("Realer Spieler wurde hinzugefügt und Spiel wurde gespeichert");
        }
        return newPlayer;
    }
}
