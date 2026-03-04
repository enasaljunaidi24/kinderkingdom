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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class mychild extends AppCompatActivity {
    String userId,collection;
    TextSpeaker speaker;
    TextView myinformation, AcademicInformationOfStudents,teachersinformation,academicinformation, RegistrationPayment, bustour, messages, FeedbackNotes,DailyMenu;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mychild);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        speaker = new TextSpeaker(this);
        myinformation = findViewById(R.id.myinformation);
        AcademicInformationOfStudents = findViewById(R.id.AcademicInformationOfStudents);
        bustour = findViewById(R.id.BusTour);
        messages = findViewById(R.id.Messages);
        FeedbackNotes = findViewById(R.id.FeedbackNotes);
        RegistrationPayment = findViewById(R.id.RegistrationPayment);
        DailyMenu = findViewById(R.id.DailyMenu);
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



// التأكد من القيم عبر Toast
        Toast.makeText(this, "userId: " + userId + "\ncollection: " + collection, Toast.LENGTH_LONG).show();

    }


    public void information(View view) {
        if (isVoiceEnabled()) speaker.speak(myinformation.getText().toString());
        Intent studentinformationpage =new Intent(mychild.this, studentinformationpage.class);
        studentinformationpage .putExtra("userId",userId);
        studentinformationpage .putExtra("collection",collection);
        startActivity( studentinformationpage);
    }

    public void academicnformation(View view) {
        if (isVoiceEnabled()) speaker.speak(AcademicInformationOfStudents.getText().toString());
        Intent  academicnformation =new Intent(mychild.this, academicinformation.class);
        academicnformation.putExtra("userId",userId);
        academicnformation.putExtra("collection",collection);
        startActivity( academicnformation);
    }

    public void Messages(View view) {
        if (isVoiceEnabled()) speaker.speak(messages.getText().toString());
        Intent Messages =new Intent(mychild.this, messageofstudents.class);
        Messages.putExtra("userId",userId);
        Messages.putExtra("collection",collection);
        startActivity(Messages);
    }

    public void bustour(View view) {
        if (isVoiceEnabled()) speaker.speak(bustour.getText().toString());
        Intent bustour =new Intent(mychild.this, bustour.class);
        bustour.putExtra("userId",userId);
        bustour.putExtra("collection",collection);
        startActivity(bustour);
    }

    public void FeedbackandNotes(View view) {
        if (isVoiceEnabled()) speaker.speak(FeedbackNotes.getText().toString());
        Intent FeedbackandNotes =new Intent(mychild.this, feedbackandnotesofstudents.class);
        FeedbackandNotes.putExtra("userId",userId);
        FeedbackandNotes.putExtra("collection",collection);
        startActivity(FeedbackandNotes);
    }

    public void RegistrationandPayment(View view) {
        if (isVoiceEnabled()) speaker.speak(RegistrationPayment.getText().toString());
        Intent  RegistrationandPayment=new Intent(mychild.this, registrationandpayment.class);
        RegistrationandPayment .putExtra("userId",userId);
        RegistrationandPayment.putExtra("collection",collection);
        startActivity(RegistrationandPayment);
    }

    public void dailymenuu(View view) {
        if (isVoiceEnabled()) speaker.speak(DailyMenu.getText().toString());
        Intent  dailymenuu =new Intent(mychild.this,  dailymenu.class);
        dailymenuu.putExtra("userId",userId);
        dailymenuu.putExtra("collection",collection);
        startActivity( dailymenuu);
    }

    public void teachersinformation(View view) {
        if (isVoiceEnabled()) speaker.speak(teachersinformation.getText().toString());
        Intent teachersinformation =new Intent(mychild.this, teacherinformationofstudents.class);
        teachersinformation. putExtra("userId",userId);
        teachersinformation.putExtra("collection",collection);
        startActivity(teachersinformation );
    }
}