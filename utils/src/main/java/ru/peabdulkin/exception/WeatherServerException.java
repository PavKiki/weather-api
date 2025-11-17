package ru.peabdulkin.exception;

public class WeatherServerException extends WeatherSdkException {
    public WeatherServerException(String message) {
        super(message);
    }

    public WeatherServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
