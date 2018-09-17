package com.olczyk.android.myweatherapp2.Utils;


import android.content.Context;
import android.util.Log;

import com.olczyk.android.myweatherapp2.R;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

@EBean(scope = EBean.Scope.Singleton)
public class WeatherGetter {

    private static final String OPEN_WEATHER_CURRENT ="http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&cnt=7&lang=pl";

    String lat = "53.0948800";
    String lng = "23.6684800";

    @Pref
    Preferences_ preferences;

    @Background
    public void getWeatherForecast(Context context, WeatherCallback callback){

        long delta = System.currentTimeMillis()-preferences.lastWeatherUpdate().get();
        if(preferences.weatherJson().equals("") || delta < 14400000){
            try {
                URL url = new URL(String.format(OPEN_WEATHER_CURRENT,lat,lng));
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection();
                httpURLConnection.addRequestProperty("x-api-key", context.getString(R.string.ow_key));
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection
                .getInputStream()));
                StringBuffer jsonBuffer = new StringBuffer(1024);
                String temp = "";
                while((temp=reader.readLine()) != null){
                    jsonBuffer.append(temp).append("\n");
                }
                reader.close();
                JSONObject jsonObject = new JSONObject(jsonBuffer.toString());
                if(jsonObject.getInt("cod") == 200){
                    preferences.edit().weatherJson().put(jsonBuffer.toString()).apply();
                    Log.i("LOG","data" + jsonBuffer.toString());
                    preferences.edit().lastWeatherUpdate().put(System.currentTimeMillis()).apply();
                    callback.onWeatherLoaded(1);
                } else {
                    callback.onWeatherLoaded(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.onWeatherLoaded(0);
            }
        } else {
            callback.onWeatherLoaded(1);
        }

//        try {
//            URL url = new URL(String.format(OPEN_WEATHER_CURRENT,lat,lng));
//            HttpURLConnection httpURLConnection =
//                    (HttpURLConnection) url.openConnection();
//            httpURLConnection.addRequestProperty("x-api-key", context.getString(R.string.ow_key));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection
//                    .getInputStream()));
//            StringBuffer jsonBuffer = new StringBuffer(1024);
//            String temp;
//            while((temp=reader.readLine()) != null){
//                jsonBuffer.append(temp).append("\n");
//            }
//            reader.close();
//            JSONObject jsonObject = new JSONObject(jsonBuffer.toString());
//            if(jsonObject.getInt("cod") == 200){
//                preferences.edit().weatherJson().put(jsonBuffer.toString()).apply();
//                Log.i("LOG","data" + jsonBuffer.toString());
//                preferences.edit().lastWeatherUpdate().put(System.currentTimeMillis()).apply();
//                callback.onWeatherLoaded(1);
//            } else {
//                callback.onWeatherLoaded(0);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            callback.onWeatherLoaded(0);
//        }
    }

    public int getDayTemp(int day){
        double dayTemp = 0;
        try {
            JSONObject data = new JSONObject(preferences.weatherJson().get());
            JSONArray dataArray = data.getJSONArray("list");
            JSONObject arrayObject = (JSONObject) dataArray.get(day);
            JSONObject objectTemp = arrayObject.getJSONObject("temp");
            dayTemp = objectTemp.getDouble("day");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return kelvinToCelcius(dayTemp);
    }

    public int kelvinToCelcius(Double input){
        Log.i("LOG", "this is double: "+ input);
        int result = (int) (input - 273.15);
        return result;
    }

    public JSONObject listElementAcess(int day){
        JSONObject arrayObject = null;
        try {
            JSONObject data = new JSONObject(preferences.weatherJson().get());
            JSONArray dataArray = data.getJSONArray("list");
            arrayObject = (JSONObject) dataArray.get(day);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayObject;
    }
}
