package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class teachers extends AppCompatActivity {
    String userId, collection;
    TextSpeaker speaker;
    TextView mypage;
    TextView students;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teachers);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speaker = new TextSpeaker(this);
        mypage = findViewById(R.id.mypage);
        students = findViewById(R.id.students);

        // جلب البيانات من intent
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        if(userId == null || collection == null){
            Toast.makeText(this, "User data missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toast.makeText(this, "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_LONG).show();

        // ---- دروب مينيو ----
        ImageView settingView = findViewById(R.id.settingView);
        if (settingView != null) {
            settingView.setOnClickListener(v -> {
                androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(this, v);
                popup.getMenuInflater().inflate(R.menu.settingsmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.menu_change_language) {
                        changeLanguage();
                        return true;
                    } else if (id == R.id.menu_enable_voice) {
                        toggleVoice();
                        return true;
                    } else if (id == R.id.menu_about) {
                        Toast.makeText(this, "About App", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.menu_contact) {
                        Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });

                popup.show();
            });
        }
    }

    private void changeLanguage() {
        String lang = getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        String newLang = lang.equals("en") ? "ar" : "en";

        getSharedPreferences("settings", MODE_PRIVATE)
                .edit().putString("lang", newLang).apply();

        Locale locale = new Locale(newLang);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate();
    }

    private void toggleVoice() {
        boolean enabled = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("voice_enabled", false);

        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("voice_enabled", !enabled)
                .apply();

        Toast.makeText(this,
                !enabled ? "Voice Enabled" : "Voice Disabled",
                Toast.LENGTH_SHORT).show();
    }


    protected boolean isVoiceEnabled() {
        return getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("voice_enabled", false);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");
        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));
        super.attachBaseContext(newBase.createConfigurationContext(config));
    }

    public void mypage(View view) {
        if (isVoiceEnabled()) speaker.speak(mypage.getText().toString());
        Intent mypage = new Intent(teachers.this, teachers2.class);
        mypage.putExtra("userId", userId);
        mypage.putExtra("collection", collection);
        startActivity(mypage);
    }

    public void students(View view) {
        if (isVoiceEnabled()) speaker.speak(students.getText().toString());
        Intent students = new Intent(teachers.this, studentmychild.class);
        students.putExtra("userId", userId);
        students.putExtra("collection", collection);
        startActivity(students);
    }
}
