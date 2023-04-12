package pl.zajacp.repository;

import org.apache.commons.collections4.ListUtils;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DynamoDbRepository<T> {

    //https://github.com/aws/aws-sdk-java-v2/issues/2265

    private final TableSchema<T> tableSchema;

    private final String tableName;
    private final DynamoDbClient client = DynamoDbClient.builder().region(Region.EU_CENTRAL_1).build();

    public DynamoDbRepository(String tableName, Class<T> itemType) {
        this.tableName = tableName;
        this.tableSchema = TableSchema.fromBean(itemType);
    }

    public T getItem(ItemQueryKey itemQueryKey) {
        var getRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(itemQueryKey.toAttributeMap())
                .build();

        var itemAsMap = client.getItem(getRequest).item();
        return tableSchema.mapToItem(itemAsMap);
    }

    public String putItem(T gameRecord) {
        var map = tableSchema.itemToMap(gameRecord, true);
        var put = PutItemRequest.builder().tableName(tableName).item(map).build();
        return client.putItem(put).toString();
    }

    public String batchPutItems(List<T> items) {
        var batchWriteResponses = ListUtils.partition(items, 25)
                .stream()
                .map(this::mapToBatchWriteRequest)
                .map(client::batchWriteItem)
                .collect(Collectors.toList());

        return batchWriteResponses.toString();
    }

    private BatchWriteItemRequest mapToBatchWriteRequest(List<T> recordBatch) {
        var putRequests = recordBatch.stream()
                .map(i -> tableSchema.itemToMap(i, true))
                .map(m -> PutRequest.builder().item(m).build())
                .map(p -> WriteRequest.builder().putRequest(p).build())
                .collect(Collectors.toList());

        return BatchWriteItemRequest.builder()
                .requestItems(Collections.singletonMap(tableName, putRequests))
                .build();
    }
}
