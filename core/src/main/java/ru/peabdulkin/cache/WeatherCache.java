package ru.peabdulkin.cache;

import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WeatherCache {

    private final Map<String, CachedWeatherInfo> cache;
    private final Duration ttlMinutes;

    public WeatherCache(int capacity, int ttlMinutes) {
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CachedWeatherInfo> eldest) {
                return this.size() > capacity;
            }
        };
        this.ttlMinutes = Duration.ofMinutes(ttlMinutes);
    }

    public WeatherInfoDto get(String city) {
        var entry = cache.get(city);

        return   entry != null && Duration.between(entry.timestamp(), Instant.now()).compareTo(ttlMinutes) < 0
               ? entry.weatherInfoDto()
               : null;
    }

    public void put(String city, WeatherInfoDto weatherInfoDto) {
        cache.put(city, new CachedWeatherInfo(weatherInfoDto, Instant.now()));
    }

    public Set<String> getKeys() {
        return cache.keySet();
    }
}
