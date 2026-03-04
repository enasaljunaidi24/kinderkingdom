package com.example.kinderkingdom;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class academicinformation extends AppCompatActivity {
    String userId,collection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_academicinformation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userId = getIntent().getStringExtra("userId");
        collection = getIntent().getStringExtra("collection");
        ImageView settingView = findViewById(R.id.settingView);

        settingView.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.settingsmenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {

                int id = item.getItemId();

                if (id == R.id.menu_change_language) {
                    Toast.makeText(this, "Change Language Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                else if (id == R.id.menu_enable_voice) {
                    Toast.makeText(this, "Voice Enabled/Disabled", Toast.LENGTH_SHORT).show();
                    return true;
                }

                else if (id == R.id.menu_about) {
                    return true;
                }

                else if (id == R.id.menu_contact) {
                    return true;
                }

                return false;
            });

            popup.show();
        });

    }
    public void classschedule2(View view) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        classschedule classschedule = new classschedule();
        ft.replace(R.id.linearcon,classschedule);
        ft.commit();

    }

    public void homework2(View view) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        homework homework = new homework();

        // 2. تمرير البيانات عبر Bundle
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("collection", collection);
        homework .setArguments(args);

        // 3. عرض Fragment داخل الـ container
        getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.linearcon, homework ); // R.id.fragment_container = FrameLayout أو LinearLayout
        ft.commit();




    }


    public void attendanceandabsence2(View view) {
        // 1. إنشاء Fragment
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        attendancendabsenceofstudents attendanceandabsence = new attendancendabsenceofstudents ();


    // 2. تمرير البيانات عبر Bundle
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("collection", collection);
        attendanceandabsence .setArguments(args);

    // 3. عرض Fragment داخل الـ container
        getSupportFragmentManager()
        .beginTransaction();
                ft.replace(R.id.linearcon, attendanceandabsence ); // R.id.fragment_container = FrameLayout أو LinearLayout
                ft.commit();

    }

    public void Interaction2(View view) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        interaction Interaction  = new interaction ();

        // 2. تمرير البيانات عبر Bundle
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("collection", collection);
        Interaction  .setArguments(args);

        // 3. عرض Fragment داخل الـ container
        getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.linearcon, Interaction  ); // R.id.fragment_container = FrameLayout أو LinearLayout
        ft.commit();
    }

    public void examschedule(View view) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        examsschedule examschedule = new examsschedule ();
        ft.replace(R.id.linearcon, examschedule);
        ft.commit();
    }

    public void marks2(View view) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        marks marks = new marks ();

        // 2. تمرير البيانات عبر Bundle
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("collection", collection);
        marks .setArguments(args);

        // 3. عرض Fragment داخل الـ container
        getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.linearcon, marks ); // R.id.fragment_container = FrameLayout أو LinearLayout
        ft.commit();


    }
}