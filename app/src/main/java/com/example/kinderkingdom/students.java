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

public class students extends AppCompatActivity {

    String userId, collection;
    TextSpeaker speaker;
    TextView mychild;
    TextView Teachers;

    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));
        super.attachBaseContext(newBase.createConfigurationContext(config));
    }

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_students);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speaker = new TextSpeaker(this);
        mychild = findViewById(R.id.mychild);
        Teachers = findViewById(R.id.Teachers);

        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        if (!( "children".equals(collection) || "managers".equals(collection) )) {
            finish(); // ممنوع الدخول
            return;
        }

        if(userId == null || collection == null){
            Toast.makeText(this, "User data missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ----- دروب مينيو -----
        ImageView settingView = findViewById(R.id.settingView);
        if (settingView != null) {
            settingView.setOnClickListener(v -> {
                androidx.appcompat.widget.PopupMenu popup =
                        new androidx.appcompat.widget.PopupMenu(this, v);
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

    // ================= VOICE =================
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

    public void mychild(View view) {
        if (isVoiceEnabled()) speaker.speak(mychild.getText().toString());
        Intent intent = new Intent(students.this, mychild.class);
        intent.putExtra("userId", userId);
        intent.putExtra("collection", collection);
        startActivity(intent);
    }

    public void teachebuttonofstudents(View view) {
        if (isVoiceEnabled()) speaker.speak(Teachers.getText().toString());
        Intent intent = new Intent(students.this, teacherinformationofstudents.class);
        intent.putExtra("userId", userId);
        intent.putExtra("collection", collection);
        startActivity(intent);
    }
}
