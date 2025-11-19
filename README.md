# Weather SDK

## Overview

**Weather SDK** is a simple and convenient Java client for retrieving current weather by city name using the OpenWeather API.  
The SDK supports:

- caching
- **ON_DEMAND** and **POLLING** modes
- protection against creating multiple clients with the same API key
- a standardized JSON response format

The SDK is designed to be extremely easy to use: you can get started in just a couple of lines of code.

---

# Quick Start

~~~java
import ru.peabdulkin.*;
import ru.peabdulkin.mode.SdkMode;

public class Example {
    public static void main(String[] args) throws Exception {
        WeatherClient client =
                WeatherClientFactory.createOpenWeatherClient("YOUR_API_KEY", SdkMode.ON_DEMAND);

        String weatherJson = client.getWeather("Zocca");
        System.out.println(weatherJson);

        client.shutdown(); // optional for ON_DEMAND
    }
}
~~~

Example output:

~~~json
{
  "weather": {
    "main": "Clouds",
    "description": "scattered clouds",
  },
  "temperature": {
    "temp": 269.6,
    "feels_like": 267.57,
  },
  "visibility": 10000,
  "wind": {
    "speed": 1.38,
  },
  "datetime": 1675744800,
  "sys": {
    "sunrise": 1675751262,
    "sunset": 1675787560
  },
  "timezone": 3600,
  "name": "Zocca"
}
~~~

---

# Installation

### Gradle

~~~groovy
implementation("ru.peabdulkin.weather-api:weather-sdk:1.0.0")
~~~

### Maven

~~~xml
<dependency>
    <groupId>ru.peabdulkin.weather-api</groupId>
    <artifactId>weather-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
~~~

---

# SDK Modes

## ON_DEMAND

- API calls are performed **only on demand** when `getWeather` is called.
- Minimal network and API load.
- Suitable for most applications.

## POLLING

- The SDK periodically refreshes the cache every 10 minutes.
- Calls to `getWeather()` return instantly from the cache (zero-latency).
- Uses a background `ScheduledExecutorService`.

~~~java
client.shutdown(); // required for graceful shutdown in POLLING mode
~~~

---

# Caching

The SDK uses an LRU cache with the following rules:

- maximum of **10 cities**
- TTL = **10 minutes**
- expired entries are refreshed on demand or by polling
- when the cache is full, the least recently used entries are evicted

---

# API Key Policy

`WeatherClientFactory` guarantees:

- API keys are normalized (`trim().toLowerCase()`)
- **it is forbidden to create two clients with the same key**
- attempting to do so will throw `WeatherSdkException`

---

# Error Handling

The SDK may throw the following exceptions:

### `WeatherIOException`

Network-related issues, DNS problems, timeouts, or malformed responses.

### `WeatherServerException`

OpenWeather responded with an HTTP error (4xx/5xx).

### `WeatherSdkException`

Incorrect use of the SDK, such as trying to create multiple clients with the same API key.

All exceptions contain descriptive messages with the reason.

---

# Shutdown

Stop the scheduler for a single client:

~~~java
client.shutdown();
~~~

Stop **all** clients and free resources:

~~~java
WeatherClientFactory.peacefulShutdown();
~~~

---

# Example — POLLING Mode

~~~java
import ru.peabdulkin.*;
import ru.peabdulkin.mode.SdkMode;

public class PollingExample {
    public static void main(String[] args) throws Exception {
        WeatherClient client =
                WeatherClientFactory.createOpenWeatherClient("YOUR_API_KEY", SdkMode.POLLING);

        System.out.println(client.getWeather("Moscow")); // instant response

        Runtime.getRuntime().addShutdownHook(new Thread(client::shutdown));
    }
}
~~~

---

# 🧩 JSON Format Returned by the SDK

~~~json
{
  "weather": {
    "main": ...,
    "description": ...,
  },
  "temperature": {
    "temp": ...,
    "feels_like": ...,
  },
  "visibility": ...,
  "wind": {
    "speed": ...,
  },
  "datetime": ...,
  "sys": {
    "sunrise": ...,
    "sunset": ...
  },
  "timezone": ...,
  "name": ...
}
~~~

This is a **normalized SDK format**, not the raw OpenWeather response.  
It is designed to be convenient for further processing.