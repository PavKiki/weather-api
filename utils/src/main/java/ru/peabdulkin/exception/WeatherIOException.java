package ru.peabdulkin.exception;

public class WeatherIOException extends WeatherSdkException {
    public WeatherIOException(String message) {
        super(message);
    }

    public WeatherIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
