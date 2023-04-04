package pl.zajacp;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.time.Instant;

public class PutGamesLogHandler implements RequestHandler<GamesLog, String> {

    private static final String TABLE_NAME = "games_log";

    private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String handleRequest(GamesLog gamesLog, Context context) {
        context.getLogger().log("Received message " + gamesLog.toString());

        Table table = dynamoDB.getTable(TABLE_NAME);

        gamesLog.getGamesLog().stream()
                .map(this::mapToUpdateItem)
                .forEach(table::putItem);
        return "OK";
    }

    @SneakyThrows
    private Item mapToUpdateItem(GameRecord g) {
        var item = new Item()
                .withPrimaryKey("timestamp", g.getTimestamp() != null ? g.getTimestamp() : Instant.now().toEpochMilli())
                .withBoolean("solo", g.isSolo())
                .withString("gameName", g.getGameName())
                .withString("gameDate", g.getGameDate())
                .withString("playerResults", MAPPER.writeValueAsString(g.getPlayerResults()));

        putStringIfExists(item, "gameDescription", g.getGameDescription());
        putStringIfExists(item, "duration", g.getDuration());
        return item;
    }

    private void putStringIfExists(Item item, String attrName, String val) {
        if (val != null) item.withString(attrName, val);
    }
}
