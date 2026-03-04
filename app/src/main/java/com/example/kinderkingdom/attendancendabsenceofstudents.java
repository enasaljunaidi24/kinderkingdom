package com.example.kinderkingdom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;


public class attendancendabsenceofstudents extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView nameTv,absenceCountTv;
    private String userId, collection;

    public attendancendabsenceofstudents() {
        // Required empty public constructor
    }


    // تمرير البيانات من Activity أو Fragment السابقة
    public static attendancendabsenceofstudents newInstance(String userId, String collection) {
        attendancendabsenceofstudents fragment = new attendancendabsenceofstudents();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("collection", collection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_attendancendabsenceofstudents, container, false);

        // ربط TextView
        nameTv = view.findViewById(R.id.nameTv);
        absenceCountTv = view.findViewById(R.id.absenceCountTv);

        // جلب البيانات من Bundle
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            collection = getArguments().getString("collection");
        }

        // للتأكد من القيم
        Toast.makeText(getContext(), "UserId: " + userId + "\nCollection: " + collection, Toast.LENGTH_SHORT).show();

        // تحميل الاسم حسب userId
        loadUserName();

        return view;
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