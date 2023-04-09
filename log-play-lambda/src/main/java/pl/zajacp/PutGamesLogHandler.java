package pl.zajacp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import pl.zajacp.model.GamesLog;

public class PutGamesLogHandler implements RequestHandler<GamesLog, String> {

    private static final String TABLE_NAME = "games_log";
    private final GameLogRepository gameLogRepository = new GameLogRepository(TABLE_NAME);

    @Override
    public String handleRequest(GamesLog gamesLog, Context context) {

        context.getLogger().log("Received message " + gamesLog.toString());
        return gameLogRepository.putGamesLog(gamesLog);
    }

    private void validateGameRecordsNonEmpty(GamesLog gamesLog){
        if (gamesLog.getGamesLog() == null || gamesLog.getGamesLog().isEmpty()) {
            throw new IllegalArgumentException("Game record list should not be empty");
        }
    }
}
