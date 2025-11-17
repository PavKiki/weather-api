package ru.peabdulkin.cache;

import ru.peabdulkin.http.dto.WeatherInfoDto;

import java.time.Instant;

public record CachedWeatherInfo(WeatherInfoDto weatherInfoDto, Instant timestamp) {
}
