package maumau.spiel.model.implSpiel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import maumau.implPlayer.ComputerEinfach;
import maumau.implPlayer.ComputerMittel;
import maumau.implPlayer.ComputerSchwer;
import maumau.implPlayer.PlayerImpl;
import maumau.interPlayer.Computer;
import maumau.interPlayer.IPlayer;
import maumau.karten.interKarten.ICardFactory;
import maumau.karten.interKarten.module.Karte;
import maumau.regel.interRegel.IRegelFactory;
import maumau.regel.interRegel.RegelServiceTemplate;
import maumau.regel.interRegel.module.RegelType;
import maumau.spiel.model.interSpiel.ISpielService;
import maumau.spiel.model.interSpiel.exceptions.AnzahlSpielerException;
import maumau.spiel.model.interSpiel.exceptions.KeineKartenMehr;
import maumau.spiel.model.interSpiel.model.Spiel;

@Data
public class SpielServiceImpl implements ISpielService {

    private static final int ANZAHL_ANFANGSKARTEN = 5;
    private static final int MAX_PLAYERS = 6;
    private ICardFactory cardFactory;
    private IRegelFactory regelFactory;

    @Override
    public Spiel erzeugeSpiel(String playername, int anzahlSpieler, int computerEinfach,
        int computerMittel, int computerSchwer, List<RegelType> regeln)
        throws AnzahlSpielerException {
        if (anzahlSpieler + computerEinfach + computerMittel + computerSchwer <= 1) {
            throw new AnzahlSpielerException("Es sind zu wenig Spieler ausgewählt worden");
        }
        Spiel spiel = new Spiel();
        spiel.setZiehStapel(cardFactory.createNewCardSet());
        spiel.setRegel(regeln);
        PlayerImpl player1 = new PlayerImpl(playername);
        player1.setCookieId(UUID.randomUUID().toString());
        neuerSpieler(spiel, player1);
        for (int i = 1; i < anzahlSpieler; i++) {
            neuerSpieler(spiel, new PlayerImpl("Spieler " + (i + 1)));
        }
        for (int i = 0; i < computerEinfach; i++) {
            neuerSpieler(spiel, new ComputerEinfach("Easy " + (i + 1)));
        }
        for (int i = 0; i < computerMittel; i++) {
            neuerSpieler(spiel, new ComputerMittel("Average " + (i + 1)));
        }
        for (int i = 0; i < computerSchwer; i++) {
            neuerSpieler(spiel, new ComputerSchwer("Hard " + (i + 1)));
        }
        return spiel;
    }


    @Override
    public void neuerSpieler(Spiel spiel, IPlayer spieler) throws AnzahlSpielerException {
        if (spiel.getPlayers().size() >= MAX_PLAYERS) {
            throw new AnzahlSpielerException(
                "Zu es können nur maximal " + MAX_PLAYERS + " mitspielen!");
        }
        spiel.getPlayers().add(spieler);
    }

    @Override
    public void kartenVerteilen(Spiel spiel) throws KeineKartenMehr {
        for (IPlayer spieler : spiel.getPlayers()) {
            for (int i = 0; i < ANZAHL_ANFANGSKARTEN; i++) {
                spieler.getKarten().add(zieheKarteVonZiehstapel(spiel));
            }
        }
    }

    @Override
    public void spielVorbereiten(Spiel spiel) throws KeineKartenMehr {
        ziehstapelMischen(spiel);
        Karte k = zieheKarteVonZiehstapel(spiel);
        spiel.getAblageStapel().add(k);
        spiel.setGueltigeKarte(new Karte(k.getFarbe(), k.getWert()));
        spiel.setRichtung(1);
        this.kartenVerteilen(spiel);
    }

    @Override
    public IPlayer naechsterSpieler(Spiel spiel) {
        Karte aktiveKarte =
            spiel.getAktiveKarten().isEmpty() ? null : spiel.getAktiveKarten().get(0);
        IPlayer player = getRegelByRegelType(spiel.getRegel())
            .bestimmeNaechstenSpieler(spiel.getPlayers(), spiel.getAktuellerPlayer(),
                aktiveKarte, spiel.getRichtung());
        spiel.setAktuellerPlayer(player);
        return player;
    }

    @Override
    public boolean istSpielLaufend(Spiel spiel) {
        if (spiel.getAblageStapel().isEmpty()) {
            return false;
        }
        for (IPlayer spieler : spiel.getPlayers()) {
            if (spieler.getKarten().isEmpty()) {
                return false;
            }
        }
        return spiel.getPlayers().size() > 1;
    }

    @Override
    public Karte zieheKarteVonZiehstapel(Spiel spiel) throws KeineKartenMehr {
        if (spiel.getZiehStapel().isEmpty()) {
            if (!spiel.getAblageStapel().isEmpty()) {
                Karte letzteKarte = spiel.getAblageStapel()
                    .remove(spiel.getAblageStapel().size() - 1);
                spiel.getZiehStapel().addAll(spiel.getAblageStapel());
                spiel.setAblageStapel(new ArrayList<>());
                spiel.getAblageStapel().add(letzteKarte);
                this.ziehstapelMischen(spiel);
            }
        }
        if (spiel.getZiehStapel().isEmpty()) {
            throw new KeineKartenMehr("Es sind keine Karten mehr auf vorhanden");
        }
        return spiel.getZiehStapel().remove(0);
    }

