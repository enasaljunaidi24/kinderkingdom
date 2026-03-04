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

public class helpersinformation extends AppCompatActivity {
    // تهيئة Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextSpeaker speaker;

    String userId,collection;
    TextView nameTv, emailTv, nationalNumberTv, phoneTv, addressTv, cityTv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helpersinformation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        // ربط الواجهة

        // ربط العناصر من الـ XML

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        nationalNumberTv = findViewById(R.id.nationalNumberTv);
        phoneTv = findViewById(R.id.phoneTv);
        addressTv = findViewById(R.id.addressTv);
        cityTv = findViewById(R.id.cityTv);


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

                        String name = doc.getString("Name");
                        String email = doc.getString("Email");
                        String phone = doc.getString("Phone Number");
                        String address = doc.getString("Address");
                        String city = doc.getString("City");
                        String nationalNum = doc.getString("National Number");


//                         فحص null
                        name = name != null ? name : "N/A";
                        email = email != null ? email : "N/A";
                        phone = phone != null ? phone : "N/A";
                        address = address != null ? address : "N/A";
                        city = city != null ? city : "N/A";
                        nationalNum = nationalNum != null ? nationalNum : "N/A";


                        // عرض البيانات
                        nameTv.setText("Name : " + name);
                        emailTv.setText("Email : " + email);
                        phoneTv.setText("Phone : " + phone);
                        addressTv.setText("Address : " + address);
                        cityTv.setText("City : " + city);
                        nationalNumberTv.setText("National Number : " + nationalNum);


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