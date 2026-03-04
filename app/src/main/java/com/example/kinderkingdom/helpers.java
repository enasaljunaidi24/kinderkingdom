package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class helpers extends AppCompatActivity {
    String userId, collection;
    TextSpeaker speaker;
    CardView cardInformation, cardAttendanceAbsences;
    TextView txt_information, txt_AttendanceAbsences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helpers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        speaker = new TextSpeaker(this);

        cardInformation = findViewById(R.id.card_information);
        cardAttendanceAbsences = findViewById(R.id.card_attendance2);

        txt_information = findViewById(R.id.txt_information);
        txt_AttendanceAbsences = findViewById(R.id.txt_AttendanceAbsences);
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        // عند الضغط على كارد المعلومات
        cardInformation.setOnClickListener(v -> {
            speak(txt_information.getText().toString());
            startActivity(new android.content.Intent(this, helpers.class));
        });

        // عند الضغط على كارد الحضور والغياب
        cardAttendanceAbsences.setOnClickListener(v -> {
            speak(txt_AttendanceAbsences.getText().toString());
            startActivity(new android.content.Intent(this, attendanceandabsenceofhelpers.class));
        });

        setupSettingsMenu();

// عرض القيم للتأكد
        Toast.makeText(this, "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_LONG).

                show();
    }
    // ======== Settings Popup Menu ==========
    private void setupSettingsMenu() {
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
                        android.widget.Toast.makeText(this, "About App", android.widget.Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.menu_contact) {
                        android.widget.Toast.makeText(this, "Contact Us", android.widget.Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                });

                popup.show();
            });
        }
    }

    // ======= تغيير اللغة =======
    private void changeLanguage() {
        String lang = getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        String newLang = lang.equals("en") ? "ar" : "en";

        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putString("lang", newLang)
                .apply();

        Locale locale = new Locale(newLang);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        recreate();
    }
    // ====== تشغيل وإيقاف الصوت ======
    private void toggleVoice() {
        boolean voiceEnabled = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("voice_enabled", false);

        boolean newValue = !voiceEnabled;

        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("voice_enabled", newValue)
                .apply();

        android.widget.Toast.makeText(this,
                newValue ? "Voice Enabled" : "Voice Disabled",
                android.widget.Toast.LENGTH_SHORT).show();
    }

    private void speak(String text) {
        if (isVoiceEnabled()) speaker.speak(text);
    }

    private boolean isVoiceEnabled() {
        return getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("voice_enabled", false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    public void informationofhelpers(View view) {
        if (isVoiceEnabled()) speaker.speak(txt_information.getText().toString());
        Intent informationofhelpers =new Intent(helpers.this, helpersinformation.class);
        informationofhelpers.putExtra("userId",userId);
        informationofhelpers.putExtra("collection",collection);
        startActivity(informationofhelpers);
    }

    public void attendanceandabsenceofhelpers(View view) {
        if (isVoiceEnabled()) speaker.speak(txt_AttendanceAbsences.getText().toString());
        Intent attendanceandabsenceofhelpers =new Intent(helpers.this,  attendanceandabsenceofhelpers.class);
        attendanceandabsenceofhelpers .putExtra("userId",userId);
        attendanceandabsenceofhelpers .putExtra("collection",collection);
        startActivity(attendanceandabsenceofhelpers );
    }
}