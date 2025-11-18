package ru.peabdulkin.cache;

import org.junit.jupiter.api.Test;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherCacheTest {

    private WeatherInfoDto sampleDto(String name) {
        return new WeatherInfoDto(
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
            name
        );
    }

    @Test
    public void getShouldReturnNullWhenCityNotInCache() {
        WeatherCache cache = new WeatherCache(10, 10);
        var result = cache.get("Moscow");
        assertNull(result);
    }

    @Test
    public void getShouldReturnValueWhenEntryIsFresh() {
        WeatherCache cache = new WeatherCache(10, 10);
        WeatherInfoDto dto = sampleDto("Moscow");

        cache.put("Moscow", dto);
        var result = cache.get("Moscow");

        assertNotNull(result);
        assertSame(dto, result);
    }

    @Test
    public void getShouldReturnNullWhenEntryExpiredImmediatelyWithZeroTtl() {
        WeatherCache cache = new WeatherCache(10, 0);
        WeatherInfoDto dto = sampleDto("Moscow");

        cache.put("Moscow", dto);
        var result = cache.get("Moscow");

        assertNull(result, "Entry must be treated as expired when TTL is zero");
    }

    @Test
    public void putShouldEvictOldestEntryWhenCapacityExceeded() {
        WeatherCache cache = new WeatherCache(2, 10);
        WeatherInfoDto Moscow = sampleDto("Moscow");
        WeatherInfoDto paris = sampleDto("Paris");
        WeatherInfoDto rome = sampleDto("Rome");

        cache.put("Moscow", Moscow);
        cache.put("Paris", paris);
        cache.put("Rome", rome);

        assertNull(cache.get("Moscow"), "Oldest entry must be evicted when capacity exceeded");
        assertSame(paris, cache.get("Paris"));
        assertSame(rome, cache.get("Rome"));
    }

    @Test
    public void getKeysShouldReturnAllCurrentKeysInInsertionOrder() {
        WeatherCache cache = new WeatherCache(3, 10);
        cache.put("Moscow", sampleDto("Moscow"));
        cache.put("Paris", sampleDto("Paris"));
        cache.put("Rome", sampleDto("Rome"));

        Set<String> keys = cache.getKeys();

        assertEquals(3, keys.size());
        String[] actualOrder = keys.toArray(new String[0]);
        assertArrayEquals(new String[]{"Moscow", "Paris", "Rome"}, actualOrder);
    }
}