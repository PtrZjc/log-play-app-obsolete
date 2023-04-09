package pl.zajacp;

import org.apache.commons.collections4.ListUtils;
import pl.zajacp.model.GameRecord;
import pl.zajacp.model.GamesLog;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameLogRepository {

    //https://github.com/aws/aws-sdk-java-v2/issues/2265

    private final TableSchema<GameRecord> gameLogTableSchema = TableSchema.fromBean(GameRecord.class);

    private final String tableName;
    private final DynamoDbClient client = DynamoDbClient.builder().region(Region.EU_CENTRAL_1).build();

    public GameLogRepository(String tableName) {
        this.tableName = tableName;
    }

    public String putGamesLog(GamesLog gamesLog) {
        return gamesLog.getGamesLog().size() == 1
                ? putGameRecord(gamesLog.getGamesLog().get(0))
                : batchPutGameRecords(gamesLog.getGamesLog());
    }

    public String putGameRecord(GameRecord gameRecord) {
        var map = gameLogTableSchema.itemToMap(gameRecord, true);
        var put = PutItemRequest.builder().tableName(tableName).item(map).build();
        return client.putItem(put).toString();
    }

    private String batchPutGameRecords(List<GameRecord> items) {
        var batchWriteResponses = ListUtils.partition(items, 25)
                .stream()
                .map(this::mapToBatchWriteRequest)
                .map(client::batchWriteItem)
                .collect(Collectors.toList());

        return batchWriteResponses.toString();
    }

    private BatchWriteItemRequest mapToBatchWriteRequest(List<GameRecord> recordBatch) {
        var putRequests = recordBatch.stream()
                .map(i -> gameLogTableSchema.itemToMap(i, true))
                .map(m -> PutRequest.builder().item(m).build())
                .map(p -> WriteRequest.builder().putRequest(p).build())
                .collect(Collectors.toList());

        return BatchWriteItemRequest.builder()
                .requestItems(Collections.singletonMap(tableName, putRequests))
                .build();
    }
}
