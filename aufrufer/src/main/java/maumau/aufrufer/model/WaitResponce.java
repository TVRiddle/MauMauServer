package maumau.aufrufer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;

@Builder
public class WaitResponce {

    @JsonProperty("log")
    private List<String> log;

    @JsonProperty("aktuellerPlayer")
    private String playerId;

    @JsonProperty("command")
    private String command;

    @JsonProperty("isRunning")
    private boolean isRunning;
}
