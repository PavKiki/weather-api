package ru.peabdulkin.mapper.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.peabdulkin.http.dto.CoordinatesDto;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinatesDeserializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldDeserializeFirstArrayElementLonLat() throws Exception {
        String json = """
                [
                  {
                    "name": "Moscow",
                    "lat": "55.75",
                    "lon": "37.62",
                    "country": "Russia"
                  }
                ]
                """;

        CoordinatesDto dto = mapper.readValue(json, CoordinatesDto.class);

        assertEquals("37.62", dto.longitudeRaw());
        assertEquals("55.75", dto.latitudeRaw());
    }

    @Test
    public void shouldThrowIOExceptionWhenArrayIsEmpty() {
        String json = "[]";

        var ex = assertThrows(IOException.class, () -> mapper.readValue(json, CoordinatesDto.class));

        assertTrue(ex.getMessage().contains("Coordinates not found"));
    }

    @Test
    public void shouldThrowIOExceptionWhenLonOrLatMissing() {
        String json = """
                [
                  {
                    "name": "Moscow",
                    "country": "Russia"
                  }
                ]
                """;

        var ex = assertThrows(IOException.class, () -> mapper.readValue(json, CoordinatesDto.class));

        assertTrue(ex.getMessage().contains("Coordinates not found"));
    }
}
