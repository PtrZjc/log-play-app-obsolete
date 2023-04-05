package pl.zajacp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

//https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/migration-whats-different.html

public class GetGamesLogHandler implements RequestHandler<GamesLog, String> {

    private static final String TABLE_NAME = "games_log";

    private final DynamoDbClient dynamoDB = DynamoDbClient.builder().region(Region.EU_CENTRAL_1).build();
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String handleRequest(GamesLog pageRequest, Context context) {

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(TABLE_NAME)
                .build();
        return null;
    }

}