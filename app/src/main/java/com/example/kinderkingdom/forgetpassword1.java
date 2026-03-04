package com.example.kinderkingdom;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class forgetpassword1 extends AppCompatActivity {
    TextSpeaker speaker;
    Button button3, button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgetpassword1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        speaker = new TextSpeaker(this);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        // ===== DROPDOWN MENU =====
        ImageView settingView = findViewById(R.id.settingView);

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
    // ================= LANGUAGE =================
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

        recreate(); // إعادة تحميل الواجهة
    }

    // ================= APPLY LANGUAGE =================
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));

        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }
    public void continuee(View view) {
        if (isVoiceEnabled())
            speaker.speak(button3.getText().toString());
        // المفروض يرجعهم حسب هم شو اختاروا من الويلكم بيج
        Intent continuee=new Intent(forgetpassword1.this, welcomeepage.class);
        startActivity(continuee);
    }

    public void searchbyemail(View view) {
        if (isVoiceEnabled())
            speaker.speak(button4.getText().toString());
        Intent searchbyemail =new Intent(forgetpassword1.this, forgetpassword2.class);
        startActivity(searchbyemail);
    }
public void setting(View view) {

}
}
