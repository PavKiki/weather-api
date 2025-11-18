package ru.peabdulkin.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newHttpClient();

    public static <T> T getRequest(HttpClient httpClient, ObjectMapper mapper,
                                   String uri, Class<T> responseClass, String errorMessage) throws WeatherIOException, WeatherServerException {

        var request = HttpRequest.newBuilder(URI.create(uri))
                                 .GET()
                                 .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var statusCode = response.statusCode();

            if (statusCode >= 400) {
                log.error("{}\nStatus code: {}. Reason: {}", errorMessage, statusCode, response.body());
                throw new WeatherServerException(response.body(), statusCode);
            }

            return mapper.readValue(response.body(), responseClass);
        } catch (Exception e) {
            log.error("{}", errorMessage, e);
            throw new WeatherIOException(e.getMessage(), e);
        }
    }
}
