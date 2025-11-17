package ru.peabdulkin.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static ru.peabdulkin.mapper.MapperUtils.DEFAULT_MAPPER;

public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newHttpClient();

    public static <T> T processGetRequest(HttpClient httpClient, String uri,
                                          Class<T> responseClass, String errorMessage) throws WeatherIOException, WeatherServerException {

        var request = HttpRequest.newBuilder(URI.create(uri))
                                 .GET()
                                 .build();

        try {
            var response = DEFAULT_HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            var statusCode = response.statusCode();

            if (statusCode >= 400) {
                log.error("{}\nStatus code: {}. Reason: {}", errorMessage, statusCode, response.body());
                throw new WeatherServerException(response.body());
            }

            return DEFAULT_MAPPER.readValue(response.body(), responseClass);
        } catch (Exception e) {
            log.error("{}", errorMessage, e);
            throw new WeatherIOException(e.getMessage());
        }
    }
}
