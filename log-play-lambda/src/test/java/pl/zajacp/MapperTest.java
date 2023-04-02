package pl.zajacp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {

    private final ObjectMapper MAPPER = new ObjectMapper();


    @Test
    void shouldProperlyMapRequestToDomain() throws IOException {
        //given
        String request = new String(getClass().getClassLoader().getResourceAsStream("exemplary-request.json").readAllBytes());

        //when
        GamesLog gamesLog = MAPPER.readValue(request, GamesLog.class);

        //then
        assertEquals(2, gamesLog.getGamesLog().size());
    }
}
