
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

public class teacherinformationforteachers extends AppCompatActivity {

    // Firebase
    FirebaseFirestore db;
    TextSpeaker speaker;

    String userId, collection;

    TextView nameTv, emailTv, phoneTv, addressTv, cityTv;
    TextView nationalNumberTv, experienceTv, majorTv;

    // ---------- اللغة ----------
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacherinformationofteachers);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speaker = new TextSpeaker(this);
        db = FirebaseFirestore.getInstance();

        // ربط TextViews
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        addressTv = findViewById(R.id.addressTv);
        cityTv = findViewById(R.id.cityTv);
        nationalNumberTv = findViewById(R.id.nationalNumberTv);
        experienceTv = findViewById(R.id.experienceTv);
        majorTv = findViewById(R.id.majorTv);

        // Intent data
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        Toast.makeText(this,
                "userId: " + userId + "\ncollection: " + collection,
                Toast.LENGTH_LONG).show();

        // ---- دروب مينيو ----
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

        loadUserData();
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
    // ---------- تحميل البيانات ----------
    private void loadUserData() {
        db.collection(collection)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {

                        String name = doc.getString("Name") != null ? doc.getString("Name") : "N/A";
                        String email = doc.getString("Email") != null ? doc.getString("Email") : "N/A";
                        String phone = doc.getString("Phone Number") != null ? doc.getString("Phone Number") : "N/A";
                        String address = doc.getString("Address") != null ? doc.getString("Address") : "N/A";
                        String city = doc.getString("City") != null ? doc.getString("City") : "N/A";
                        String nationalNumber = doc.getString("National Number") != null ? doc.getString("National Number") : "N/A";
                        String experience = doc.getString("Experience") != null ? doc.getString("Experience") : "N/A";
                        String major = doc.getString("University Major") != null ? doc.getString("University Major") : "N/A";

                        nameTv.setText("Name: " + name);
                        emailTv.setText("Email: " + email);
                        phoneTv.setText("Phone: " + phone);
                        addressTv.setText("Address: " + address);
                        cityTv.setText("City: " + city);
                        nationalNumberTv.setText("National Number: " + nationalNumber);
                        experienceTv.setText("Experience: " + experience);
                        majorTv.setText("University Major: " + major);

                        Toast.makeText(this, "Data Loaded!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
