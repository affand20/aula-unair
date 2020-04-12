package id.trydev.aulaunair.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String PREFS_NAME = "app_pref";

    private static final String ROLE = "role";

    private final SharedPreferences prefs;

    public AppPreferences(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void resetPreference(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void setRole(int role){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ROLE, role);
        editor.apply();
    }

    public int getRole(){
        return prefs.getInt(ROLE, 0);
    }


}