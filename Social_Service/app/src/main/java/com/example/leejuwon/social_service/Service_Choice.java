package com.example.leejuwon.social_service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.leejuwon.social_service.utils.AudioWriterPCM;

import java.lang.ref.WeakReference;

public class Service_Choice extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = "ArwceCDm48ayZxRUvLyQ";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private EditText txtResult; //중간중간 찍어주는 텍스트뷰
    private Button btnStart;
    private String mResult;

    private AudioWriterPCM writer;
    Intent intent;

    ImageButton national_merit, industrial_accident, disability, Elderly, low_income, teenager, emergency, homeless, infants,
            childbirth, harm, family, teenhouse, child, single_parent, grand_parent, work, serviceperson, social_group, button;
    EditText et;

    //private TextView fn; //stop눌렀을때 결과 다 찍어주는거

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("검색중....");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            //중간중간 찍어주는부분?
            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                break;
            //여기까지?

            //에러 나면 찍어주는부분?
            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setText("음성 검색");
                btnStart.setEnabled(true);
                break;
            //여기까지?

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
                btnStart.setText("음성 검색");
                btnStart.setEnabled(true);
                break;
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service__choice);

        txtResult = (EditText)findViewById(R.id.info00); //중간중간 보여주는거
        btnStart = (Button) findViewById(R.id.voice); //음성인식 시작 버튼
        //fn = (TextView)findViewById(R.id.a02); //최종 결과 보여주는거

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    //txtResult.setText("Connecting...");
                    btnStart.setText("검색 중지");
                    //btnStart.setText(R.string.str_stop);
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    btnStart.setEnabled(false);
                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });

        ImageButton.OnClickListener buttonClickListener = new ImageButton.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent = new Intent(Service_Choice.this, Search_Service.class);
                switch (v.getId()){
                    case R.id.search : intent.putExtra("01", et.getText()); //음성인식
                        startActivity(intent); break;

                    case R.id.national_merit : intent.putExtra("02", "국가유공자"); //국가유공자
                        startActivity(intent); break;

                    case R.id.industrial_accident : intent.putExtra("03", "산업재해"); //산업재해
                        startActivity(intent); break;

                    case R.id.disability : intent.putExtra("04", "장애"); //장애
                        startActivity(intent); break;

                    case R.id.Elderly : intent.putExtra("05", "고령층"); //고령층
                        startActivity(intent); break;

                    case R.id.low_income : intent.putExtra("06", "저소득"); //저소득
                        startActivity(intent); break;

                    case R.id.teenager : intent.putExtra("07", "청소년"); //청소년
                        startActivity(intent); break;

                    case R.id.emergency : intent.putExtra("08", "긴급지원"); //긴급지원
                        startActivity(intent); break;

                    case R.id.homeless : intent.putExtra("09", "무주택"); //무주택
                        startActivity(intent); break;

                    case R.id.infants : intent.putExtra("10","영유아"); //영유아
                        startActivity(intent); break;

                    case R.id.childbirth : intent.putExtra("11",  "출산"); //출산
                        startActivity(intent); break;

                    case R.id.harm : intent.putExtra("12","피해"); //피해
                        startActivity(intent); break;

                    case R.id.family : intent.putExtra("13", "가정"); //가정
                        startActivity(intent); break;

                    case R.id.teenhouse : intent.putExtra("14","소년소녀가정"); //소년소녀가정
                        startActivity(intent); break;

                    case R.id.child : intent.putExtra("15", "아동"); //아동
                        startActivity(intent); break;

                    case R.id.single_parent : intent.putExtra("16","한부모");
                        startActivity(intent); break;

                    case R.id.grand_parent : intent.putExtra("17", "조손 가족");
                        startActivity(intent); break;

                    case R.id.work : intent.putExtra("18","근로");
                        startActivity(intent); break;

                    case R.id.serviceperson : intent.putExtra("19", "군인");
                        startActivity(intent); break;

                    case R.id.social_group : intent.putExtra("20", "취약계층");
                        startActivity(intent);
                }

            }
        };

        et = (EditText)findViewById(R.id.info00);

        national_merit = (ImageButton)findViewById(R.id.national_merit);//국가유공자
        national_merit.setOnClickListener(buttonClickListener);

        industrial_accident = (ImageButton)findViewById(R.id.industrial_accident);//산업재해
        industrial_accident.setOnClickListener(buttonClickListener);

        disability = (ImageButton)findViewById(R.id.disability);//장애
        disability.setOnClickListener(buttonClickListener);

        Elderly = (ImageButton)findViewById(R.id.Elderly);//고령층
        Elderly.setOnClickListener(buttonClickListener);

        low_income = (ImageButton)findViewById(R.id.low_income);//저소득
        low_income.setOnClickListener(buttonClickListener);

        teenager = (ImageButton)findViewById(R.id.teenager);//청소년
        teenager.setOnClickListener(buttonClickListener);

        emergency = (ImageButton)findViewById(R.id.emergency);//긴급지원
        emergency.setOnClickListener(buttonClickListener);

        homeless = (ImageButton)findViewById(R.id.homeless);//무주택
        homeless.setOnClickListener(buttonClickListener);

        infants = (ImageButton)findViewById(R.id.infants);//영유아
        infants.setOnClickListener(buttonClickListener);

        childbirth = (ImageButton)findViewById(R.id.childbirth);//출산
        childbirth.setOnClickListener(buttonClickListener);

        harm = (ImageButton)findViewById(R.id.harm);//피해
        harm.setOnClickListener(buttonClickListener);

        family = (ImageButton)findViewById(R.id.family);//가정
        family.setOnClickListener(buttonClickListener);

        teenhouse = (ImageButton)findViewById(R.id.teenhouse);//소년소녀가정
        teenhouse.setOnClickListener(buttonClickListener);

        child = (ImageButton)findViewById(R.id.child);//아동
        child.setOnClickListener(buttonClickListener);

        single_parent = (ImageButton)findViewById(R.id.single_parent);//한부모
        single_parent.setOnClickListener(buttonClickListener);

        grand_parent = (ImageButton)findViewById(R.id.grand_parent);//조손 가족
        grand_parent.setOnClickListener(buttonClickListener);

        work = (ImageButton)findViewById(R.id.work);//근로
        work.setOnClickListener(buttonClickListener);

        serviceperson = (ImageButton)findViewById(R.id.serviceperson);//군인
        serviceperson.setOnClickListener(buttonClickListener);

        social_group = (ImageButton)findViewById(R.id.social_group);//취약계층
        social_group.setOnClickListener(buttonClickListener);

        button = (ImageButton)findViewById(R.id.search); //검색버튼
        button.setOnClickListener(buttonClickListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResult = "";
        //btnStart.setText(R.string.str_start);
        btnStart.setText("음성검색");
        btnStart.setEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // NOTE : release() must be called on stop time.
        naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<Service_Choice> mActivity;

        RecognitionHandler(Service_Choice activity) {
            mActivity = new WeakReference<Service_Choice>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Service_Choice activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}