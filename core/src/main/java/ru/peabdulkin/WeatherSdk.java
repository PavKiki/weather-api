package ru.peabdulkin;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.peabdulkin.cache.WeatherCache;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.WeatherApiClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static ru.peabdulkin.mapper.MapperUtils.DEFAULT_MAPPER;

public class WeatherSdk {
    private final WeatherApiClient weatherApiClient;
    private final SdkMode mode;
    private final ScheduledExecutorService scheduler;
    private final WeatherCache cache;

    public WeatherSdk(WeatherApiClient apiClient, SdkMode mode) {
        this.weatherApiClient = apiClient;
        this.mode = mode;
        this.scheduler = (mode == SdkMode.POLLING) ? Executors.newSingleThreadScheduledExecutor() : null;
        this.cache = new WeatherCache(10, 10);

        if (mode == SdkMode.POLLING) {
            // TODO: strat polling
        }
    }

    public String getWeather(String city) throws WeatherIOException, WeatherServerException, JsonProcessingException {
        var weather = cache.get(city);
        if (weather == null) {
            weather = weatherApiClient.getWeatherInfo(city);
            cache.put(city, weather);
        }

        return DEFAULT_MAPPER.writeValueAsString(weather);
    }
}
