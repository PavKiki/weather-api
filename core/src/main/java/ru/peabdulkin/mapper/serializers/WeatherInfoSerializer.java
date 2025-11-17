package ru.peabdulkin.mapper.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.io.IOException;

public class WeatherInfoSerializer extends JsonSerializer<WeatherInfoDto> {

    @Override
    public void serialize(WeatherInfoDto weatherInfoDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeFieldName("weather");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("main", weatherInfoDto.weatherTitle());
        jsonGenerator.writeStringField("description", weatherInfoDto.weatherDescription());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeFieldName("temperature");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("temp", weatherInfoDto.realTemperature());
        jsonGenerator.writeNumberField("feels_like", weatherInfoDto.feelsLikeTemperature());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeNumberField("visibility", weatherInfoDto.visibility());

        jsonGenerator.writeFieldName("wind");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("speed", weatherInfoDto.windSpeed());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeNumberField("datetime", weatherInfoDto.datetime());

        jsonGenerator.writeFieldName("sys");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("sunrise", weatherInfoDto.sunrise());
        jsonGenerator.writeNumberField("sunset", weatherInfoDto.sunset());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeNumberField("timezone", weatherInfoDto.timezone());

        jsonGenerator.writeStringField("name", weatherInfoDto.name());

        jsonGenerator.writeEndObject();
    }
}
