package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class FindActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    ImageButton searchButton, micButton, doseButton, cautionButton, homeButton;
    EditText searchInput;
    ImageView imageView;
    Intent intent;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        tts = new TextToSpeech(this, this);

        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, PERMISSION);
        } else {

        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName()); //여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); //언어 설정

        searchButton = (ImageButton) findViewById(R.id.searchButton);
        micButton = (ImageButton) findViewById(R.id.micButton);
        doseButton = (ImageButton) findViewById(R.id.doseButton);
        cautionButton = (ImageButton) findViewById(R.id.cautionButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        searchInput = (EditText) findViewById(R.id.searchInput);
        imageView = (ImageView) findViewById(R.id.medicImage);

        imageView.setVisibility(View.INVISIBLE);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeIntent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = searchInput.getText().toString();
                //tts로 input을 검색합니다라고 알리기
                speak(input + "을 검색합니다");
                imageView.setVisibility(View.INVISIBLE);
                if (input == "베아제") {
                    imageView.setImageResource(R.drawable.begase);
                    imageView.setVisibility(View.VISIBLE);

                } else if (input == "타이레놀500" || input == "타이레놀 500") {
                    imageView.setImageResource(R.drawable.tylenol500);
                    imageView.setVisibility(View.VISIBLE);

                } else if (input == "훼스탈 골드" || input == "훼스탈골드") {
                    imageView.setImageResource(R.drawable.gold);
                    imageView.setVisibility(View.VISIBLE);

                }


//                speak_add("종합감기약으로 콧물, 코 막힘, 기침, 발열 등 감기 증상의 완화에 필요한 의약품입니다.");
            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(FindActivity.this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent); //듣기 시작
            }
        });

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

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트워크 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER 가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간 초과";
                    break;
                default:
                    message = "알 수 없는 오류";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러 발생 : " + message, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            searchInput.setText("");

            for (int i = 0; i < matches.size(); i++) {
                searchInput.setText(matches.get(i));
            }
            String input = searchInput.getText().toString();
            //tts로 input을 검색합니다라고 알리기
            speak(input + "을 검색합니다");
//            speak_add("종합감기약으로 콧물, 코 막힘, 기침, 발열 등 감기 증상의 완화에 필요한 의약품입니다.");
//            imageView.setVisibility(View.VISIBLE);
            if (input == "베아제" || input == "배아재" || input == "배아제") {
                imageView.setImageResource(R.drawable.begase);
                imageView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            //향후 이벤트 추가
        }
    };


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.KOREA);
            tts.setSpeechRate(1);
            tts.setPitch(1);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("TTS", "Language not supported");
            } else {
//                button.setEnabled(true);
//                speak();

            }
        } else {
            Log.d("TextToSpeech", "Initialization failed");
        }

    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void speak_add(String message) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null){
            tts.stop();
            tts.shutdown();
        }
    }
}