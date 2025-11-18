package ru.peabdulkin.mode;

/**
 * SDK operating mode controlling how weather data is fetched and refreshed.
 */
public enum SdkMode {
    /**
     * Fetches data from the remote API on demand.
     * <p>
     * The client first checks the cache. If the entry is present and still valid,
     * it is returned. Otherwise, the SDK performs a live call to OpenWeather.
     */
    ON_DEMAND,

    /**
     * Keeps weather data up to date in the background for all cached cities.
     * <p>
     * Periodically refreshes entries using a background scheduler to minimize
     * the latency of {@link ru.peabdulkin.WeatherClient#getWeather(String)} calls,
     * at the cost of additional API requests.
     */
    POLLING;
}
