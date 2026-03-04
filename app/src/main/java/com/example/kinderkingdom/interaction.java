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
 * Use the {@link interaction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class interaction extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView interaction;
    String userId, collection;

    public interaction() {
        // Required empty public constructor
    }



    public static interaction newInstance(String userId, String collection) {
        interaction fragment = new interaction();
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
        View view = inflater.inflate(R.layout.fragment_interaction, container, false);

        // ربط الـ TextView
        interaction = view.findViewById(R.id.interaction);

        // تحميل التفاعل من Firebase
        loadInteraction();

        return view;
    }

    private void loadInteraction() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String interactionText = doc.getString("interaction");
                        if (interactionText != null && !interactionText.isEmpty()) {
                            interaction.setText(interactionText);
                        } else {
                            interaction.setText("No interaction recorded.");
                        }
                    } else {
                        interaction.setText("Student data not found.");
                    }
                })
                .addOnFailureListener(e -> {
                    interaction.setText("Error loading interaction.");
                    e.printStackTrace();
                });
    }
}