package com.olczyk.android.myweatherapp2.Utils;


import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultString("")
    String weatherJson();

    @DefaultLong(-1)
    long lastWeatherUpdate();
}
