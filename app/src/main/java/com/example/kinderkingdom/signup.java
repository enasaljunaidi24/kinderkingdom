package com.example.kinderkingdom;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;


public class signup extends AppCompatActivity {
    TextSpeaker speaker;
    Button Signup;
    EditText email, password, phone;
    TextView resultogphone;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView settingView = findViewById(R.id.settingView);

        // ====== DROPDOWN MENU ======
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

        // ====== TEXT TO SPEECH ======
        speaker = new TextSpeaker(this);

        Signup = findViewById(R.id.SignUp);
        email = findViewById(R.id.EnterEmailss);
        password = findViewById(R.id.EnterPasswordss);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Signup.setOnClickListener(v -> {

            if (isVoiceEnabled()) speaker.speak(Signup.getText().toString());

            String Email = email.getText().toString().trim();
            String Password = password.getText().toString().trim();

            if (Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(signup.this, "يرجى تعبئة كل الحقول", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();
                                User newUser = new User(Email);
                                mDatabase.child("Users").child(userId).setValue(newUser)
                                        .addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(signup.this, "تم التسجيل وحفظ البيانات بنجاح", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(signup.this, welcomeepage.class));
                                            } else {
                                                Toast.makeText(signup.this, "خطأ بحفظ البيانات: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(signup.this, "خطأ بالتسجيل: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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

        // مباشرة تحديث الـ Locale بدون LanguageManager
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

    // كلاس المستخدم لتخزين البيانات في Firebase
    public static class User {
        public String email;
        public String phone;

        // كونستركتور فارغ مطلوب من Firebase
        public User(String email) {}

        public User(String email, String phone){
            this.email = email;
            this.phone = phone;
        }



//        Signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String Email = email.getText().toString();
//                String Password = password.getText().toString();
//                String Phone =phone.getText().toString();
//                Spinner spphone = (Spinner) findViewById(R.id.myarrays);
//                resultogphone=findViewById(R.id.resultogphones);
//
//
//                if (!Email.isEmpty() && !Password.isEmpty()) {
//                    if (!Phone.isEmpty()) {
//                        try {
//                            // التحقق أن الرقم عبارة عن أرقام فقط
//                            int Phone2 = Integer.parseInt(Phone);
//
//                            // جلب رمز الدولة من السبنر
//                            String phonenum = String.valueOf(spphone.getSelectedItem());
//                            resultogphone.setText(phonenum);
//
//                            // إذا كل شيء تمام → انتقل للصفحة التالية
//                            Intent signuptowelcome = new Intent(signup.this, welcomeepage.class);
//                            startActivity(signuptowelcome );
//
//                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
//                            Toast.makeText(signup.this, "رقم الهاتف غير صالح", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(signup.this, "Please Enter Your Phone", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(signup.this, "Please Enter Your Email And Password", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//

    }



        public void forget(View view) {
            Intent forget = new Intent(signup.this, forgetpassword1.class);
            startActivity(forget);
        }

        public void RememberMe(View view) {
            Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show();
        }
    public void setting(View view) {}
}





