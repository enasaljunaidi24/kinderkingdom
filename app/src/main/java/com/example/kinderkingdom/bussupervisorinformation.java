package com.example.kinderkingdom;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class bussupervisorinformation extends AppCompatActivity {
    // تهيئة Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextSpeaker speaker;

    String userId,collection;
    TextView nameTv, emailTv, phoneTv, addressTv, cityTv, nationalNumberTv ;
    TextView experienceTv, tourAreaTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bussupervisorinformation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        // ربط الواجهة

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        addressTv = findViewById(R.id.addressTv);
        cityTv = findViewById(R.id.cityTv);
        nationalNumberTv = findViewById(R.id.nationalNumberTv);
        experienceTv = findViewById(R.id.experienceTv);

        tourAreaTv = findViewById(R.id.tourAreaTv);

        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        Toast.makeText(this, "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_LONG).show();
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

                        // جلب البيانات مع فحص null
                        String name = doc.getString("Name") != null ? doc.getString("Name") : "N/A";
                        String email = doc.getString("Email") != null ? doc.getString("Email") : "N/A";
                        String phone = doc.getString("Phone Number") != null ? doc.getString("Phone Number") : "N/A";
                        String address = doc.getString("Address") != null ? doc.getString("Address") : "N/A";
                        String city = doc.getString("City") != null ? doc.getString("City") : "N/A";
                        String nationalNumber = doc.getString("National Number") != null ? doc.getString("National Number") : "N/A";
                        String experience = doc.getString("Experience") != null ? doc.getString("Experience") : "N/A";
                        String tourArea = doc.getString("Tour Area") != null ? doc.getString("Tour Area") : "N/A";

                        // فحص null
                        name = name != null ? name : "N/A";
                        email = email != null ? email : "N/A";
                        phone = phone != null ? phone : "N/A";
                        address = address != null ? address : "N/A";
                        city = city != null ? city : "N/A";
                        nationalNumber = nationalNumber != null ? nationalNumber : "N/A";
                        experience = experience != null ? experience : "N/A";
                        tourArea = tourArea != null ? tourArea : "N/A";

                        // عرض البيانات في TextViews
                        nameTv.setText("Name: " + name);
                        emailTv.setText("Email: " + email);
                        phoneTv.setText("Phone Number: " + phone);
                        addressTv.setText("Address: " + address);
                        cityTv.setText("City: " + city);
                        nationalNumberTv.setText("National Number: " + nationalNumber);
                        experienceTv.setText("Experience: " + experience);
                        tourAreaTv.setText("Tour Area: " + tourArea);

                        Toast.makeText(this, "Data Loaded!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}