package ru.peabdulkin.http;

import org.junit.jupiter.api.Test;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.dto.CoordinatesDto;
import ru.peabdulkin.http.dto.WeatherInfoDto;

import static org.junit.jupiter.api.Assertions.*;

public class OpenWeatherApiClientTest {

    public static class TestableClient extends OpenWeatherApiClient {

        private String lastUri;
        private CoordinatesDto nextCoordinatesResult;
        private WeatherInfoDto nextWeatherResult;

        public TestableClient(String apiKey) {
            super(apiKey);
        }

        public void setNextCoordinatesResult(CoordinatesDto nextCoordinatesResult) {
            this.nextCoordinatesResult = nextCoordinatesResult;
        }

        public void setNextWeatherResult(WeatherInfoDto nextWeatherResult) {
            this.nextWeatherResult = nextWeatherResult;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected <T> T processGetRequest(String uri, Class<T> responseClass, String error) {
            this.lastUri = uri;
            if (responseClass == CoordinatesDto.class) {
                return (T)nextCoordinatesResult;
            } else if (responseClass == WeatherInfoDto.class) {
                return (T)nextWeatherResult;
            } else {
                return null;
            }
        }
    }

    @Test
    public void getCoordinatesShouldFormCorrectUri() throws WeatherIOException, WeatherServerException {
        TestableClient client = new TestableClient("TEST_KEY");
        client.setNextCoordinatesResult(new CoordinatesDto("37.6204", "55.7539"));

        CoordinatesDto dto = client.getCoordinates("Moscow");

        assertNotNull(dto);
        assertTrue(client.lastUri.contains("q=Moscow"));
        assertTrue(client.lastUri.contains("limit=1"));
        assertTrue(client.lastUri.contains("appid=TEST_KEY"));
        assertTrue(client.lastUri.startsWith("http://api.openweathermap.org/geo/1.0/direct"));
    }

    @Test
    public void getWeatherInfoShouldCallGetCoordinatesThenCorrectWeatherUrl() throws WeatherIOException, WeatherServerException {
        TestableClient client = new TestableClient("TEST_KEY");

        client.setNextCoordinatesResult(new CoordinatesDto("37.6204", "55.7539"));
        CoordinatesDto coords = client.getCoordinates("Moscow");

        assertEquals("37.6204", coords.longitudeRaw());
        assertEquals("55.7539", coords.latitudeRaw());

        client.setNextWeatherResult(new WeatherInfoDto(
            "Clouds",
            "overcast",
            268.0,
            263.0,
            8000,
            4.0,
            1000L,
            1001L,
            1002L,
            10800,
            "Moscow"
        ));

        WeatherInfoDto weather = client.getWeatherInfo("Moscow");

        assertNotNull(weather);
        assertTrue(client.lastUri.contains("lat=55.7539"));
        assertTrue(client.lastUri.contains("lon=37.6204"));
        assertTrue(client.lastUri.contains("appid=TEST_KEY"));
        assertTrue(client.lastUri.startsWith("https://api.openweathermap.org/data/2.5/weather"));
    }
}