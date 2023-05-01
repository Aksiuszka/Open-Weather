package com.example.openweather;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etCity;
    TextView tvResult;
    ImageButton imgWeatherIcon;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";
    String output = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = (EditText) findViewById(R.id.etCity);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvResult.setPadding(16, 16, 16, 16);
        imgWeatherIcon = (ImageButton) findViewById(R.id.weatherIconImageView);
    }

    public void getWeatherDetails(View view) {
        String city = etCity.getText().toString().trim();
        String apiUrl = "";
        if (city.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.brak, Toast.LENGTH_SHORT).show();
        } else {
            apiUrl = url + "?q=" + city + "&units=metric&appid=" + appid;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        double feelsLike = jsonObjectMain.getDouble("feels_like");
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        String cityName = jsonObject.getString("name");
                        output += "bieżąca pogoda w mieście: " + cityName + "\n temperatura: " + temp + "\n temperature odczuwalna: " + feelsLike + "\n wilgotność: " + humidity + "\n opis: " + description + "\n wiatr: " + " m/s " + "\n zachmurzenie: " + clouds + " % " + "\n ciśnienie " + pressure + "hpa";
                        tvResult.setText(output);

                        // Set the weather icon
                        String iconCode = jsonObjectWeather.getString("icon");
                        int iconResourceId = getIconResourceId(iconCode);
                        imgWeatherIcon.setImageResource(iconResourceId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    clearResult();
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private int getIconResourceId(String iconCode) {
        switch (iconCode) {
            case "01d":
                return R.drawable.sun;
            case "02d":
                return R.drawable.cloudy;
            // Add more cases for other weather conditions
            default:
                return R.drawable.unknown;
        }
    }

    public void clearResult() {
        output = " ";
        tvResult.setText(output);
        tvResult.setBackgroundColor(0x00FFFFFF);
    }
}