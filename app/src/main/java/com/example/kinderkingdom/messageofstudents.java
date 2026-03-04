package com.example.kinderkingdom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class messageofstudents extends AppCompatActivity {
    String userId, collection;
    FirebaseFirestore db;
    TextView englishTeacherTv,OtherSubjectsTeacherTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messageofstudents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        englishTeacherTv =findViewById(R.id.englishTeacherTv);
        OtherSubjectsTeacherTv=findViewById(R.id.otherSubjectsTeacherTv);

        Toast.makeText(this,
                "userId: " + userId + "\ncollection: " + collection,
                Toast.LENGTH_LONG).show();

        loadUserData();

    }

    public void studentsmessageone(View view) {
        Intent studentsmessageone = new Intent(messageofstudents.this, studentsmessageone.class);
        studentsmessageone.putExtra("userId", userId);
        studentsmessageone.putExtra("collection", collection);
        startActivity(studentsmessageone);
        finish();
    }

    public void studentsmessagtwo(View view) {
        Intent studentsmessagetwo = new Intent(messageofstudents.this, studentssmessagetwo.class);
        studentsmessagetwo.putExtra("userId", userId);
        studentsmessagetwo.putExtra("collection", collection);
        startActivity(studentsmessagetwo);
        finish();
    }
    public void thirdmessageforthestudents(View view) {
        Intent thirdmessageforthestudents = new Intent(messageofstudents.this, thirdmessageforthestudents.class);
        thirdmessageforthestudents.putExtra("userId", userId);
        thirdmessageforthestudents.putExtra("collection", collection);
        startActivity(thirdmessageforthestudents);
        finish();
    }
    private void loadUserData() {
        db.collection(collection)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {

                        String englishTeacher = doc.getString("English Teacher") != null ? doc.getString("English Teacher") : "N/A";

                        String OtherSubjectsTeacher = doc.getString("Other subjects teacher") != null ? doc.getString("Other subjects teacher") : "N/A";

                        // عرض البيانات
                        englishTeacherTv.setText(englishTeacher);
                        OtherSubjectsTeacherTv.setText(OtherSubjectsTeacher);


                        Toast.makeText(this, "Data Loaded ", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}


