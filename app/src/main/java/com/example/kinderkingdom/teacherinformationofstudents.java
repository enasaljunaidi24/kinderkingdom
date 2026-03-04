package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class teacherinformationofstudents extends AppCompatActivity {
    // تهيئة Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextSpeaker speaker;

    String userId,collection;
    TextView englishTeacherTv, otherSubjectsTeacherTv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacherinformationofstudents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        // ربط الواجهة

// ربط بالواجهة
        englishTeacherTv = findViewById(R.id.englishTeacherTv);
        otherSubjectsTeacherTv = findViewById(R.id.otherSubjectsTeacherTv);


        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        // التأكد من القيم عبر Toast
        Toast.makeText(this, "userId: " + userId + "\ncollection: " + collection, Toast.LENGTH_LONG).show();


        String[] allCollections = {"children", "teachers", "helpers", "security", "drivers", "managers"};
        loadUserData();
    }
    private void loadUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        try {
                            String englishTeacher = doc.getString("English Teacher") != null ? doc.getString("English Teacher") : "N/A";
                            String otherSubjectsTeacher = doc.getString("Other subjects teacher") != null ? doc.getString("Other subjects teacher") : "N/A";

                            // عرض البيانات
                            englishTeacherTv.setText("English Teacher: " + englishTeacher);
                            otherSubjectsTeacherTv.setText("Other Subjects Teacher: " + otherSubjectsTeacher);

                            Toast.makeText(this, "Data Loaded!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




}