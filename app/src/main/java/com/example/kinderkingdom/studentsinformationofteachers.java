package com.example.kinderkingdom;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class studentsinformationofteachers extends AppCompatActivity {
    private LinearLayout studentsContainer; // هنا نضيف كل الطلاب
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId,collection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_studentsinformationofteachers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        Toast.makeText(this, "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_LONG).show();

        studentsContainer = findViewById(R.id.studentsContainer);

        loadStudents();
    }

    private void loadStudents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("children") // اسم الكوليكشن
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("Name");
                        String email = doc.getString("Email");
                        String phone = doc.getString("Phone Number");
                        Long age = doc.getLong("Age");
                        String bloodType = doc.getString("Blood Type");
                        String address = doc.getString("Address");
                        String doctor = doc.getString("Child's Doctor's Name");
                        String chronic = doc.getString("Chronic Medical Conditions");
                        String city = doc.getString("City");
                        String dob = doc.getString("Date Of Birth");
                        String dosage = doc.getString("Dosage and Administration Schedule");
                        String englishTeacher = doc.getString("English Teacher");
                        String foodAllergies = doc.getString("Food Allergies");
                        String gender = doc.getString("Gender");
                        String immunization = doc.getString("Immunization Status");
                        String kinship = doc.getString("Kinship");
                        String nationalNum = doc.getString("National Number");
                        String nationality = doc.getString("Nationality");
                        String otherTeacher = doc.getString("Other subjects teacher");
                        String parentEmail = doc.getString("Parent's Email");
                        String parentName = doc.getString("Parent's Name");
                        String parentWork = doc.getString("Parent's Work");
                        String firstAid = doc.getString("Parental Consent for First Aid");
                        String password = doc.getString("Password");
                        String regularMeds = doc.getString("Regular Medications");
                        String restrictedFoods = doc.getString("Restricted Foods for Health Reasons");
                        String specialNeeds = doc.getString("Special Needs or Disabilities");
                        String absence = doc.getString("Absence");

                        // إنشاء CardView لكل طالب
                        CardView card = new CardView(this);
                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        cardParams.setMargins(8,8,8,8);
                        card.setLayoutParams(cardParams);
                        card.setRadius(12);
                        card.setCardElevation(6);

                        LinearLayout cardLayout = new LinearLayout(this);
                        cardLayout.setOrientation(LinearLayout.VERTICAL);
                        cardLayout.setPadding(12,12,12,12);

                        TextView tv = new TextView(this);
                        tv.setText(
                                "Name: " + (name != null ? name : "N/A") +
                                        "\nAge: " + (age != null ? age : "N/A") +
                                        "\nAbsence: " + (absence != null ? absence : "0") +
                                        "\nEmail: " + (email != null ? email : "N/A") +
                                        "\nPhone: " + (phone != null ? phone : "N/A") +
                                        "\nAddress: " + (address != null ? address : "N/A") +
                                        "\nBlood Type: " + (bloodType != null ? bloodType : "N/A") +
                                        "\nDoctor: " + (doctor != null ? doctor : "N/A") +
                                        "\nChronic: " + (chronic != null ? chronic : "N/A") +
                                        "\nCity: " + (city != null ? city : "N/A") +
                                        "\nDOB: " + (dob != null ? dob : "N/A") +
                                        "\nDosage: " + (dosage != null ? dosage : "N/A") +
                                        "\nEnglish Teacher: " + (englishTeacher != null ? englishTeacher : "N/A") +
                                        "\nFood Allergies: " + (foodAllergies != null ? foodAllergies : "N/A") +
                                        "\nGender: " + (gender != null ? gender : "N/A") +
                                        "\nImmunization: " + (immunization != null ? immunization : "N/A") +
                                        "\nKinship: " + (kinship != null ? kinship : "N/A") +
                                        "\nNational No.: " + (nationalNum != null ? nationalNum : "N/A") +
                                        "\nNationality: " + (nationality != null ? nationality : "N/A") +
                                        "\nOther Teacher: " + (otherTeacher != null ? otherTeacher : "N/A") +
                                        "\nParent Email: " + (parentEmail != null ? parentEmail : "N/A") +
                                        "\nParent Name: " + (parentName != null ? parentName : "N/A") +
                                        "\nParent Work: " + (parentWork != null ? parentWork : "N/A") +
                                        "\nFirst Aid: " + (firstAid != null ? firstAid : "N/A") +
                                        "\nRegular Meds: " + (regularMeds != null ? regularMeds : "N/A") +
                                        "\nRestricted Foods: " + (restrictedFoods != null ? restrictedFoods : "N/A") +
                                        "\nSpecial Needs: " + (specialNeeds != null ? specialNeeds : "N/A")
                        );
                        tv.setTextSize(14);
                        tv.setPadding(8,8,8,8);

                        cardLayout.addView(tv);
                        card.addView(cardLayout);

                        studentsContainer.addView(card);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show());
    }
}

