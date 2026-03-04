package com.example.kinderkingdom;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class teachers2 extends AppCompatActivity {
    String userId,collection;
    TextSpeaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teachers2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        speaker = new TextSpeaker(this);

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

    }

    public void teachersinformation(View view) {
        if (isVoiceEnabled()) speaker.speak("Teachers Information");
        Intent teachersinformation =new Intent(teachers2.this, teacherinformationforteachers.class);
        teachersinformation. putExtra("userId",userId);
        teachersinformation.putExtra("collection",collection);
        startActivity(teachersinformation);
    }
    public void studentsinformation(View view) {
        if (isVoiceEnabled()) speaker.speak("Students Information");
        Intent studentinformationpage =new Intent(teachers2.this, studentinformationpage.class);
        studentinformationpage .putExtra("userId",userId);
        studentinformationpage .putExtra("collection",collection);
        startActivity( studentinformationpage);
    }

    public void academicnformation(View view) {
        if (isVoiceEnabled()) speaker.speak("Academic Information");
        Intent  academicnformation =new Intent(teachers2.this, academicinformation2.class);
        academicnformation.putExtra("userId",userId);
        academicnformation.putExtra("collection",collection);
        startActivity( academicnformation);
    }

    public void Messages(View view) {
        if (isVoiceEnabled()) speaker.speak("Messages");
        Intent Messages =new Intent(teachers2.this, messagesofteachers.class);
        Messages.putExtra("userId",userId);
        Messages.putExtra("collection",collection);
        startActivity(Messages);
    }

    public void bustour(View view) {
        if (isVoiceEnabled()) speaker.speak("Bus Tour");
        Intent bustour =new Intent(teachers2.this, bustour.class);
        bustour.putExtra("userId",userId);
        bustour.putExtra("collection",collection);
        startActivity(bustour);
    }

    public void FeedbackandNotess(View view) {
        if (isVoiceEnabled()) speaker.speak("Feedback and Notes");
        Intent FeedbackandNotes =new Intent(teachers2.this, feedbackandnotesofteachers.class);
        FeedbackandNotes.putExtra("userId",userId);
        FeedbackandNotes.putExtra("collection",collection);
        startActivity(FeedbackandNotes);
    }



    public void dailymenuu(View view) {
        if (isVoiceEnabled()) speaker.speak("Daily Menu");
        Intent dailymenuu =new Intent(teachers2.this,  dailymenu.class);
        dailymenuu .putExtra("userId",userId);
        dailymenuu .putExtra("collection",collection);
        startActivity( dailymenuu);
    }


    public void attendanceandabsenceofteachers(View view) {
        if (isVoiceEnabled()) speaker.speak("Attendance and Absence");
        Intent attendanceandabsenceofteachers =new Intent(teachers2.this,  attendanceandabsenceofteachers.class);
        attendanceandabsenceofteachers .putExtra("userId",userId);
        attendanceandabsenceofteachers.putExtra("collection",collection);
        startActivity( attendanceandabsenceofteachers);

    }

    public void work(View view) {
        if (isVoiceEnabled()) speaker.speak("Work");
        Intent work =new Intent(teachers2.this,  work.class);
        work  .putExtra("userId",userId);
        work .putExtra("collection",collection);
        startActivity(work);
    }


}