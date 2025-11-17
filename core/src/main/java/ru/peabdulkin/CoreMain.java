package ru.peabdulkin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.peabdulkin.http.OpenWeatherApiClient;

import static ru.peabdulkin.http.HttpUtils.DEFAULT_HTTP_CLIENT;

public class CoreMain {

    private static final Logger log = LoggerFactory.getLogger(CoreMain.class);

    public static void main(String[] args) {
        var client = new OpenWeatherApiClient(DEFAULT_HTTP_CLIENT, "");
        var sdk = new WeatherSdk(client, SdkMode.ON_DEMAND);
        try {
            log.info(sdk.getWeather("Moscow"));
        } catch (Exception e) {
            log.error("Error", e);
        }
    }
}
