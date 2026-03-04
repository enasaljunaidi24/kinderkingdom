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

public class attendanceandabsenceofbussupervisor extends AppCompatActivity {
    TextView nameTv,absenceCountTv;
    String userId, collection;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendanceandabsenceofbussupervisor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ربط TextViews
        nameTv = findViewById(R.id.nameTv);
        absenceCountTv = findViewById(R.id.absenceCountTv);

        // جلب البيانات من Intent
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");

        // للتأكد من القيم
        Toast.makeText(this, "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_SHORT).show();

        // تحميل الاسم وعدد الغياب
        loadUserName();
    }
    private void loadUserName() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        // الاسم مع فحص null
                        String name = doc.getString("Name") != null ? doc.getString("Name") : "N/A";

                        // الغياب مع فحص null
                        Long absence = doc.getLong("Absence");

                        // عرض الاسم
                        nameTv.setText( name);

                        // عرض الغياب كما هو من Firebase
                        absenceCountTv.setText(absence != null ? String.valueOf(absence) : "N/A");
                    } else {
                        nameTv.setText("Name: N/A");
                        absenceCountTv.setText("Absence: N/A");

                    }
                })
                .addOnFailureListener(e -> {
                    nameTv.setText("Error loading name");
                    absenceCountTv.setText("Error");
                });
    }

}