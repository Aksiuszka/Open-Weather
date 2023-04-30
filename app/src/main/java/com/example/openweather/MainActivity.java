package com.example.openweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etCity;
    TextView tvResult;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = (EditText) findViewById(R.id.etCity);
        tvResult= (TextView) findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String city = etCity.getText().toString().trim();
        String apiUrl="";
        if(city.equals("")){
            Toast.makeText(getApplicationContext(), R.string.brak, Toast.LENGTH_SHORT).show();
        }else{
            apiUrl = url + "?q="+city+"&units=metric&appid="+appid;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output="";
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            })
        }
    }
}