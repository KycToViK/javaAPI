package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final String API_KEY = "73f99d860154dece4105f8dcf6eecd7e";

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Boolean isEnable = true;

        while (isEnable) {
            System.out.println("Введите город: ");

            String city = scanner.next();

            if(city.equals("Выход"))
                return;


            try {
                String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=ru";
                URL url = new URL(urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == 404) {
                    System.out.println("Not Found: Город не найден.");

                } else if (responseCode == 401) {
                    System.out.println("Unauthorized: Указывает на проблемы с аутентификацией. Это происходит, если API-ключ недействителен или отсутствует.");
                    return;
                }
                else if (responseCode == 400){
                    System.out.println("Bad Request: Ошибка возникает, если запрос неправильно сформирован (например, неверный формат URL или неправильные параметры).");
                }
                else if (responseCode == 500){
                    System.out.println("Internal Server Error: Ошибка на сервере, которая может указывать на временные проблемы с API.");

                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                conn.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject main = jsonResponse.getJSONObject("main");
                JSONObject weathers = jsonResponse.getJSONArray("weather").getJSONObject(0);
                JSONObject wind = jsonResponse.getJSONObject("wind");

                double temperature = main.getDouble("temp");
                String description = weathers.getString("description");
                int wetness = main.getInt("humidity");
                double speedWind = wind.getDouble("speed");

                System.out.println("Погода в " + city + ":");
                System.out.println("Описание: " + description);
                System.out.println("Температура: " + temperature + "'C");
                System.out.println("Влажность: " + wetness + "%");
                System.out.println("Скорость ветра: " + speedWind + " м/с");


            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}