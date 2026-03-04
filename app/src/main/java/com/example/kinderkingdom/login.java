package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class login extends AppCompatActivity {

    FirebaseFirestore db;
    TextSpeaker speaker;

    Button loginBtn;
    EditText email, password;
    TextView signupText;
    ImageView settingView;

    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        speaker = new TextSpeaker(this);

        // Views
        loginBtn = findViewById(R.id.LogIn);
        email = findViewById(R.id.EnterEmailll);
        password = findViewById(R.id.EnterPasswordll);
        signupText = findViewById(R.id.textView6);
        settingView = findViewById(R.id.settingView);

        // ====== DROPDOWN MENU ======
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


        // ===== LOGIN BUTTON =====
        loginBtn.setOnClickListener(v -> {

            if (isVoiceEnabled())
                speaker.speak(loginBtn.getText().toString());

            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] collections = {
                    "children", "teachers", "helpers",
                    "security", "drivers", "managers" , "bussupervisor"
            };

            checkCollections(0, collections, emailInput, passwordInput);
        });


    }
    public void SignUptext(View view) {
        Intent SignUptexttosignup = new Intent(login.this, signup.class);
        startActivity(SignUptexttosignup);
    }


    // ================= CHECK LOGIN =================
    private void checkCollections(int index, String[] collections, String emailInput, String passwordInput) {
        if (index >= collections.length) {
            Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show();
            return;}
        String currentCollection = collections[index];
        db.collection(currentCollection)
                .whereEqualTo("Email", emailInput)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            String dbPassword = doc.getString("Password");
                            if (dbPassword != null && dbPassword.equals(passwordInput)) {
                                userId = doc.getId();
                                Intent i = new Intent(this, welcomeepage.class);
                                i.putExtra("userId", userId);
                                i.putExtra("collection", currentCollection);
                                startActivity(i);
                                finish();
                                return;}}
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {checkCollections(index + 1, collections, emailInput, passwordInput);}
                }).addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }

    // ================= VOICE =================
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

    private boolean isVoiceEnabled() {
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
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        recreate();
    }

    // ================= APPLY LANGUAGE =================
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));

        super.attachBaseContext(
                newBase.createConfigurationContext(config)
        );
    }
}
