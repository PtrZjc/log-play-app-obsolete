package pl.zajacp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class GameRecordDocument {

    private Long timestamp;
    private String gameName;
    private String gameDate;
    private String gameDescription;
    private boolean solo;
    private String duration;
    private String playerResults;

    @DynamoDbPartitionKey
    public String getGameName() {
        return gameName;
    }

    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp;
    }

    public static GameRecordDocument fromDomain(GameRecord gameRecord, String playerResults) {
        return new GameRecordDocument(
                gameRecord.getTimestamp(),
                gameRecord.getGameName(),
                gameRecord.getGameDate(),
                gameRecord.getGameDescription(),
                gameRecord.isSolo(),
                gameRecord.getDuration(),
                playerResults
        );
    }
}