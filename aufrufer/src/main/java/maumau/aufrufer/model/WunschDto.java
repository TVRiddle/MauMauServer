package maumau.aufrufer.model;

import java.util.List;
import lombok.Data;

@Data

public class WunschDto {

    private Long spielId;
    private List<String> kartenFarben;


    public WunschDto(Long spielId, List<String> kartenFarben) {
        this.spielId = spielId;
        this.kartenFarben = kartenFarben;
    }
}
