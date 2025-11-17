package ru.peabdulkin.http.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.peabdulkin.mapper.deserializers.WeatherInfoDeserializer;
import ru.peabdulkin.mapper.serializers.WeatherInfoSerializer;

@JsonDeserialize(using = WeatherInfoDeserializer.class)
@JsonSerialize(using = WeatherInfoSerializer.class)
public record WeatherInfoDto(
    String weatherTitle,
    String weatherDescription,
    double realTemperature,
    double feelsLikeTemperature,
    int visibility,
    double windSpeed,
    long datetime,
    long sunrise,
    long sunset,
    int timezone,
    String name
) { }