    @Override
    public void ziehstapelMischen(Spiel spiel) {
        Collections.shuffle(spiel.getZiehStapel());
    }

    @Override
    public void karteSpielen(Spiel spiel, Karte karte) {
        if (karte == null) {
            return;
        }
        spiel.getAktuellerPlayer().getKarten().remove(karte);
        spiel.getAblageStapel().add(karte);
        spiel.setGueltigeKarte(karte);
        speichereKarte(spiel, karte);
    }

    @Override
    public boolean checkMau(Spiel spiel, boolean maugesagt) throws KeineKartenMehr {
        IPlayer player = spiel.getAktuellerPlayer();
        RegelServiceTemplate regel = getRegelByRegelType(spiel.getRegel());
        if (!regel.hatPlayerMauRichtigGesagt(player, maugesagt)) {
            player.getKarten().add(zieheKarteVonZiehstapel(spiel));
            return false;
        }
        return true;
    }

    @Override
    public Spiel resetSpiel(Spiel spiel) throws KeineKartenMehr {
        spiel.getPlayers().forEach(player -> {
            spiel.getZiehStapel().addAll(player.getKarten());
            player.setKarten(new ArrayList<>());
        });
        spiel.getZiehStapel().addAll(spiel.getAblageStapel());
        spiel.setAblageStapel(new ArrayList<>());
        spielVorbereiten(spiel);
        return spiel;
    }

    @Override
    public void spielerZiehtKarte(Spiel spiel) throws KeineKartenMehr {
        spiel.getAktuellerPlayer().getKarten().add(zieheKarteVonZiehstapel(spiel));
        speichereKarte(spiel, null);
    }

    @Override
    public Karte getLetzteAblageStapelKarte(Spiel spiel) {
        return spiel.getAblageStapel().get(spiel.getAblageStapel().size() - 1);
    }

    @Override
    public int playerMussZiehen(Spiel spiel) throws KeineKartenMehr {
        int kartenZuZiehen = getRegelByRegelType(spiel.getRegel())
            .mussZiehen(spiel.getAktiveKarten());
        for (int i = 0; i < kartenZuZiehen; i++) {
            spiel.getAktuellerPlayer().getKarten().add(this.zieheKarteVonZiehstapel(spiel));
            spiel.setAktiveKarten(new ArrayList<>());
        }
        return kartenZuZiehen;
    }

    @Override
    public boolean karteGueltig(Karte karte, Spiel spiel) {
        return getRegelByRegelType(spiel.getRegel()).gueltigeKarte(spiel.getGueltigeKarte(), karte);
    }

    @Override
    public boolean wuenschen(Spiel spiel, Karte gespielteKarte) {
        return getRegelByRegelType(spiel.getRegel()).setGueltigeKarte(gespielteKarte);
    }

    private RegelServiceTemplate getRegelByRegelType(List<RegelType> regeln) {
        return regelFactory.getRegelService(regeln);
    }

    private void speichereKarte(Spiel spiel, Karte karte) {
        if (getRegelByRegelType(spiel.getRegel())
            .sollGespeichertWerden(karte, spiel.getAktiveKarten())) {
            if (!spiel.getAktiveKarten().isEmpty()) {
                if (!spiel.getAktiveKarten().get(0).getWert().equals(karte.getWert())) {
                    spiel.getAktiveKarten().clear();
                }
            }
            spiel.getAktiveKarten().add(karte);
        } else {
            spiel.getAktiveKarten().clear();
        }
    }

    @Override
    public Long lasseComputerKarteWaehlen(Spiel spiel) {
        int stufe = berechneSpielstand(spiel);
        while (stufe >= 0) {
            List<Karte> selectedCards = ((Computer) spiel.getAktuellerPlayer())
                .autoSelectCard(stufe);
            List<Karte> moeglicheKarten = selectedCards.stream().filter(k -> karteGueltig(k, spiel))
                .collect(Collectors.toList());
            if (moeglicheKarten.isEmpty()) {
                stufe -= 1;
            } else {
                return moeglicheKarten.get(new Random().nextInt(moeglicheKarten.size())).getId();
            }
        }
        return -1l;
    }

    private int berechneSpielstand(Spiel spiel) {
        int handKartenAnzahlPlayer = spiel.getAktuellerPlayer().getKarten().size();
        List<Integer> handKartenAnzahlGegener = spiel.getPlayers().stream()
            .map(e -> e.getKarten().size()).collect(Collectors.toList());

        if (handKartenAnzahlGegener.contains(1)) {
            return 3;
        }

        Integer maxDiff = handKartenAnzahlGegener.stream()
            .map(e -> handKartenAnzahlPlayer - e)
            .max((i, j) -> i.compareTo(j)).get();

        if (maxDiff >= 4) {
            return 3;
        } else if (maxDiff >= 1) {
            return 2;
        } else {
            return 1;
        }
    }
}
