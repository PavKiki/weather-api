package ru.peabdulkin.mapper.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import static org.junit.jupiter.api.Assertions.*;

class WeatherInfoDeserializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldDeserializeOpenWeatherResponseToWeatherInfoDto() throws Exception {
        String json = """
                {
                  "weather": [
                    {
                      "main": "Clouds",
                      "description": "overcast clouds"
                    }
                  ],
                  "main": {
                    "temp": 268.15,
                    "feels_like": 263.10
                  },
                  "visibility": 8000,
                  "wind": {
                    "speed": 5.2
                  },
                  "dt": 1675744800,
                  "sys": {
                    "sunrise": 1675749000,
                    "sunset": 1675782000
                  },
                  "timezone": 10800,
                  "name": "Moscow"
                }
                """;

        WeatherInfoDto dto = mapper.readValue(json, WeatherInfoDto.class);

        assertEquals("Clouds", dto.weatherTitle());
        assertEquals("overcast clouds", dto.weatherDescription());
        assertEquals(268.15, dto.realTemperature(), 0.000001);
        assertEquals(263.10, dto.feelsLikeTemperature(), 0.000001);
        assertEquals(8000, dto.visibility());
        assertEquals(5.2, dto.windSpeed(), 0.000001);
        assertEquals(1675744800L, dto.datetime());
        assertEquals(1675749000L, dto.sunrise());
        assertEquals(1675782000L, dto.sunset());
        assertEquals(10800, dto.timezone());
        assertEquals("Moscow", dto.name());
    }

    @Test
    void shouldGracefullyUseDefaultsWhenSomeFieldsMissing() throws Exception {
        String json = """
                {
                  "weather": [
                    {
                      "main": "Snow",
                      "description": "light snow"
                    }
                  ],
                  "main": {
                    "temp": 260.0,
                    "feels_like": 255.0
                  },
                  "name": "Moscow"
                }
                """;

        WeatherInfoDto dto = mapper.readValue(json, WeatherInfoDto.class);

        assertEquals("Snow", dto.weatherTitle());
        assertEquals("light snow", dto.weatherDescription());
        assertEquals(260.0, dto.realTemperature(), 0.000001);
        assertEquals(255.0, dto.feelsLikeTemperature(), 0.000001);
        assertEquals("Moscow", dto.name());

        assertEquals(0, dto.visibility());
        assertEquals(0.0, dto.windSpeed(), 0.000001);
        assertEquals(0L, dto.datetime());
        assertEquals(0L, dto.sunrise());
        assertEquals(0L, dto.sunset());
        assertEquals(0, dto.timezone());
    }
}