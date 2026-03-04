package com.example.kinderkingdom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homework#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homework extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView studentNameTv, arabicHomeworkTv, englishHomeworkTv, mathHomeworkTv, scienceHomeworkTv, islamicHomeworkTv;
    String userId, collection;

    public homework() {
        // Required empty public constructor
    }

    public static homework newInstance(String userId, String collection) {
        homework  fragment = new homework();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("collection", collection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            collection = getArguments().getString("collection");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, container, false);

        // ربط الـ TextViews
        arabicHomeworkTv = view.findViewById(R.id.arabicHomeworkTv);
        englishHomeworkTv = view.findViewById(R.id.englishHomeworkTv);
        mathHomeworkTv = view.findViewById(R.id.mathHomeworkTv);
        scienceHomeworkTv = view.findViewById(R.id.scienceHomeworkTv);
        islamicHomeworkTv = view.findViewById(R.id.islamicHomeworkTv);

        // تحميل الواجبات من Firebase
        loadHomework();

        return view;
    }

    private void loadHomework() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        arabicHomeworkTv.setText("Arabic: " + (doc.getString("arabic homework") != null ? doc.getString("arabic homework") : "No homework."));
                        englishHomeworkTv.setText("English: " + (doc.getString("english homework") != null ? doc.getString("english homework") : "No homework."));
                        mathHomeworkTv.setText("Math: " + (doc.getString("math homework") != null ? doc.getString("math homework") : "No homework."));
                        scienceHomeworkTv.setText("Science: " + (doc.getString("science homework") != null ? doc.getString("science homework") : "No homework."));
                        islamicHomeworkTv.setText("Islamic: " + (doc.getString("islamic ed homework") != null ? doc.getString("islamic ed homework") : "No homework."));
                    }
                else {
                        arabicHomeworkTv.setText("Student data not found.");
                        englishHomeworkTv.setText("Student data not found.");
                        mathHomeworkTv.setText("Student data not found.");
                        scienceHomeworkTv.setText("Student data not found.");
                        islamicHomeworkTv.setText("Student data not found.");
                    }
                })
                .addOnFailureListener(e -> {
                    arabicHomeworkTv.setText("Error loading homework.");
                    englishHomeworkTv.setText("Error loading homework.");
                    mathHomeworkTv.setText("Error loading homework.");
                    scienceHomeworkTv.setText("Error loading homework.");
                    islamicHomeworkTv.setText("Error loading homework.");
                    e.printStackTrace();
                });
    }
}