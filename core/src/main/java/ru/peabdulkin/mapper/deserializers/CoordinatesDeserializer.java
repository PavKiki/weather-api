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

        var longitudeNode = root.at("/0/lon");
        var latitudeNode = root.at("/0/lat");

        if (longitudeNode.isMissingNode() || latitudeNode.isMissingNode()) {
            throw new IOException("Coordinates not found");
        }

        return new CoordinatesDto(longitudeNode.asText(), latitudeNode.asText());
    }
}
