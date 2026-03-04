package com.example.kinderkingdom;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link attendancendabsenceofstudents2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class attendancendabsenceofstudents2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters


    private TableLayout tableLayout;
    private String collection = "children";

    public attendancendabsenceofstudents2() {
        // Required empty public constructor
    }

    // إنشاء Instance وتمرير اسم الكوليكشن
    public static attendancendabsenceofstudents2 newInstance(String collection) {
        attendancendabsenceofstudents2 fragment = new attendancendabsenceofstudents2();
        Bundle args = new Bundle();
        args.putString("collection", collection);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_attendancendabsenceofstudents2, container, false);

        // جلب اسم الكوليكشن من الـ Bundle
        if (getArguments() != null) {
            collection = getArguments().getString("collection");
        }
        if (collection == null) collection = "children"; // القيمة الافتراضية

        // تحميل بيانات الطلاب
        loadAllStudents(view);

        return view;
    }

    private void loadAllStudents(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // TableLayout من XML
        TableLayout tableLayout = view.findViewById(R.id.tableLayout);

        // جلب كل المستندات من الكوليكشن
        db.collection(collection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("Name") != null ? doc.getString("Name") : "N/A";
                            Long absenceLong = doc.getLong("Absence");
                            String absence = absenceLong != null ? String.valueOf(absenceLong) : "0";

                            // إنشاء صف جديد
                            TableRow row = new TableRow(getContext());

                            TextView nameTv = new TextView(getContext());
                            nameTv.setText(name);
                            nameTv.setTextSize(16f);
                            nameTv.setPadding(16, 16, 16, 16);
                            nameTv.setTextColor(getResources().getColor(R.color.blue));

                            TextView absenceTv = new TextView(getContext());
                            absenceTv.setText(absence);
                            absenceTv.setTextSize(16f);
                            absenceTv.setPadding(16, 16, 16, 16);
                            absenceTv.setTextColor(getResources().getColor(R.color.blue));
                            absenceTv.setGravity(Gravity.CENTER);

                            row.addView(nameTv);
                            row.addView(absenceTv);

                            // إضافة الصف إلى الجدول
                            tableLayout.addView(row);
                        }
                    } else {
                        Toast.makeText(getContext(), "No students found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}