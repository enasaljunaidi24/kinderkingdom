package com.example.kinderkingdom;

//import static com.example.kinderkingdom.R.id.kinder;
import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    String userId,collection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // تعريف الفيديو
        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo); // ضع الفيديو في res/raw/ باسم logo.mp4
        videoView.setVideoURI(videoUri);

       // التحكم بالفيديو
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

       // تشغيل الفيديو مع التحكم بالسرعة
        videoView.setOnPreparedListener(mp -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // تعيين سرعة الفيديو (0.5f = نصف السرعة)
                mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(0.7f));
            }
            videoView.start();
        });

       // الانتقال بعد انتهاء الفيديو
        videoView.setOnCompletionListener(mp -> {
            Intent intent = new Intent(MainActivity.this, page1.class); // ضع النشاط التالي
            startActivity(intent);
            finish();
        });

    }
}