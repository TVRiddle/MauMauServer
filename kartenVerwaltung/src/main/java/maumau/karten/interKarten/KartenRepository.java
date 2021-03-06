package maumau.karten.interKarten;

import maumau.karten.interKarten.module.Karte;
import maumau.karten.interKarten.module.KartenFarbe;
import maumau.karten.interKarten.module.KartenWert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Ersetzt das DAO under der Verwendung von Spring-Data JPA
 */
@Component
public interface KartenRepository extends JpaRepository<Karte, Long> {

    Karte findByFarbeAndWert(KartenFarbe farbe, KartenWert wert);

}
