package ru.peabdulkin;

import org.junit.jupiter.api.Test;
import ru.peabdulkin.cache.WeatherCache;
import ru.peabdulkin.exception.WeatherIOException;
import ru.peabdulkin.exception.WeatherServerException;
import ru.peabdulkin.http.WeatherApiClient;
import ru.peabdulkin.http.dto.WeatherInfoDto;
import ru.peabdulkin.mode.SdkMode;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherClientImplTest {

    public static class FakeWeatherApiClient implements WeatherApiClient {

        private WeatherInfoDto nextResult;
        private int callCount = 0;
        private String lastCity = null;

        public void setNextResult(WeatherInfoDto nextResult) {
            this.nextResult = nextResult;
        }

        public void setCallCount(int callCount) {
            this.callCount = callCount;
        }

        public int getCallCount() {
            return callCount;
        }

        public String getLastCity() {
            return lastCity;
        }

        @Override
        public WeatherInfoDto getWeatherInfo(String city) {
            callCount++;
            lastCity = city;
            return nextResult;
        }
    }

    private WeatherInfoDto weather(String name) {
        return new WeatherInfoDto(
            "Clouds",
            "overcast",
            270,
            268,
            8000,
            5,
            1000,
            1001,
            1002,
            10800,
            name
        );
    }

    @Test
    void shouldCallApiOnCacheMiss() throws WeatherIOException, WeatherServerException {
        FakeWeatherApiClient api = new FakeWeatherApiClient();
        WeatherCache cache = new WeatherCache(10, 10);

        api.setNextResult(weather("Moscow"));

        WeatherClientImpl client = new WeatherClientImpl(api, SdkMode.ON_DEMAND, cache);

        String json = client.getWeather("Moscow");

        assertNotNull(json);
        assertEquals(1, api.getCallCount());
        assertEquals("Moscow", api.getLastCity());
        assertTrue(cache.getKeys().contains("Moscow"));
    }

    @Test
    void shouldUseCacheOnSecondRequest() throws WeatherIOException, WeatherServerException {
        FakeWeatherApiClient api = new FakeWeatherApiClient();
        WeatherCache cache = new WeatherCache(10, 10);

        api.setNextResult(weather("Moscow"));

        WeatherClientImpl client = new WeatherClientImpl(api, SdkMode.ON_DEMAND, cache);

        client.getWeather("Moscow");
        client.getWeather("Moscow");

        assertEquals(1, api.getCallCount());
    }

    @Test
    void shouldReturnValidJson() throws WeatherIOException, WeatherServerException {
        FakeWeatherApiClient api = new FakeWeatherApiClient();
        WeatherCache cache = new WeatherCache(10, 10);

        api.setNextResult(weather("Moscow"));

        WeatherClientImpl client = new WeatherClientImpl(api, SdkMode.ON_DEMAND, cache);

        String json = client.getWeather("Moscow");

        assertTrue(json.contains("\"name\":\"Moscow\""));
        assertTrue(json.contains("\"weather\""));
    }

    @Test
    void shouldRefetchWhenCacheExpired() throws WeatherIOException, WeatherServerException, InterruptedException {
        FakeWeatherApiClient api = new FakeWeatherApiClient();
        WeatherCache cache = new WeatherCache(10, 0);

        api.setNextResult(weather("Moscow"));

        WeatherClientImpl client = new WeatherClientImpl(api, SdkMode.ON_DEMAND, cache);

        client.getWeather("Moscow");
        client.getWeather("Moscow");

        assertEquals(2, api.getCallCount());
    }

    @Test
    void pollingModeShouldTriggerInitialPoll() throws Exception {
        FakeWeatherApiClient api = new FakeWeatherApiClient();
        WeatherCache cache = new WeatherCache(10, 10);

        cache.put("Moscow", weather("Moscow"));

        api.setNextResult(weather("Moscow"));

        WeatherClientImpl client = new WeatherClientImpl(api, SdkMode.POLLING, cache);

        Thread.sleep(50);

        assertTrue(api.getCallCount() >= 1);

        client.shutdown();
    }

    @Test
    void shutdownShouldStopScheduler() throws Exception {
        FakeWeatherApiClient api = new FakeWeatherApiClient();
        WeatherCache cache = new WeatherCache(10, 10);

        cache.put("Moscow", weather("Moscow"));
        api.setNextResult(weather("Moscow"));

        WeatherClientImpl client = new WeatherClientImpl(api, SdkMode.POLLING, cache);

        client.shutdown();

        int before = api.getCallCount();
        Thread.sleep(50);
        int after = api.getCallCount();

        assertEquals(before, after);
    }
}
