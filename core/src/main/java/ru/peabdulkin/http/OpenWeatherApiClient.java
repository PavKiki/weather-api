package ru.peabdulkin.http;

import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.dto.CoordinatesDto;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static ru.peabdulkin.http.HttpUtils.processGetRequest;

public class OpenWeatherApiClient implements WeatherApiClient {

    private static final String GEO_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final String apiKey;

    public OpenWeatherApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public CoordinatesDto getCoordinates(String cityName) throws WeatherServerException, WeatherIOException {
        var encodedCity = URLEncoder.encode(cityName.trim(), StandardCharsets.UTF_8);
        var uri = GEO_URL + "?q=" + encodedCity + "&limit=1&appid=" + apiKey;
        return processGetRequest(uri, CoordinatesDto.class, "Error getting coordinates");
    }

    @Override
    public WeatherInfoDto getWeatherInfo(String cityName) throws WeatherIOException, WeatherServerException {
        var coordinates = getCoordinates(cityName);
        var uri = WEATHER_URL + "?lat=" + coordinates.latitude() + "&lon=" + coordinates.longitude() + "&appid=" + apiKey;
        return processGetRequest(uri, WeatherInfoDto.class, "Error getting weather info");
    }
}
