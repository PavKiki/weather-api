package ru.peabdulkin.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.http.dto.WeatherInfoDto;

public class MapperUtils {
    public static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    public static String convertToJson(WeatherInfoDto weather) throws WeatherIOException {
        try {
            return DEFAULT_MAPPER.writeValueAsString(weather);
        } catch (JsonProcessingException e) {
            throw new WeatherIOException("Failed to serialize weather info", e);
        }
    }
}
