package ru.peabdulkin.http;

import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.dto.WeatherInfoDto;

public interface WeatherApiClient {
    WeatherInfoDto getWeatherInfo(String cityName) throws WeatherIOException, WeatherServerException;
}
