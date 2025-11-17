package ru.peabdulkin;

import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;

public interface WeatherClient {
    String getWeather(String city) throws WeatherIOException, WeatherServerException;
}
