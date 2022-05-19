package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class DetectedActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    ImageButton doseButton, cautionButton, homeButton;
    ImageView imageView;
    TextToSpeech tts;
    String class_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected);

        tts = new TextToSpeech(this, this);

        doseButton = (ImageButton) findViewById(R.id.doseButton);
        cautionButton = (ImageButton) findViewById(R.id.cautionButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        imageView = (ImageView) findViewById(R.id.imageView2);

        Intent intent = getIntent();
        class_name = intent.getStringExtra("class_name");

        Toast.makeText(getApplicationContext(), class_name+"의 정보를 가져옵니다...", Toast.LENGTH_SHORT).show();
        imageView.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
            }
        }, 3000); //3초 후에 VISIBLE

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeIntent);
            }
        });


        //베아제 껄로 바꾸기!!!
        doseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak("1회 3정 복용하세요");
            }
        });

        cautionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak("복용 후에 속, 구토, 발진 증상이 있는 경우, 복용을 즉각 중지하고 의사, 약사와 상의해주세요");
            }
        });

    }

    @Override
    public void onInit(int status) {

        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.KOREA);
            tts.setSpeechRate(1);
            tts.setPitch(1);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.d("TTS","Language not supported");
            }else {
//                button.setEnabled(true);
//                speak();
            }
        }
        else {
            Log.d("TextToSpeech","Initialization failed");
        }

    }

    private void speak(String message) {
//        String message = textView.getText().toString();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    private void speak_add(String message) {
//        String message = textView.getText().toString();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    @Override
    protected void onDestroy() {

        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}