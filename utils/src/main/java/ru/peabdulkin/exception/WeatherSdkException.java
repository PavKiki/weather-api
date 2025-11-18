package ru.peabdulkin.exception;

/**
 * Thrown to indicate incorrect usage of the Weather SDK itself,
 * for example when attempting to create multiple clients with
 * the same API key or passing invalid configuration parameters.
 */
public class WeatherSdkException extends Exception {
    public WeatherSdkException(String message) {
        super(message);
    }

    public WeatherSdkException(String message, Throwable cause) {
        super(message, cause);
    }
}
