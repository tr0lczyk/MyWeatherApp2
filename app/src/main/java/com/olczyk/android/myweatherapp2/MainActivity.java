package com.olczyk.android.myweatherapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.olczyk.android.myweatherapp2.Utils.WeatherCallback;
import com.olczyk.android.myweatherapp2.Utils.WeatherGetter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements WeatherCallback {

    private static final String OPEN_WEATHER_CURRENT ="http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&cnt=7&lang=pl";

    List<Integer> temperatures = new ArrayList<>();

    @ViewById
    TextView temptext;

    @Bean
    WeatherGetter weatherGetter;

    @AfterViews
    public void aVoid(){
        weatherGetter.getWeatherForecast(this, this);
    }

    @Override
    public void onWeatherLoaded(int result) {
        for(int i = 0; i < 7 ; i++){
            temperatures.add(weatherGetter.getDayTemp(i));
            afterLoad();
        }
    }

    private void afterLoad() {
        temptext.setText(""+ temperatures.get(0));
    }
}
