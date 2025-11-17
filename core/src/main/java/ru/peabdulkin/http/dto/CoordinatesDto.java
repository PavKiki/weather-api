package ru.peabdulkin.http.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.peabdulkin.mapper.deserializers.CoordinatesDeserializer;

@JsonDeserialize(using = CoordinatesDeserializer.class)
public record CoordinatesDto(String longitude, String latitude) { }