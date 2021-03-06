package maumau.spiel.model.interSpiel;

import maumau.spiel.model.interSpiel.model.Spiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ersetzt das DAO under der Verwendung von Spring-Data JPA
 */
@Repository
public interface SpielRepository extends JpaRepository<Spiel, Long> {

}
