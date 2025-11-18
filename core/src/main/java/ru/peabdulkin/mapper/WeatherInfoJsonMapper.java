package ru.peabdulkin.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import static ru.peabdulkin.mapper.MapperUtils.DEFAULT_MAPPER;

public class WeatherInfoJsonMapper {

    public static String convertToJson(ObjectMapper mapper, WeatherInfoDto weather) throws WeatherIOException {
        try {
            return mapper.writeValueAsString(weather);
        } catch (JsonProcessingException e) {
            throw new WeatherIOException("Failed to serialize weather info", e);
        }
    }

    public static String convertToJsonByDefaultMapper(WeatherInfoDto weather) throws WeatherIOException {
        return convertToJson(DEFAULT_MAPPER, weather);
    }
}
