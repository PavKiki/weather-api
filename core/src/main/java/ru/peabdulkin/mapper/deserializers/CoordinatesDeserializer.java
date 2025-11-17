package ru.peabdulkin.mapper.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.peabdulkin.http.dto.CoordinatesDto;

import java.io.IOException;

public class CoordinatesDeserializer extends JsonDeserializer<CoordinatesDto> {

    @Override
    public CoordinatesDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);

        var longitude = root.at("/0/lon").asText();
        var latitude = root.at("/0/lat").asText();

        return new CoordinatesDto(longitude, latitude);
    }
}
