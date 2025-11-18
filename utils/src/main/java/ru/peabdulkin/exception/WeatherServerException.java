package ru.peabdulkin.exception;

/**
 * Thrown when the external weather API (OpenWeather) responds
 * with an HTTP error status code (4xx or 5xx).
 */
public class WeatherServerException extends WeatherSdkException {

    private final int statusCode;

    public WeatherServerException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
