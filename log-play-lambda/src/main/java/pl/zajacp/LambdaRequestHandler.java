package pl.zajacp;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LambdaRequestHandler implements RequestHandler<String, String> {

    private static final String TABLE_NAME = "games_log";

    private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String handleRequest(String gamesLogJson, Context context) {
        GamesLog gamesLog;
        try {
            gamesLog = MAPPER.readValue(gamesLogJson, GamesLog.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Table table = dynamoDB.getTable(TABLE_NAME);

        gamesLog.getGamesLog().stream()
                .map(this::mapToTableItem)
                .forEach(table::putItem);

        return "OK";
    }

    private Item mapToTableItem(GameRecord g) {
        String jsonPlayerResults;
        try {
            jsonPlayerResults = MAPPER.writeValueAsString(g.getPlayerResults());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new Item()
                .withPrimaryKey("timestamp", g.getTimestamp())
                .withString("gameName", g.getGameName())
                .withString("gameDate", g.getGameDate())
                .withString("playerResults", jsonPlayerResults);
    }
}
