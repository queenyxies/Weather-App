package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText et_city;
    TextView tv_temp, tv_minTemp, tv_maxTemp, tv_humid, tv_weather, tv_des, tv_date;
    Button btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        function();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        tv_date.setText(currentDate);

    }

    private void initialize() {
        et_city = findViewById(R.id.et_city);
        tv_temp = findViewById(R.id.tv_temp);
        tv_minTemp = findViewById(R.id.tv_minTemp);
        tv_maxTemp = findViewById(R.id.tv_maxTemp);
        tv_humid = findViewById(R.id.tv_humid);
        tv_weather = findViewById(R.id.tv_weather);
        tv_des = findViewById(R.id.tv_des);
        btn_clear = findViewById(R.id.btn_clear);
        tv_date = findViewById(R.id.tv_date);

        // Add location and search icons to the EditText
        Drawable locationIcon = getResources().getDrawable(R.drawable.location_ic);
        Drawable searchIcon = getResources().getDrawable(R.drawable.search_ic);
        et_city.setCompoundDrawablesRelativeWithIntrinsicBounds(locationIcon, null, searchIcon, null);
    }




    private void function() {
        et_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_city.getRight() - et_city.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Handle search button click here
                        performSearch();
                        return true;
                    }
                }
                return false;
            }
        });

        btn_clear.setOnClickListener(v -> {
            tv_temp.setText(null);
            tv_minTemp.setText(null);
            tv_maxTemp.setText(null);
            tv_humid.setText(null);
            tv_weather.setText(null);
            tv_des.setText(null);
        });
    }

    private void performSearch() {
        String city = et_city.getText().toString();
        String appid = "b5b3a5b2cd1493ce23ec5bc17dde1aba";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + appid;

        RequestQueue q = Volley.newRequestQueue(this);
        q.start();

        JsonObjectRequest data = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject obj = response.getJSONObject("main");
                        JSONArray array = response.getJSONArray("weather");
                        JSONObject o = array.getJSONObject(0);

                        String temp = obj.getString("temp");
                        String min = obj.getString("temp_min");
                        String max = obj.getString("temp_max");
                        String humid = obj.getString("humidity");

                        String weather = o.getString("main");
                        String desc = o.getString("description");

                        tv_temp.setText(temp);
                        tv_minTemp.setText(min);
                        tv_maxTemp.setText(max);
                        tv_humid.setText(humid + "%");
                        tv_weather.setText(weather);
                        tv_des.setText(desc);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                });

        q.add(data);
    }
}
