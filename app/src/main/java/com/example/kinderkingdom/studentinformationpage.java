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

public class studentinformationpage extends AppCompatActivity {

    FirebaseFirestore db;
    TextSpeaker speaker;

    String userId, collection;

    TextView nameTv, emailTv, phoneTv, addressTv, cityTv;
    TextView ageTv, dobTv, genderTv, kinshipTv, nationalNumberTv, nationalityTv,
            parentEmailTv, parentNameTv, parentWorkTv;
    TextView bloodTypeTv, chronicConditionsTv, specialNeedsTv, foodAllergyTv,
            medicationsTv, dosageTv, immunizationTv, doctorNameTv,
            firstAidConsentTv, restrictedFoodTv;

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
        setContentView(R.layout.activity_studentinformationpage);

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
        ageTv = findViewById(R.id.ageTv);
        dobTv = findViewById(R.id.dobTv);
        genderTv = findViewById(R.id.genderTv);
        kinshipTv = findViewById(R.id.kinshipTv);
        nationalNumberTv = findViewById(R.id.nationalNumberTv);
        nationalityTv = findViewById(R.id.nationalityTv);
        parentEmailTv = findViewById(R.id.parentEmailTv);
        parentNameTv = findViewById(R.id.parentNameTv);
        parentWorkTv = findViewById(R.id.parentWorkTv);
        bloodTypeTv = findViewById(R.id.bloodTypeTv);
        chronicConditionsTv = findViewById(R.id.chronicConditionsTv);
        specialNeedsTv = findViewById(R.id.specialNeedsTv);
        foodAllergyTv = findViewById(R.id.foodAllergyTv);
        medicationsTv = findViewById(R.id.medicationsTv);
        dosageTv = findViewById(R.id.dosageTv);
        immunizationTv = findViewById(R.id.immunizationTv);
        doctorNameTv = findViewById(R.id.doctorNameTv);
        firstAidConsentTv = findViewById(R.id.firstAidConsentTv);
        restrictedFoodTv = findViewById(R.id.restrictedFoodTv);

        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        Toast.makeText(this,
                "userId: " + userId + "\ncollection: " + collection,
                Toast.LENGTH_LONG).show();

        loadUserData();
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
                        Long ageLong = doc.getLong("Age");
                        String age = ageLong != null ? String.valueOf(ageLong) : "N/A";
                        String dob = doc.get("Date Of Birth") != null ? doc.get("Date Of Birth").toString() : "N/A";
                        String gender = doc.getString("Gender") != null ? doc.getString("Gender") : "N/A";
                        String kinship = doc.getString("Kinship") != null ? doc.getString("Kinship") : "N/A";
                        String nationalNum = doc.getString("National Number") != null ? doc.getString("National Number") : "N/A";
                        String nationality = doc.getString("Nationality") != null ? doc.getString("Nationality") : "N/A";
                        String parentEmail = doc.getString("Parent's Email") != null ? doc.getString("Parent's Email") : "N/A";
                        String parentName = doc.getString("Parent's Name") != null ? doc.getString("Parent's Name") : "N/A";
                        String parentWork = doc.getString("Parent's Work") != null ? doc.getString("Parent's Work") : "N/A";
                        String bloodType = doc.getString("Blood Type") != null ? doc.getString("Blood Type") : "N/A";
                        String chronicConditions = doc.getString("Chronic Medical Conditions") != null ? doc.getString("Chronic Medical Conditions") : "N/A";
                        String specialNeeds = doc.getString("Special Needs or Disabilities") != null ? doc.getString("Special Needs or Disabilities") : "N/A";
                        String foodAllergies = doc.getString("Food Allergies") != null ? doc.getString("Food Allergies") : "N/A";
                        String medications = doc.getString("Regular Medications") != null ? doc.getString("Regular Medications") : "N/A";
                        String dosage = doc.getString("Dosage and Administration Schedule") != null ? doc.getString("Dosage and Administration Schedule") : "N/A";
                        String immunization = doc.getString("Immunization Status ") != null ? doc.getString("Immunization Status ") : "N/A";
                        String doctorName = doc.getString("Child's Doctor's Name ") != null ? doc.getString("Child's Doctor's Name ") : "N/A";
                        String firstAidConsent = doc.getString("Parental Consent for First Aid") != null ? doc.getString("Parental Consent for First Aid") : "N/A";
                        String restrictedFood = doc.getString("Restricted Foods for Health Reasons") != null ? doc.getString("Restricted Foods for Health Reasons") : "N/A";

                        nameTv.setText("Name :  " + name);
                        emailTv.setText("Email :  " + email);
                        phoneTv.setText("Phone :  " + phone);
                        addressTv.setText("Address :  " + address);
                        cityTv.setText("City :  " + city);
                        ageTv.setText("Age :  " + age);
                        dobTv.setText("Date of Birth :   " + dob);
                        genderTv.setText("Gender :   " + gender);
                        kinshipTv.setText("Kinship :  " + kinship);
                        nationalNumberTv.setText("National Number : " + nationalNum);
                        nationalityTv.setText("Nationality :  " + nationality);
                        parentEmailTv.setText("Parent's Email :  " + parentEmail);
                        parentNameTv.setText("Parent's Name :  " + parentName);
                        parentWorkTv.setText("Parent's Work :  " + parentWork);
                        bloodTypeTv.setText("Blood Type :  " + bloodType);
                        chronicConditionsTv.setText("Chronic Medical Conditions :  " + chronicConditions);
                        specialNeedsTv.setText("Special Needs / Disabilities :  " + specialNeeds);
                        foodAllergyTv.setText("Food Allergies :  " + foodAllergies);
                        medicationsTv.setText("Regular Medications :  " + medications);
                        dosageTv.setText("Dosage & Schedule :  " + dosage);
                        immunizationTv.setText("Immunization Status :  " + immunization);
                        doctorNameTv.setText("Child's Doctor's Name :  " + doctorName);
                        firstAidConsentTv.setText("First Aid Consent :  " + firstAidConsent);
                        restrictedFoodTv.setText("Restricted Foods :  " + restrictedFood);

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
