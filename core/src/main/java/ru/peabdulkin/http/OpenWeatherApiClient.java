package ru.peabdulkin.http;

import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.dto.CoordinatesDto;
import ru.peabdulkin.http.dto.WeatherInfoDto;
import ru.peabdulkin.mapper.MapperUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static ru.peabdulkin.http.HttpUtils.DEFAULT_HTTP_CLIENT;
import static ru.peabdulkin.http.HttpUtils.getRequest;
import static ru.peabdulkin.mapper.MapperUtils.DEFAULT_MAPPER;

public class OpenWeatherApiClient implements WeatherApiClient {

    private static final String GEO_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final String apiKey;

    public OpenWeatherApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    protected <T> T processGetRequest(String uri, Class<T> clazz, String error) throws WeatherIOException, WeatherServerException {
        return HttpUtils.getRequest(HttpUtils.DEFAULT_HTTP_CLIENT,
                                    MapperUtils.DEFAULT_MAPPER,
                                    uri,
                                    clazz,
                                    error);
    }

    public CoordinatesDto getCoordinates(String cityName) throws WeatherServerException, WeatherIOException {
        var encodedCity = URLEncoder.encode(cityName.trim(), StandardCharsets.UTF_8);
        var uri = GEO_URL + "?q=" + encodedCity + "&limit=1&appid=" + apiKey;
        return processGetRequest(uri, CoordinatesDto.class, "Error getting coordinates");
    }

    @Override
    public WeatherInfoDto getWeatherInfo(String cityName) throws WeatherIOException, WeatherServerException {
        var coordinates = getCoordinates(cityName);
        var uri = WEATHER_URL + "?lat=" + coordinates.latitudeRaw() + "&lon=" + coordinates.longitudeRaw() + "&appid=" + apiKey;
        return processGetRequest(uri, WeatherInfoDto.class, "Error getting weather info");
    }
}
