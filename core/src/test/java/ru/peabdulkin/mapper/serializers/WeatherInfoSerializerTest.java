package ru.peabdulkin.mapper.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherInfoSerializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldSerializeWeatherInfoDtoToExpectedJsonStructure() throws Exception {
        WeatherInfoDto dto = new WeatherInfoDto(
            "Clouds",
            "overcast clouds",
            268.15,        // temp (K)
            263.10,        // feels like (K)
            8000,          // visibility
            5.2,           // wind speed
            1675744800L,   // dt
            1675749000L,   // sunrise
            1675782000L,   // sunset
            10800,         // timezone (UTC+3)
            "Moscow"
        );

        String json = mapper.writeValueAsString(dto);
        JsonNode root = mapper.readTree(json);

        JsonNode weatherNode = root.get("weather");
        assertNotNull(weatherNode);
        assertEquals("Clouds", weatherNode.get("main").asText());
        assertEquals("overcast clouds", weatherNode.get("description").asText());

        JsonNode temperatureNode = root.get("temperature");
        assertNotNull(temperatureNode);
        assertEquals(268.15, temperatureNode.get("temp").asDouble(), 0.000001);
        assertEquals(263.10, temperatureNode.get("feels_like").asDouble(), 0.000001);

        assertEquals(8000, root.get("visibility").asInt());

        JsonNode windNode = root.get("wind");
        assertNotNull(windNode);
        assertEquals(5.2, windNode.get("speed").asDouble(), 0.000001);

        assertEquals(1675744800L, root.get("datetime").asLong());

        JsonNode sysNode = root.get("sys");
        assertNotNull(sysNode);
        assertEquals(1675749000L, sysNode.get("sunrise").asLong());
        assertEquals(1675782000L, sysNode.get("sunset").asLong());

        assertEquals(10800, root.get("timezone").asInt());

        assertEquals("Moscow", root.get("name").asText());
    }

    @Test
    public void shouldNotProduceUnexpectedExtraFields() throws Exception {
        WeatherInfoDto dto = new WeatherInfoDto(
            "Snow",
            "light snow",
            260.0,
            255.0,
            0,
            0.0,
            0L,
            0L,
            0L,
            10800,
            "Moscow"
        );

        String json = mapper.writeValueAsString(dto);
        JsonNode root = mapper.readTree(json);

        assertTrue(root.has("weather"));
        assertTrue(root.has("temperature"));
        assertTrue(root.has("visibility"));
        assertTrue(root.has("wind"));
        assertTrue(root.has("datetime"));
        assertTrue(root.has("sys"));
        assertTrue(root.has("timezone"));
        assertTrue(root.has("name"));

        assertEquals(8, root.size());
    }
}
