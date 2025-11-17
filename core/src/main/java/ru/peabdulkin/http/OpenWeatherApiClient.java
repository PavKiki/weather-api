package ru.peabdulkin.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.dto.CoordinatesDto;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import static ru.peabdulkin.http.HttpUtils.processGetRequest;

public class OpenWeatherApiClient implements WeatherApiClient {

    private static final Logger log = LoggerFactory.getLogger(OpenWeatherApiClient.class);

    private static final String GEO_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final String apiKey;
    private final HttpClient httpClient;

    public OpenWeatherApiClient(HttpClient httpClient, String apiKey) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
    }

    public CoordinatesDto getCoordinates(String cityName) throws WeatherServerException, WeatherIOException {
        var encodedCity = URLEncoder.encode(cityName.trim(), StandardCharsets.UTF_8);
        var uri = GEO_URL + "?q=" + encodedCity + "&limit=1&appid=" + apiKey;
        return processGetRequest(httpClient, uri, CoordinatesDto.class, "Error getting coordinates");
    }

    @Override
    public WeatherInfoDto getWeatherInfo(String cityName) throws WeatherIOException, WeatherServerException {
        var coordinates = getCoordinates(cityName);
        var uri = WEATHER_URL + "?lat=" + coordinates.latitude() + "&lon=" + coordinates.longitude() + "&appid=" + apiKey;
        return processGetRequest(httpClient, uri, WeatherInfoDto.class, "Error getting weather info");
    }
}
