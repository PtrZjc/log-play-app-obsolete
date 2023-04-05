package pl.zajacp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.zajacp.TestUtils.EXEMPLARY_REQUEST;

public class MapperTest {


    private final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void shouldProperlyMapRequestToDomain() throws IOException {
        //when
        GamesLog gamesLog = MAPPER.readValue(EXEMPLARY_REQUEST, GamesLog.class);

        //then
        assertEquals(199, gamesLog.getGamesLog().size());
    }
}
