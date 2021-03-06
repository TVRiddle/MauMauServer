package maumau.spiel.model.interSpiel;

import java.util.List;
import maumau.spiel.model.interSpiel.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Ersetzt das DAO under der Verwendung von Spring-Data JPA
 */
@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    List<LogEntry> findAllBySpielIdAndRundeIsNullOrderByTimestampDesc(Long id);

    @Query(value = "SELECT * FROM LOG_ENTRY WHERE SPIEL_ID = ? AND runde IS NULL ORDER BY TIMESTAMP DESC LIMIT 10", nativeQuery = true)
    List<LogEntry> logKurz(Long id);

    default void updateLogRunde(Long spielId) {
        Long runde = getMaxRunde(spielId);
        final Long finalRunde = runde == null ? 1 : runde + 1;
        List<LogEntry> aktuelleLogs = findAllBySpielIdAndRundeIsNull(spielId);
        aktuelleLogs.stream().forEach(l -> l.setRunde(finalRunde));
        saveAll(aktuelleLogs);
    }

    List<LogEntry> findAllBySpielIdAndRundeIsNull(Long spielId);

    @Query(value = "select max(runde) from LOG_ENTRY where SPIEL_ID = ?", nativeQuery = true)
    Long getMaxRunde(Long spielId);
}
