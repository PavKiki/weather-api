package ru.peabdulkin;

import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;

/**
 * Main SDK client interface for retrieving weather information.
 * <p>
 * Instances are created via {@link WeatherClientFactory}.
 */
public interface WeatherClient {
    /**
     * Returns weather information for the given city as a JSON string
     * in the standardized SDK format.
     *
     * <p>The actual caching behavior depends on the selected SDK mode
     * (see {@link ru.peabdulkin.mode.SdkMode}). In {@code ON_DEMAND} mode,
     * data is fetched from the remote API when it is missing or expired in the cache.
     * In {@code POLLING} mode, the cache is kept up to date in the background.
     *
     * @param city the city name (for example, {@code "Moscow"})
     * @return a JSON string with weather information
     * @throws WeatherIOException    if a network I/O error occurs, a timeout happens,
     *                               or the response cannot be read/parsed
     * @throws WeatherServerException if OpenWeather responds with a 4xx or 5xx HTTP status code
     */
    String getWeather(String city) throws WeatherIOException, WeatherServerException;

    /**
     * Gracefully shuts down internal resources associated with this client.
     * <p>
     * In {@code POLLING} mode, this stops the background scheduler.
     * In {@code ON_DEMAND} mode, this may be a no-op but is still recommended
     * to call for consistency and future extensibility.
     */
    void shutdown();
}
