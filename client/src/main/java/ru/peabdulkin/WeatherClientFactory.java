package ru.peabdulkin;

import ru.peabdulkin.cache.WeatherCache;
import ru.peabdulkin.exception.WeatherSdkException;
import ru.peabdulkin.http.OpenWeatherApiClient;
import ru.peabdulkin.mode.SdkMode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for creating {@link WeatherClient} instances.
 * <p>
 * Responsible for managing the lifecycle of clients and preventing
 * multiple clients from being created with the same API key.
 */
public class WeatherClientFactory {

    private static final Map<String, WeatherClient> CLIENT_MAP = new ConcurrentHashMap<>();

    /**
     * Creates a new {@link WeatherClient} backed by the OpenWeather API.
     *
     * <p>The API key is normalized (trimmed and lowercased) and used as a unique
     * identifier for the client. Attempting to create a second client with the same
     * normalized key will result in a {@link WeatherSdkException}.
     *
     * @param apiKey the OpenWeather API key
     * @param mode   the desired SDK mode ({@link SdkMode#ON_DEMAND} or {@link SdkMode#POLLING})
     * @return a new {@link WeatherClient} instance
     * @throws WeatherSdkException if a client with the same API key already exists
     */
    public static synchronized WeatherClient createOpenWeatherClient(String apiKey, SdkMode mode) throws WeatherSdkException {
        var processedApiKey = apiKey.trim().toLowerCase();
        if (CLIENT_MAP.containsKey(processedApiKey)) {
            throw new WeatherSdkException("Client with apiKey" + processedApiKey + " already exists");
        }
        var client = new WeatherClientImpl(new OpenWeatherApiClient(processedApiKey), mode, new WeatherCache(10, 10));
        CLIENT_MAP.put(processedApiKey, client);

        return client;
    }

    /**
     * Performs a graceful shutdown of all created clients.
     * <p>
     * For each registered client, {@link WeatherClient#shutdown()} is called,
     * and then the internal client registry is cleared.
     * <p>
     * It is recommended to call this method during application shutdown when
     * using {@link SdkMode#POLLING}.
     */
    public static synchronized void peacefulShutdown() {
        for (var client : CLIENT_MAP.values()) {
            client.shutdown();
        }
        CLIENT_MAP.clear();
    }
}
