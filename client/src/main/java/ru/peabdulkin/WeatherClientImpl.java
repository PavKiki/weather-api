package ru.peabdulkin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.peabdulkin.cache.WeatherCache;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherSdkException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.WeatherApiClient;
import ru.peabdulkin.http.dto.WeatherInfoDto;
import ru.peabdulkin.mode.SdkMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.peabdulkin.mapper.WeatherInfoJsonMapper.convertToJsonByDefaultMapper;

class WeatherClientImpl implements WeatherClient {

    private static final Logger log = LoggerFactory.getLogger(WeatherClientImpl.class);

    private final WeatherApiClient weatherApiClient;
    private final ScheduledExecutorService scheduler;
    private final WeatherCache cache;

    WeatherClientImpl(WeatherApiClient apiClient, SdkMode mode, WeatherCache cache) {
        this.weatherApiClient = apiClient;
        this.scheduler = (mode == SdkMode.POLLING) ? Executors.newSingleThreadScheduledExecutor() : null;
        this.cache = cache;

        if (mode == SdkMode.POLLING) {
            scheduler.scheduleAtFixedRate(() -> {
                cache.getKeys().forEach(city -> {
                    try {
                        getWeatherViaApi(city);
                    } catch (WeatherSdkException e) {
                        log.error("Failed to poll city {}", city, e);
                    }
                });
            }, 0, 10, TimeUnit.MINUTES);
        }
    }

    private WeatherInfoDto getWeatherViaApi(String city) throws WeatherIOException, WeatherServerException {
        var weather = weatherApiClient.getWeatherInfo(city);
        cache.put(city, weather);

        return weather;
    }

    @Override
    public String getWeather(String city) throws WeatherIOException, WeatherServerException {
        var weather = cache.get(city);
        if (weather == null) {
            weather = weatherApiClient.getWeatherInfo(city);
            cache.put(city, weather);
        }
        return convertToJsonByDefaultMapper(weather);
    }

    @Override
    public void shutdown() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
