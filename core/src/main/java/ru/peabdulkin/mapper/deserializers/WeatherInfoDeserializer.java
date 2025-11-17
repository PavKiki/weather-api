package ru.peabdulkin.mapper.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.io.IOException;

public class WeatherInfoDeserializer extends JsonDeserializer<WeatherInfoDto> {

    @Override
    public WeatherInfoDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);

        var weatherTitle = root.at("/weather/0/main").asText();
        var weatherDescription = root.at("/weather/0/description").asText();
        var realTemperature = root.at("/main/temp").asDouble();
        var feelsLikeTemperature = root.at("/main/feels_like").asDouble();
        var visibility = root.at("/visibility").asInt();
        var windSpeed = root.at("/wind/speed").asDouble();
        var datetime = root.at("/dt").asLong();
        var sunrise = root.at("/sys/sunrise").asLong();
        var sunset = root.at("/sys/sunset").asLong();
        var timezone = root.at("/timezone").asInt();
        var cityName = root.at("/name").asText();

        return new WeatherInfoDto(
            weatherTitle,
            weatherDescription,
            realTemperature,
            feelsLikeTemperature,
            visibility,
            windSpeed,
            datetime,
            sunrise,
            sunset,
            timezone,
            cityName);
    }
}
