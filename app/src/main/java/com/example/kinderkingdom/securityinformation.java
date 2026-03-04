package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class securityinformation extends AppCompatActivity {

    // تهيئة Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextSpeaker speaker;

    String userId, collection;
    TextView nameTv, emailTv, nationalNumberTv, phoneTv, addressTv, cityTv, experienceTv, workingHoursTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_securityinformation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        // ربط الواجهة
        speaker = new TextSpeaker(this);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        nationalNumberTv = findViewById(R.id.nationalNumberTv);
        phoneTv = findViewById(R.id.phoneTv);
        addressTv = findViewById(R.id.addressTv);
        cityTv = findViewById(R.id.cityTv);
        experienceTv = findViewById(R.id.experienceTv);
        workingHoursTv = findViewById(R.id.workingHoursTv);


        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        String[] allCollections = {"children", "teachers", "helpers", "security", "drivers", "managers"};
        loadUserData();

        // إعدادات PopupMenu
        @SuppressLint({"LocalSuppress", "MissingInflatedId"}) ImageView settingView = findViewById(R.id.settingView);
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

    // تغيير اللغة
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

    // تمكين/تعطيل الصوت
    private void toggleVoice() {
        boolean voiceEnabled = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("voice_enabled", false);
        boolean newValue = !voiceEnabled;

        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("voice_enabled", newValue)
                .apply();

        Toast.makeText(this, newValue ? "Voice Enabled" : "Voice Disabled", Toast.LENGTH_SHORT).show();
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
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);

    }
        private void loadUserData () {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(collection)
                    .document(userId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {

                            String name = doc.getString("Name");
                            String email = doc.getString("Email");
                            String phone = doc.getString("Phone Number");
                            String address = doc.getString("Address");
                            String city = doc.getString("City");
                            String experience = doc.getString("Experience");
                            String workingHours = doc.getString("Working Hours");
                            String nationalNumber = doc.getString("National Number");


                            name = name != null ? name : "N/A";
                            email = email != null ? email : "N/A";
                            nationalNumber = nationalNumber != null ? nationalNumber : "N/A";
                            phone = phone != null ? phone : "N/A";
                            address = address != null ? address : "N/A";
                            city = city != null ? city : "N/A";
                            experience = experience != null ? experience : "N/A";
                            workingHours = workingHours != null ? workingHours : "N/A";

                            // عرض المعلومات
                            nameTv.setText("Name : " + name);
                            emailTv.setText("Email : " + email);
                            nationalNumberTv.setText("National Number : " + nationalNumber);
                            phoneTv.setText("Phone Number : " + phone);
                            addressTv.setText("Address : " + address);
                            cityTv.setText("City : " + city);
                            experienceTv.setText("Experience : " + experience + " years");
                            workingHoursTv.setText("Working Hours : " + workingHours);


                            Toast.makeText(this, "Data Loaded!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }