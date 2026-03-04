package com.example.kinderkingdom;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextSpeaker {
    private TextToSpeech tts;

    public TextSpeaker(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
            }
        });
    }

    public void speak(String text) {
        if (tts != null) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void shutdown() {
        if (tts != null) tts.shutdown();
    }
}

