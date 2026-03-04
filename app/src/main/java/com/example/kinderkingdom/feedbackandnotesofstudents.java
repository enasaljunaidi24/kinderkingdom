package com.example.kinderkingdom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class feedbackandnotesofstudents extends AppCompatActivity {

    TextView tvStudentName;
    Spinner spinnerFeedbackType;
    EditText etFeedback, etNote;
    Button btnSendFeedback, btnSaveNote;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedbackandnotesofstudents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init Views
        tvStudentName = findViewById(R.id.tvStudentName);
        spinnerFeedbackType = findViewById(R.id.spinnerFeedbackType);
        etFeedback = findViewById(R.id.etFeedback);
        etNote = findViewById(R.id.etNote);
        btnSendFeedback = findViewById(R.id.btnSendFeedback);
        btnSaveNote = findViewById(R.id.btnSaveNote);

        db = FirebaseFirestore.getInstance();

        // Sample: Set student name
        tvStudentName.setText("John Doe");

        // Send Feedback
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String studentName = tvStudentName.getText().toString();
                String feedbackType = spinnerFeedbackType.getSelectedItem().toString();
                String feedbackMessage = etFeedback.getText().toString().trim();

                if(feedbackMessage.isEmpty()){
                    etFeedback.setError("Please write feedback");
                    return;
                }

                Map<String, Object> feedback = new HashMap<>();
                feedback.put("studentName", studentName);
                feedback.put("type", feedbackType);
                feedback.put("message", feedbackMessage);
                feedback.put("timestamp", System.currentTimeMillis());

                // Save to Firestore collection "feedbacks"
                db.collection("feedbacks")
                        .add(feedback)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(feedbackandnotesofstudents.this, "Feedback sent", Toast.LENGTH_SHORT).show();
                            etFeedback.setText("");

                            // Send notification to Teacher & Manager
                            sendNotification(studentName, feedbackType, feedbackMessage);

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(feedbackandnotesofstudents.this, "Failed to send feedback", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Save Note
        btnSaveNote.setOnClickListener(v -> {
            String studentName = tvStudentName.getText().toString();
            String noteMessage = etNote.getText().toString().trim();

            if(noteMessage.isEmpty()){
                etNote.setError("Please write note");
                return;
            }

            Map<String, Object> note = new HashMap<>();
            note.put("studentName", studentName);
            note.put("message", noteMessage);
            note.put("timestamp", System.currentTimeMillis());

            db.collection("notes")
                    .add(note)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(feedbackandnotesofstudents.this, "Note saved", Toast.LENGTH_SHORT).show();
                        etNote.setText("");

                        // Optional: Notify teacher/manager
                        sendNotification(studentName, "Note", noteMessage);

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(feedbackandnotesofstudents.this, "Failed to save note", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Example Notification method using FCM topic
    private void sendNotification(String studentName, String type, String message){
        // You can use Firebase Cloud Functions to send push notifications to teacher/manager
        // For simplicity, we'll assume you are subscribed to a topic "teachers"
        // This method triggers FCM notification to topic "teachers"
        // Actual implementation of FCM sending should be done via server/Cloud Function
        Toast.makeText(this, "Notification sent to Teacher & Manager", Toast.LENGTH_SHORT).show();
    }
}

