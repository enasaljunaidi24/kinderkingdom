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

public class drivers extends AppCompatActivity {
    String userId, collection;
    TextSpeaker speaker;
    CardView cardInfo, cardAtten, cardBus;
    TextView Information, AttendanceAbsences, BusTour;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drivers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        speaker = new TextSpeaker(this);

        // ربط العناصر
        cardInfo = findViewById(R.id.card_information);
        cardAtten = findViewById(R.id.card_attendance2);
        cardBus = findViewById(R.id.card_bus);

        Information = findViewById(R.id.information);
        AttendanceAbsences = findViewById(R.id.AttendanceAbsences);
        BusTour = findViewById(R.id.BusTour);

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

    // ---------- تغيير اللغة ----------
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

    // ---------- attachBaseContext لتفعيل اللغة عند التشغيل ----------
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);


        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        Toast.makeText(this, "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_LONG).show();


// التأكد من القيم عبر Toast
        Toast.makeText(this, "userId: " + userId + "\ncollection: " + collection, Toast.LENGTH_LONG).show();

    }


    public void informationofdrivers(View view) {
        speaker.speak(Information.getText().toString());
        Intent informationofdrivers = new Intent(drivers.this, driversinformation.class);
        informationofdrivers.putExtra("userId", userId);
        informationofdrivers.putExtra("collection", collection);
        startActivity(informationofdrivers);
    }

    public void attendanceandabsenceofdrivers(View view) {
        speaker.speak(AttendanceAbsences.getText().toString());
        Intent attendanceandabsenceofdrivers = new Intent(drivers.this, attendanceandabsenceofdrivers.class);
        attendanceandabsenceofdrivers.putExtra("userId", userId);
        attendanceandabsenceofdrivers.putExtra("collection", collection);
        startActivity(attendanceandabsenceofdrivers);
    }

    public void bustour(View view) {
        speaker.speak(BusTour.getText().toString());
        Intent bustour = new Intent(drivers.this, bustour.class);
        bustour.putExtra("userId", userId);
        bustour.putExtra("collection", collection);
        startActivity(bustour);


    }

}

