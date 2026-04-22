package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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

public class managers extends AppCompatActivity {

    TextSpeaker speaker;
    String userId, collection;
    TextView students, Teachers, drivers, security, helpers, bustour, messages, fedback;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_managers);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speaker = new TextSpeaker(this);
        students = findViewById(R.id.students);
        Teachers = findViewById(R.id.Teachers);
        drivers = findViewById(R.id.drivers);
        helpers = findViewById(R.id.helpers);
        bustour = findViewById(R.id.bustour);
        messages = findViewById(R.id.messages);
        fedback = findViewById(R.id.fedback);
        security = findViewById(R.id.security);
        //  المكان الصحيح لجلب القيم
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

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

//        // تشغيل الصوت عند الضغط على أي صورة
//        View.OnClickListener speakListener = v -> {
//            if (isVoiceEnabled() && speaker != null && v instanceof ImageView) {
//                ImageView img = (ImageView) v;
//                CharSequence desc = img.getContentDescription();
//                if (desc != null) {
//                    speaker.speak(desc.toString());
//                }
//            }
//        };
//
//        students.setOnClickListener(speakListener);
//        Teachers.setOnClickListener(speakListener);
//        drivers.setOnClickListener(speakListener);
//        security.setOnClickListener(speakListener);
//        helpers.setOnClickListener(speakListener);
//        bustour.setOnClickListener(speakListener);
//        messages.setOnClickListener(speakListener);
//        fedback.setOnClickListener(speakListener);
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

        recreate();// تحديث الصفحة
    }
    // ---------- اللغة عند التشغيل ----------
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    // ---------- الصوت ----------
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




    // ---------- التنقل ----------
    public void securitybutton(View view) {
        if (isVoiceEnabled()) speaker.speak(security.getText().toString());

        Intent securitybutton = new Intent(managers.this, security.class);
        securitybutton .putExtra("userId", userId);
        securitybutton .putExtra("collection", collection);
        startActivity(securitybutton );
    }

    public void helpersbutton(View view) {
        if (isVoiceEnabled()) speaker.speak(helpers.getText().toString());
        Intent helpersbutton = new Intent(managers.this, helpers.class);
        helpersbutton.putExtra("userId", userId);
        helpersbutton.putExtra("collection", collection);
        startActivity(helpersbutton);


    }

    public void driversbutton(View view) {
        if (isVoiceEnabled()) speaker.speak(drivers.getText().toString());
        Intent driversbutton = new Intent(managers.this, drivers.class);
        driversbutton.putExtra("userId", userId);
        driversbutton.putExtra("collection", collection);
        startActivity(driversbutton);


    }

    public void studentsbutton(View view) {
        if (isVoiceEnabled()) speaker.speak(students.getText().toString());
        Intent studentsbutton = new Intent(managers.this, students.class);
        studentsbutton.putExtra("userId", userId);
        studentsbutton.putExtra("collection", collection);
        startActivity(studentsbutton);


    }

    public void teachersbutton(View view) {
        if (isVoiceEnabled()) speaker.speak(Teachers.getText().toString());
        Intent teachersbutton = new Intent(managers.this, teachers2.class);
        teachersbutton.putExtra("userId", userId);
        teachersbutton.putExtra("collection", collection);
        startActivity(teachersbutton);


    }

    public void bustour(View view)
    { if (isVoiceEnabled()) speaker.speak(bustour.getText().toString());
        Intent bustour = new Intent(managers.this, bustour.class);
        bustour.putExtra("userId", userId);
        bustour.putExtra("collection", collection);
        startActivity(bustour);

    }

    public void messagebutton(View view) {
        if (isVoiceEnabled()) speaker.speak(messages.getText().toString());
        Intent messagebutton = new Intent(managers.this, messagesofmanagers.class);
        messagebutton.putExtra("userId", userId);
        messagebutton.putExtra("collection", collection);
        startActivity(messagebutton);

    }

    public void feedbackandnotes(View view) {
        if (isVoiceEnabled()) speaker.speak(fedback.getText().toString());
        Intent feedbackandnotes = new Intent(managers.this, feedbackandnotesofmanagers.class);
        feedbackandnotes.putExtra("userId", userId);
        feedbackandnotes.putExtra("collection", collection);
        startActivity(feedbackandnotes);


    }
}
