package pl.zajacp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GamesLog {
    private List<GameRecord> gamesLog;
}

@Getter
@Setter
@NoArgsConstructor
@ToString
class GameRecord {
    private Long timestamp;
    private String gameName;
    private String gameDate;
    private String gameDescription;
    private boolean solo;
    private String duration;
    private List<PlayerResult> playerResults;
}

@Getter
@Setter
@NoArgsConstructor
@ToString
class PlayerResult {
    private String playerName;
    private BigDecimal playerScore;
    @JsonProperty("isWinner")
    private boolean isWinner;
    private String comment;
}