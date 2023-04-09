package pl.zajacp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@DynamoDbBean
public class PlayerResult {
    private String playerName;
    private BigDecimal playerScore;
    @JsonProperty("isWinner")
    private boolean isWinner;
    private String comment;
}