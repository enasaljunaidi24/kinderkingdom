package com.example.kinderkingdom;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class welcomeepage extends AppCompatActivity {
    TextSpeaker speaker;
    TextView welcome;
    String userId, collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcomeepage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speaker = new TextSpeaker(this);
        welcome = findViewById(R.id.welcome);

        // جلب userId و collection مرة واحدة فقط
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        if(userId == null || collection == null){
            Toast.makeText(this, "Error: Missing user data!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ⏳ انتقال تلقائي بعد ثانيتين
        new Handler().postDelayed(this::openNextPage, 2000);

        // ----- إعداد الدروب مينيو -----
        ImageView settingView = findViewById(R.id.settingView);
        if (settingView != null) {
            settingView.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(this, v);
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

        // تشغيل الصوت عند الضغط على النص
        welcome.setOnClickListener(v -> {
            if (isVoiceEnabled()) speaker.speak(welcome.getText().toString());
        });
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

    // ---------- الصوت ----------
    private void toggleVoice() {
        boolean voiceEnabled = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("voice_enabled", false);
        boolean newValue = !voiceEnabled;
        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("voice_enabled", newValue)
                .apply();

        Toast.makeText(this,
                newValue ? "Voice Enabled" : "Voice Disabled",
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
    }

    // دالة ثابتة تفتح الصفحة المناسبة
    private void openNextPage() {
        Intent i;

        switch (collection) {
            case "children":
                i = new Intent(this, students.class);
                break;
            case "teachers":
                i = new Intent(this, teachers.class);
                break;
            case "managers":
                i = new Intent(this, managers.class);
                break;
            case "drivers":
                i = new Intent(this, drivers.class);
                break;
            case "security":
                i = new Intent(this, security.class);
                break;
            case "helpers":
                i = new Intent(this, helpers.class);
                break;
            case "bussupervisor":
                i = new Intent(this, bussupervisor.class);
                break;
            default:
                i = new Intent(this, mychild.class);
        }

        i.putExtra("userId", userId);
        i.putExtra("collection", collection);
        startActivity(i);
    }

    public void welcome(View view) {
        if (isVoiceEnabled() && speaker != null) speaker.speak(welcome.getText().toString());
        openNextPage();
    }
}
