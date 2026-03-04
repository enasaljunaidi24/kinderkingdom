package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link marks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class marks extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView nameTv,classs,teacherTv;
    private TextView islamicMonth1, islamicMonth2, islamicMonth3, islamicavg;
    private TextView arabicMonth1, arabicMonth2, arabicMonth3, arabicavg;
    private TextView englishMonth1, englishMonth2, englishMonth3, englishavg;
    private TextView mathMonth1, mathMonth2, mathMonth3, mathavg;
    private TextView scienceMonth1, scienceMonth2, scienceMonth3, scienceavg;

    private String userId;
    private String collection;
    public marks() {
        // Required empty public constructor
    }


    public static marks newInstance(String userId, String collection) {
        marks fragment = new marks();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_marks, container, false);

        // ربط TextViews
        nameTv = view.findViewById(R.id.nameTv);
        classs = view.findViewById(R.id.classs);
        teacherTv = view.findViewById(R.id.teacherTv);

        islamicMonth1 = view.findViewById(R.id.islamic_month1);
        islamicMonth2 = view.findViewById(R.id.islamic_month2);
        islamicMonth3 = view.findViewById(R.id.islamic_month3);
        islamicavg = view.findViewById(R.id.islamic_avg);

        arabicMonth1 = view.findViewById(R.id.arabic_month1);
        arabicMonth2 = view.findViewById(R.id.arabic_month2);
        arabicMonth3 = view.findViewById(R.id.arabic_month3);
        arabicavg= view.findViewById(R.id.arabic_avg);

        englishMonth1 = view.findViewById(R.id.english_month1);
        englishMonth2 = view.findViewById(R.id.english_month2);
        englishMonth3 = view.findViewById(R.id.english_month3);
        englishavg = view.findViewById(R.id.english_avg);

        mathMonth1 = view.findViewById(R.id.math_month1);
        mathMonth2 = view.findViewById(R.id.math_month2);
        mathMonth3 = view.findViewById(R.id.math_month3);
        mathavg = view.findViewById(R.id.math_avg);

        scienceMonth1 = view.findViewById(R.id.science_month1);
        scienceMonth2 = view.findViewById(R.id.science_month2);
        scienceMonth3 = view.findViewById(R.id.science_month3);
        scienceavg = view.findViewById(R.id.science_avg);

        // جلب البيانات من Bundle
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            collection = getArguments().getString("collection");
        }

        Toast.makeText(getContext(), "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_SHORT).show();

        loadMarks();

        return view;
    }

    private void loadMarks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .document(userId)
                .get()
                .addOnSuccessListener(this::fillData)
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show());
    }

    private void fillData(@NonNull DocumentSnapshot doc) {
        if (doc.exists()) {
            // الاسم
            String name = doc.getString("Name");
            nameTv.setText("Student Name: " + (name != null ? name : "N/A"));
            //الصف
            String Class = doc.getString("Class");
            classs.setText("Class: " +(Class != null ? Class : "N/A"));
            //اسم مربية الصف
            String teacherName = doc.getString("Other subjects teacher");
            teacherTv.setText("Class Teacher: " + (teacherName != null ? teacherName : "N/A"));
            // دالة مساعدة لتحويل الرقم إلى نص
            java.util.function.Function<String, String> getNumber = field -> {
                Double val = doc.getDouble(field);
                return val != null ? String.valueOf(val.intValue()) : "N/A";
            };

           // Islamic

            double islamicFinal = getDoubleFromDoc(doc, "islamic ed final");

            islamicMonth1.setText(String.valueOf((int) getDoubleFromDoc(doc, "islamic ed month1")));
            islamicMonth2.setText(String.valueOf((int) getDoubleFromDoc(doc, "islamic ed month2")));
            islamicMonth3.setText(String.valueOf((int) getDoubleFromDoc(doc, "islamic ed month3")));

            calculateSum(
                    islamicMonth1,
                    islamicMonth2,
                    islamicMonth3,
                    islamicFinal,
                    islamicavg
            );




            // Arabic

            double arabicFinal = getDoubleFromDoc(doc, "arabic final");

            arabicMonth1.setText(String.valueOf((int) getDoubleFromDoc(doc, "arabic month1")));
            arabicMonth2.setText(String.valueOf((int) getDoubleFromDoc(doc, "arabic month2")));
            arabicMonth3.setText(String.valueOf((int) getDoubleFromDoc(doc, "arabic month3")));

            calculateSum(
                    arabicMonth1,
                    arabicMonth2,
                    arabicMonth3,
                    arabicFinal,
                    arabicavg
            );


            // English

            double englishFinal = getDoubleFromDoc(doc, "english final");

            englishMonth1.setText(String.valueOf((int) getDoubleFromDoc(doc, "english month1")));
            englishMonth2.setText(String.valueOf((int) getDoubleFromDoc(doc, "english month2")));
            englishMonth3.setText(String.valueOf((int) getDoubleFromDoc(doc, "english month3")));

            calculateSum(
                    englishMonth1,
                    englishMonth2,
                    englishMonth3,
                    englishFinal,
                    englishavg
            );

            // Math

            double mathFinal = getDoubleFromDoc(doc, "math final");

            mathMonth1.setText(String.valueOf((int) getDoubleFromDoc(doc, "math month1")));
            mathMonth2.setText(String.valueOf((int) getDoubleFromDoc(doc, "math month2")));
            mathMonth3.setText(String.valueOf((int) getDoubleFromDoc(doc, "math month3")));

            calculateSum(
                    mathMonth1,
                    mathMonth2,
                    mathMonth3,
                    mathFinal,
                    mathavg
            );


            // Science

            double scienceFinal = getDoubleFromDoc(doc, "science final");

            scienceMonth1.setText(String.valueOf((int) getDoubleFromDoc(doc, "science month1")));
            scienceMonth2.setText(String.valueOf((int) getDoubleFromDoc(doc, "science month2")));
            scienceMonth3.setText(String.valueOf((int) getDoubleFromDoc(doc, "science month3")));

            calculateSum(
                    scienceMonth1,
                    scienceMonth2,
                    scienceMonth3,
                    scienceFinal,
                    scienceavg
            );


        }

    }

    // دالة آمنة لجلب الرقم من Firestore
    private double getDoubleFromDoc(DocumentSnapshot doc, String field) {
        Double val = doc.getDouble(field);
        return val != null ? val : 0;
    }



    // دالة تجمع الشهور مع Final
    private void calculateSum(TextView m1, TextView m2, TextView m3, double finalExam, TextView result) {
        double month1 = getDoubleSafe(m1.getText().toString());
        double month2 = getDoubleSafe(m2.getText().toString());
        double month3 = getDoubleSafe(m3.getText().toString());

        double sum = month1 + month2 + month3 + finalExam;
        result.setText(String.valueOf((int) sum)); // بدون كسور
    }

    // دالة آمنة لتحويل نص إلى double
    private double getDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // دالة موحدة لتعيين العلامات وحساب المجموع
    private void setMarksAndSum(DocumentSnapshot doc,
                                String month1Field, String month2Field, String month3Field,
                                String finalField,
                                TextView m1, TextView m2, TextView m3, TextView avg) {

        double finalVal = getDoubleFromDoc(doc, finalField);

        m1.setText(String.valueOf((int) getDoubleFromDoc(doc, month1Field)));
        m2.setText(String.valueOf((int) getDoubleFromDoc(doc, month2Field)));
        m3.setText(String.valueOf((int) getDoubleFromDoc(doc, month3Field)));

        calculateSum(m1, m2, m3, finalVal, avg);
    }

    }