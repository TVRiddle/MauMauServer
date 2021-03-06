package maumau.aufrufer.services;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maumau.aufrufer.exception.GameOverException;
import maumau.aufrufer.exception.WunschInterceptionException;
import maumau.interPlayer.Computer;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.KartenRepository;
import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import maumau.regel.interRegel.module.RegelType;
import maumau.spiel.model.interSpiel.ISpielService;
import maumau.spiel.model.interSpiel.LogEntryRepository;
import maumau.spiel.model.interSpiel.SpielRepository;
import maumau.spiel.model.interSpiel.exceptions.AnzahlSpielerException;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.LogEntry;
import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpielAblaufService {

    private final ISpielService spielService;
    private final SpielRepository spielRepository;
    private final KartenRepository kartenRepository;
    private final LogEntryRepository logEntryRepository;

    /**
     * Erstelt ein neues Spiel mit
     *
     * @param spielername     name des ersten Spielers, der das Spiel erstellt
     * @param spielerAnzahl   wie viele echte Spieler nehmen teil
     * @param computerEinfach wie viele einfache Computer nehmen teil
     * @param computerMittel  wie viele mittlere Computer nehmen teil
     * @param computerSchwer  wie viele schwere Computer nehmen teil
     * @param achtAussetzen   soll bei einer 8 ausgesetzt werden
     * @param assNochmal      soll bei Ass noch mal gelegt werden
     * @param mauRegel        muss man "mau" sagen
     * @param bubeBube        darf man auf Buben einen Buben legen
     * @throws KeineKartenMehr
     * @throws AnzahlSpielerException
     */
    @Transactional
    public Spiel erstelleNeuesSpiel(String spielername, int spielerAnzahl, int computerEinfach,
        int computerMittel, int computerSchwer, boolean achtAussetzen,
        boolean assNochmal, boolean mauRegel, boolean bubeBube)
        throws KeineKartenMehr, AnzahlSpielerException {
        List<RegelType> regeln = getRegelList(achtAussetzen, assNochmal, mauRegel, bubeBube);
        log.info("Neues Spiel wird erstellt");
        Spiel spiel = spielService
            .erzeugeSpiel(spielername, spielerAnzahl, computerEinfach, computerMittel,
                computerSchwer, regeln);
        log.info("Spiel wird vorbereitet");
        spielService.spielVorbereiten(spiel);
        spielService.naechsterSpieler(spiel);
        log.debug("Spiel wird gespeichert");
        spielRepository.save(spiel);
        return spiel;
    }

    /**
     * Ueberprueft ob die gespielte Karte des Users gueltig ist und welche Folgen dies hat
     *
     * @param spielId   des fraglichen Spiels
     * @param kartenId  der Karte die gespielt werden soll
     * @param maugesagt angabe ob User mau gesagt hat
     * @return das geupdatete Spiel
     * @throws KeineKartenMehr
     * @throws WunschInterceptionException
     * @throws GameOverException
     */
    @Transactional
    public Spiel evaluiereZug(Long spielId, Long kartenId, boolean maugesagt)
        throws KeineKartenMehr, WunschInterceptionException, GameOverException {
        Spiel spiel = spielRepository.getOne(spielId);
        IPlayer player = spiel.getAktuellerPlayer();
        if (kartenId < 0) {
            spiel = spielerZieht(spiel);
        } else {
            spiel = spielerMachtZug(kartenId, player, spiel, maugesagt);
        }
        if (!spielService.istSpielLaufend(spiel)) {
            log.debug("Spiel {} wurde beendet", spielId);
            throw new GameOverException("Das Spiel ist beendet");
        }
        checkPc(spiel);
        log.debug("Spiel wird gespeichert");
        spielRepository.save(spiel);
        return spiel;
    }

    /**
     * Passt das Spiel auf den Wunsch eines Users hin an
     *
     * @param spielId          des fraglichen Spiels
     * @param gewuenschteFarbe Farbe die sich der User gewuenscht hat
     * @return geupdatetes Spiel
     * @throws KeineKartenMehr
     * @throws GameOverException
     * @throws WunschInterceptionException
     */
    @Transactional
    public Spiel wunschKarteAnpassen(Long spielId, String gewuenschteFarbe)
        throws KeineKartenMehr, GameOverException, WunschInterceptionException {
        Spiel spiel = spielRepository.getOne(spielId);
        Karte karte = new Karte(KartenFarbe.getEnumByName(gewuenschteFarbe), KartenWert.WERTBube);
        spiel.setGueltigeKarte(karte);
        log.info("Spiel: {} - {} hat sich {} gewünscht", spielId,
            spiel.getAktuellerPlayer().getName(), gewuenschteFarbe);
        logEntryRepository.save(createLogEntry(spiel.getId(),
            String.format("%s hat sich %s gewünscht", spiel.getAktuellerPlayer().getName(),
                gewuenschteFarbe)));
        spielService.naechsterSpieler(spiel);
        spielService.playerMussZiehen(spiel);
        if (!spielService.istSpielLaufend(spiel)) {
            log.debug("Spiel {} wurde beendet", spielId);
            throw new GameOverException("Das Spiel ist beendet");
        }
        checkPc(spiel);
        log.debug("Spiel wird gespeichert");
        spielRepository.save(spiel);
        return spiel;
    }

    private Spiel spielerZieht(Spiel spiel)
        throws KeineKartenMehr {
        loggen(spiel.getId(),
            String.format("%s zieht eine Karte", spiel.getAktuellerPlayer().getName()));
        spielService.spielerZiehtKarte(spiel);
        spielService.naechsterSpieler(spiel);
        return spiel;
    }

    /**
     * Wenn gespielte Karte gueltig ist, werden hier die weiteren Schritte vollzogen (Check ob er
     * sich was wuenschen kann, wer als naechster dran ist, updaten der Kartenstapel)
     *
     * @param kartenId  der gespielten Karte
     * @param player    der aktuelle Player
     * @param spiel     das aktuelle Spiel
     * @param maugesagt Angabe ob Player mau gesagt hat
     * @return
     * @throws KeineKartenMehr
     * @throws WunschInterceptionException
     * @throws GameOverException
     */
    @Transactional
    public Spiel spielerMachtZug(Long kartenId, IPlayer player, Spiel spiel, boolean maugesagt)
        throws KeineKartenMehr, WunschInterceptionException, GameOverException {
        Karte karte = kartenRepository.getOne(kartenId);
        if (maugesagt) {
            loggen(spiel.getId(), String.format("%s sagt mau", player.getName()));
        }

        if (spielService.karteGueltig(karte, spiel)) {
            loggen(spiel.getId(),
                String.format("%s spielt %s", player.getName(), karte.getAusschrift()));
            if (!spielService.checkMau(spiel, maugesagt)) {
                loggen(spiel.getId(), String
                    .format("%s hat falsch mau gesagt und zieht eine Karte", player.getName()));
            }
            if (spielService.wuenschen(spiel, karte)) {
                checkWunsch(player, spiel, karte);
            } else {
                spielService.karteSpielen(spiel, karte);
                spielService.naechsterSpieler(spiel);
            }
            int kartenZuZiehen = spielService.playerMussZiehen(spiel);
            if (kartenZuZiehen != 0) {
                loggen(spiel.getId(), String.format("%s muss %s Karten ziehen",
                    spiel.getAktuellerPlayer().getName(), kartenZuZiehen));
            }
        } else {
            loggen(spiel.getId(),
                String.format("%s hat eine ungültige Karte gewählt", player.getName()));
        }
        return spiel;
    }

    private void checkWunsch(IPlayer player, Spiel spiel, Karte karte)
        throws WunschInterceptionException, KeineKartenMehr, GameOverException {
        spielService.karteSpielen(spiel, karte);
        if (player instanceof Computer) {
            String compWunschFarbe = ((Computer) player).farbewuenschen();
            wunschKarteAnpassen(spiel.getId(), compWunschFarbe);
        } else {
            loggen(spiel.getId(),
                String.format("%s darf sich eine Farbe wünschen", player.getName()));
            log.debug("Spiel wird gespeichert");
            spielRepository.save(spiel);
            throw new WunschInterceptionException();
        }
    }

    private void checkPc(Spiel spiel)
        throws KeineKartenMehr, WunschInterceptionException, GameOverException {
        IPlayer player = spiel.getAktuellerPlayer();
        if (player instanceof Computer) {
            boolean maugesagt = ((Computer) player).mussMauSagen();
            Long playedCardId = spielService.lasseComputerKarteWaehlen(spiel);
            evaluiereZug(spiel.getId(), playedCardId, maugesagt);
        }
    }

    public boolean isSpielLaufend(Spiel spiel) {
        return spielService.istSpielLaufend(spiel);
    }

    private List<RegelType> getRegelList(boolean achtAussetzen, boolean assNochmal,
        boolean mauRegel, boolean bubeBube) {
        List<RegelType> regeln = new ArrayList<>();
        if (achtAussetzen) {
            regeln.add(RegelType.ACHT_AUSSETZEN);
        }
        if (assNochmal) {
            regeln.add(RegelType.ASS_NOCHMAL);
        }
        if (mauRegel) {
            regeln.add(RegelType.MAU_REGEL);
        }
        if (bubeBube) {
            regeln.add(RegelType.BUBE_BUBE);
        }
        return regeln;
    }

    /**
     * Gibt ein Spiel als Object zurueck, dass zu der angegebenen Id passt
     *
     * @param spielId des angefragten Spiels
     * @return
     */
    public Spiel getSpiel(Long spielId) {
        return spielRepository.findById(spielId).get();
    }

    private LogEntry createLogEntry(Long id, String msg) {
        LogEntry entry = new LogEntry(id, msg);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            log.error("Unerwartete ThreadInterruption aufgetreten");
        }
        return entry;
    }

    private void loggen(Long spielId, String msg) {
        log.info("Spiel {} - {}", spielId, msg);
        logEntryRepository.save(createLogEntry(spielId, msg));
    }
}
