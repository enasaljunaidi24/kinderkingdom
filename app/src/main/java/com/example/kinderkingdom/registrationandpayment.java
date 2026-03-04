package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class registrationandpayment extends AppCompatActivity {

    TextSpeaker speaker;
    private FirebaseFirestore db;

    private TextInputEditText childName, birthDate, className, parentName, phone, email;
    private Spinner paymentMethod, paymentStatus;
    private MaterialButton registerBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrationandpayment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speaker = new TextSpeaker(this);
        db = FirebaseFirestore.getInstance();

        // ربط العناصر
        childName = findViewById(R.id.childName);
        birthDate = findViewById(R.id.birthDate);
        className = findViewById(R.id.className);
        parentName = findViewById(R.id.parentName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        paymentMethod = findViewById(R.id.paymentMethod);
        paymentStatus = findViewById(R.id.paymentStatus);
        registerBtn = findViewById(R.id.registerBtn);

        // Spinner - طريقة الدفع
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Cash", "Bank Transfer", "Online Payment"}
        );
        paymentMethod.setAdapter(methodAdapter);

        // Spinner - حالة الدفع
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Paid", "Pending"}
        );
        paymentStatus.setAdapter(statusAdapter);

        // Date Picker
        birthDate.setFocusable(false);
        birthDate.setOnClickListener(v -> showDatePicker());

        // زر التسجيل
        registerBtn.setOnClickListener(v -> saveRegistration());

        // إعدادات (Popup Menu)
        ImageView settingView = findViewById(R.id.settingView);
        if (settingView != null) {
            settingView.setOnClickListener(v -> showSettingsMenu(v));
        }
    }

    // ================= DATE PICKER =================
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) ->
                        birthDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // ================= FIREBASE =================
    private void saveRegistration() {

        if (childName.getText().toString().isEmpty()) {
            childName.setError("Required");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("childName", childName.getText().toString().trim());
        data.put("birthDate", birthDate.getText().toString().trim());
        data.put("className", className.getText().toString().trim());
        data.put("parentName", parentName.getText().toString().trim());
        data.put("phone", phone.getText().toString().trim());
        data.put("email", email.getText().toString().trim());
        data.put("paymentMethod", paymentMethod.getSelectedItem().toString());
        data.put("paymentStatus", paymentStatus.getSelectedItem().toString());
        data.put("timestamp", System.currentTimeMillis());

        db.collection("registrations")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void clearFields() {
        childName.setText("");
        birthDate.setText("");
        className.setText("");
        parentName.setText("");
        phone.setText("");
        email.setText("");
        paymentMethod.setSelection(0);
        paymentStatus.setSelection(0);
    }

    // ================= SETTINGS MENU =================
    private void showSettingsMenu(View v) {
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
                Toast.makeText(this, "Kinder Kingdom App", Toast.LENGTH_SHORT).show();
                return true;

            } else if (id == R.id.menu_contact) {
                Toast.makeText(this, "Contact: KinderKingdom@gmail.com", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        popup.show();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = newBase.getSharedPreferences("settings", MODE_PRIVATE)
                .getString("lang", "en");

        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(new Locale(lang));

        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }
}
