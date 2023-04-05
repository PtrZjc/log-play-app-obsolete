package pl.zajacp;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.collections4.ListUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.stream.Collectors;

public class GameLogRepository {

    //todo make sure the items are overriden

    TableSchema<GameRecordDocument> gameLogTableSchema = TableSchema.fromBean(GameRecordDocument.class);

    private final DynamoDbTable<GameRecordDocument> table;
    private final DynamoDbEnhancedClient enhancedClient;

    private final ObjectMapper MAPPER = new ObjectMapper();

    public Context context;

    public GameLogRepository(DynamoDbClient dynamoDbClient, String tableName) {
        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        table = enhancedClient
                .table(tableName, TableSchema.fromBean(GameRecordDocument.class));
    }

    public void putGameRecord(GameRecord gameRecord) {
        var putRequest = PutItemEnhancedRequest
                .builder(GameRecordDocument.class)
                .item(getGameRecordDocument(gameRecord))
                .build();
        table.putItem(putRequest);
    }

    public void putGamesLog(GamesLog gamesLog) {
        var records = gamesLog.getGamesLog()
                .stream()
                .map(this::getGameRecordDocument)
                .collect(Collectors.toList());
        batchPutGameRecords(records);
    }

    @SneakyThrows
    private GameRecordDocument getGameRecordDocument(GameRecord g) {
        return GameRecordDocument.fromDomain(g, MAPPER.writeValueAsString(g.getPlayerResults()));
    }

    private void batchPutGameRecords(List<GameRecordDocument> items) {
        ListUtils.partition(items, 25)
                .stream()
                .map(SingleGameRecordDocumentWriteBatch::new)
                .map(SingleGameRecordDocumentWriteBatch::getWriteBatch)
                .map(wb -> BatchWriteItemEnhancedRequest.builder().addWriteBatch(wb).build())
                .forEach(enhancedClient::batchWriteItem);
    }

    @Getter
    @AllArgsConstructor
    private class SingleGameRecordDocumentWriteBatch {
        private final WriteBatch writeBatch;
        public SingleGameRecordDocumentWriteBatch(List<GameRecordDocument> items) {
            var writeBatchBuilder = WriteBatch
                    .builder(GameRecordDocument.class)
                    .mappedTableResource(table);
            items.forEach(writeBatchBuilder::addPutItem);
            this.writeBatch = writeBatchBuilder.build();
        }
    }
}
