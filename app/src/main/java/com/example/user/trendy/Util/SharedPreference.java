package com.example.user.trendy.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;



public class SharedPreference {
    public static final String Data = "hello";


    public static void saveData(String key, String value , Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Data, Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getData(String key, Context context){
        SharedPreferences prefs = context.getSharedPreferences(Data, Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }
    public static void saveArrayData(String key, ArrayList value , Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Data, Activity.MODE_PRIVATE).edit();
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }
    public static String getArrayData(String key, Context context){
        SharedPreferences prefs = context.getSharedPreferences(Data, Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void saveIntData(String key, Integer value , Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Data, Activity.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntData(String key,Context context){
        SharedPreferences prefs = context.getSharedPreferences(Data, Activity.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public static void saveBooleanData(String key, Boolean value , Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Data, Activity.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getBooleanData(String key,Context context){
        SharedPreferences prefs = context.getSharedPreferences(Data, Activity.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static void clearSession(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Data, Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static void removeStringData(String key, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Data, Activity.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.apply();
    }
}
