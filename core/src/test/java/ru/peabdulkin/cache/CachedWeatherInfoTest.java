package ru.peabdulkin.cache;

import org.junit.jupiter.api.Test;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CachedWeatherInfoTest {

    @Test
    public void shouldStoreWeatherInfoAndTimestamp() {

        WeatherInfoDto dto = new WeatherInfoDto(
            "Clouds",
            "scattered clouds",
            10.5,
            9.0,
            10000,
            3.4,
            1675744800L,
            1675751262L,
            1675787560L,
            3600,
            "Berlin"
        );
        Instant now = Instant.now();

        CachedWeatherInfo cached = new CachedWeatherInfo(dto, now);

        assertSame(cached.weatherInfoDto(), dto);
        assertEquals(cached.timestamp(), now);
    }
}