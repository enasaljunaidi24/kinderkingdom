package com.example.kinderkingdom;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class page1 extends AppCompatActivity {

    TextSpeaker speaker;
    Button button;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView settingView = findViewById(R.id.settingView);

        // ---- DROP MENU ----
        settingView.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.settingsmenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {

                int id = item.getItemId();

                if (id == R.id.menu_change_language) {
                    changeLanguage();
                    return true;
                }
                else if (id == R.id.menu_enable_voice) {
                    toggleVoice();
                    return true;
                }
                else if (id == R.id.menu_about) {
                    Toast.makeText(this, "About App", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (id == R.id.menu_contact) {
                    Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            });

            popup.show();
        });

        speaker = new TextSpeaker(this);

        TextView title = findViewById(R.id.textView3);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        title.setOnClickListener(v -> {
            if (isVoiceEnabled())
                speaker.speak(title.getText().toString());
        });
    }

    // ---------------------- اللغة -------------------------
    private void changeLanguage() {
        String lang = getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        String newLang = lang.equals("en") ? "ar" : "en";

        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putString("lang", newLang)
                .apply();

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

    // ---------------------- اللغة عند التشغيل ---------------------
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));

        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    public void SignUp(View view) {
        if (isVoiceEnabled())
            speaker.speak(button.getText().toString());

        Intent page1tosignup = new Intent(page1.this, signup.class);
        startActivity(page1tosignup);
    }

    public void LogIn(View view) {
        if (isVoiceEnabled())
            speaker.speak(button2.getText().toString());

        Intent page1tologin = new Intent(page1.this, login.class);
        startActivity(page1tologin);
    }
}
