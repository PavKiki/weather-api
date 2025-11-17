package ru.peabdulkin;

import ru.peabdulkin.exception.WeatherSdkException;
import ru.peabdulkin.http.OpenWeatherApiClient;
import ru.peabdulkin.mode.SdkMode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherClientFactory {

    private static final Map<String, WeatherClient> CLIENT_MAP = new ConcurrentHashMap<>();

    public static synchronized WeatherClient createOpenWeatherClient(String apiKey, SdkMode mode) throws WeatherSdkException {
        var processedApiKey = apiKey.trim().toLowerCase();
        if (CLIENT_MAP.containsKey(processedApiKey)) {
            throw new WeatherSdkException("Client with apiKey" + processedApiKey + " already exists");
        }
        return new WeatherClientImpl(new OpenWeatherApiClient(processedApiKey), mode);
    }

    public static synchronized void peacefulShutdown() {
        for (var client : CLIENT_MAP.keySet()) CLIENT_MAP.remove(client);
    }
}
