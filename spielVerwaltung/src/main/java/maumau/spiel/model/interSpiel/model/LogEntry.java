package maumau.spiel.model.interSpiel.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class LogEntry {

    @Id
    @GeneratedValue
    Long id;

    Long spielId;

    String msg;

    LocalDateTime timestamp;

    Long runde;

    public LogEntry(Long spielId, String msg) {
        this.spielId = spielId;
        this.msg = msg;
        this.timestamp = LocalDateTime.now();
    }
}
