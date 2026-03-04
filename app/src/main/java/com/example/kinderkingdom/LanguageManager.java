package com.example.kinderkingdom;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageManager {

    private Context context;

    public LanguageManager(Context context) {
        this.context = context;
    }

    public void setLanguage(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
