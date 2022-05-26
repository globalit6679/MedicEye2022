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
//        speak(class_name+"의 정보를 가져옵니다...");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imageView.setImageResource(R.drawable.begase_image);
                imageView.setVisibility(View.VISIBLE);
                speak("해당 의약품은 "+class_name+"입니다");
                speak_add("소화제로 소화불량, 식욕부진, 과식 등 소화불량으로 인한 증상 완화"+"를 위한 의약품입니다");
            }
        }, 1500); //3초 후에 VISIBLE

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
                speak("1회 1정, 1일 3회 식후에 복용하세요");
            }
        });

        cautionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak("만 7세 이하 어린이, 돼지고기 알러지가 있는 경우 복용하지 마세요");
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