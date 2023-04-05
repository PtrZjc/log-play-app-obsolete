package pl.zajacp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class PutGamesLogHandler implements RequestHandler<GamesLog, String> {

    private static final String TABLE_NAME = "games_log";
    private final DynamoDbClient dynamoDB = DynamoDbClient.builder().region(Region.EU_CENTRAL_1).build();
    private final GameLogRepository gameLogRepository = new GameLogRepository(dynamoDB, TABLE_NAME);

    @Override
    public String handleRequest(GamesLog gamesLog, Context context) {
        context.getLogger().log("Received message " + gamesLog.toString());
        gameLogRepository.context = context;
        gameLogRepository.putGamesLog(gamesLog);
        return "OK";
    }
}
