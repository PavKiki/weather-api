package ru.peabdulkin;

import ru.peabdulkin.exception.WeatherSdkException;
import ru.peabdulkin.mode.SdkMode;

public class ExamplesMain {
    public static void main(String[] args) {
        try {
            var wClient = WeatherClientFactory.createOpenWeatherClient("", SdkMode.POLLING);
            var weather = wClient.getWeather("Yantikovo");
            System.out.println(weather);
        } catch (WeatherSdkException e) {
            System.out.println(e.getMessage());
        }
    }
}
