package ru.peabdulkin.exception;

/**
 * Thrown to indicate an I/O-related problem while communicating
 * with the external weather API (network issues, timeouts, etc.).
 */
public class WeatherIOException extends WeatherSdkException {
    public WeatherIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
