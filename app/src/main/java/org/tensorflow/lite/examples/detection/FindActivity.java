package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

public class FindActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    ImageButton searchButton, micButton, doseButton, cautionButton, homeButton;
    EditText searchInput;
    ImageView imageView;
    Intent intent;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    TextToSpeech tts;

//    FirebaseDatabase firebaseDatabase;
    Database_SQL database_sql;
    String eng_name = "";

    String cation = "";
    String dose = "";
    String info = "";

    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        database_sql = Database_SQL.getInstance(getApplicationContext());
        database_sql.open();

        
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
                input = searchInput.getText().toString();
                //tts로 input을 검색합니다라고 알리기
//                imageView.setVisibility(View.INVISIBLE);
//                imageView.setImageResource(R.drawable.gold);
//                imageView.setVisibility(View.VISIBLE);

                speak(input + "을 검색합니다");
//                searchInput.setText("");

                if (input.equals("베아제")) {
                    imageView.setImageResource(R.drawable.begase_image);
                    imageView.setVisibility(View.VISIBLE);

                } else if (input.equals("타이레놀500") || input.equals("타이레놀 500")) {
                    imageView.setImageResource(R.drawable.tylenol500_image);
                    imageView.setVisibility(View.VISIBLE);

                } else if (input.equals("훼스탈 골드") || input.equals("훼스탈골드")) {
                    imageView.setImageResource(R.drawable.gold_image);
                    imageView.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "아직 지원되지 않는 상품입니다.", Toast.LENGTH_SHORT).show();
                }

                eng_name = HashTable_kortoeng(input);
                readChip(eng_name);
//                Toast.makeText(getApplicationContext(), info+"...", Toast.LENGTH_SHORT).show();
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
//                speak("1회 3정 복용하세요");
//                String input = searchInput.getText().toString();
                eng_name = HashTable_kortoeng(input);

                readChip2(eng_name);
            }
        });

        cautionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = searchInput.getText().toString();
                eng_name = HashTable_kortoeng(input);
//                speak("복용 후에 속, 구토, 발진 증상이 있는 경우, 복용을 즉각 중지하고 의사, 약사와 상의해주세요");
                readChip3(eng_name);
            }
        });


    }


    public String HashTable_kortoeng(String kor_name){
        Hashtable<String, String> matchTable = new Hashtable<String, String>();
        matchTable.put("타이레놀 500","Tylenol_500");
        matchTable.put("타이레놀 콜드","Tylenol_Cold");

//        matchTable.put("진한초코칩쿠키","Dark_chocolate_chip_cookies");
//        matchTable.put("콘초코플러스","Corn_Choco_Plus");
//        matchTable.put("달콤한소라형과자","Sweet_conch_type_snack");
//        matchTable.put("감튀레드칠리맛","French_fries__Red_chili_flavor");
//        matchTable.put("구운마늘바게트","Grilled_garlic_baguette");
//        matchTable.put("아미고나쵸칩","Amigo_nacho_chip");
//        matchTable.put("계란과자","Egg_snacks");
//        matchTable.put("오란다스낵","Oranda_Snack");
//        matchTable.put("마늘맛콘스낵","Garlic_flavored_corn_snack");
//        matchTable.put("케틀칩케틀콘맛","Soft_egg_snack");
//        matchTable.put("소프트계란과자","Kettle_Chip__Kettle_Corn_Flavor");
//        matchTable.put("작은별초코스낵","Small_star_chocolate_snack");
//        matchTable.put("달고나짱구","Dalgona_Crayon_Shin_Chan");
//        matchTable.put("오구마","Sweet_potatoes");
//        matchTable.put("츄러스","Churros");
//        matchTable.put("스윙칩갈릭디핑소스맛","Swing_chip__Garlic_dipping_sauce");
//        matchTable.put("꼬북칩콘스프맛","Turtle_chip__corn_soup_flavor");
//        matchTable.put("고추칩","Chili_chip");


        String eng_name = matchTable.get(kor_name);
        return eng_name;
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
            if (input.equals("베아제")) {
                imageView.setImageResource(R.drawable.begase_image);
                imageView.setVisibility(View.VISIBLE);

            } else if (input.equals("타이레놀500") || input.equals("타이레놀 500")) {
                imageView.setImageResource(R.drawable.tylenol500_image);
                imageView.setVisibility(View.VISIBLE);

            } else if (input.equals("훼스탈 골드") || input.equals("훼스탈골드")) {
                imageView.setImageResource(R.drawable.gold_image);
                imageView.setVisibility(View.VISIBLE);

            } else {
                speak("지원하지 않는 상품입니다");
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




    public String readChip(String eng_name){ //서버 연동
//        ArrayList<String> arrayList = new ArrayList<String>();
//        database_sql.HashTable_kortoeng(searchInput)

        FirebaseDatabase.getInstance().getReference().child(eng_name).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue(Object.class);
                info = value.toString();
                speak_add(info);
//                Toast.makeText(getApplicationContext(), info+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
//        Toast.makeText(getApplicationContext(), "info는"+info, Toast.LENGTH_SHORT).show();
        return info;
    }

    public String readChip2(String eng_name){ //서버 연동
//        ArrayList<String> arrayList = new ArrayList<String>();
//        database_sql.HashTable_kortoeng(searchInput)

        FirebaseDatabase.getInstance().getReference().child(eng_name).child("dose").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue(Object.class);
                dose = value.toString();
                speak(dose);
//                Toast.makeText(getApplicationContext(), dose+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        return dose;
    }

    public String readChip3(String eng_name){ //서버 연동 //cation
//        ArrayList<String> arrayList = new ArrayList<String>();
//        database_sql.HashTable_kortoeng(searchInput)

        FirebaseDatabase.getInstance().getReference().child(eng_name).child("caution").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue(Object.class);
                String cation = value.toString();
                speak(cation);
//                Toast.makeText(getApplicationContext(), cation+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        return cation;

    }
    
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
        }
    }
}
//    @Override
//  