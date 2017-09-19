package com.example.leejuwon.social_service.calculate.basic01;

/**
 * 기초연금 예상액 계산하기
 * 입력받은 값들을 Intent 전환시 Basic01_01 액티비티로 넘겨준다
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.leejuwon.social_service.R;
import com.example.leejuwon.social_service.Task.Email_Check;

public class Basic01 extends AppCompatActivity {
    final Context context = this;
    String city; //도시 고르기

    EditText et01, et02, et03, et04, et05, et06, et07, et08, et09, et10, et11, et12, et13, et14, et15;
    String e01, e02, e03, e04, e05, e06, e07, e08, e09, e10, e11, e12, e13, e14, e15;
    Intent intent, intent00;
    String email00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic01);

        et01 = (EditText) findViewById(R.id.work01); //근로소득
        et02 = (EditText) findViewById(R.id.business01); //사업소득
        et03 = (EditText) findViewById(R.id.property01); //재산소득
        et04 = (EditText) findViewById(R.id.public_result01); //공적이전소득
        et05 = (EditText) findViewById(R.id.free01); //무료임차소득
        et06 = (EditText) findViewById(R.id.building01); //건축물
        et07 = (EditText) findViewById(R.id.land01); //토지
        et08 = (EditText) findViewById(R.id.deposit_for_lease01); //임차보증금
        et09 = (EditText) findViewById(R.id.ect_property01); //기타재산
        et10 = (EditText) findViewById(R.id.plane01); //항공기 선박
        et11 = (EditText) findViewById(R.id.membership01); //회원권
        et12 = (EditText) findViewById(R.id.car01); //자동차
        et13 = (EditText) findViewById(R.id.capital_property01); //금융재산
        et14 = (EditText) findViewById(R.id.loan01); //대출금
        et15 = (EditText) findViewById(R.id.security_deposit01); //임대보증금

        if (getIntent().getExtras() != null) {
            intent00 = getIntent();
            email00 = intent00.getExtras().getString("01");
            Log.i("기초연금 계산 이메일 확인", email00);
            try {
                Email_Check ec = new Email_Check();
                String result = ec.execute(email00).get();
                Log.i("email값으로 통신 결과", "mainactivity에서 basic01로 넘어온 email값으로 서버와 통신한 결과 : " + result);

                int a00 = result.indexOf("01a");
                int a01 = result.indexOf("02b");
                int a02 = result.indexOf("03c");
                int a03 = result.indexOf("04d");
                int a04 = result.indexOf("05e");
                int a09 = result.indexOf("10j");
                int a10 = result.indexOf("11k");
                int a12 = result.indexOf("13m");
                int a13 = result.indexOf("14n");
                int a14 = result.indexOf("15o");
                int a15 = result.indexOf("16p");
                int a16 = result.indexOf("17q");
                int a17 = result.indexOf("18r");
                int a18 = result.indexOf("19s");
                int a19 = result.indexOf("20t");
                int a20 = result.indexOf("21u");
                int a21 = result.indexOf("22v");
                int a22 = result.indexOf("23w");
                int a23 = result.indexOf("24x");
                int a25 = result.indexOf("26z");
                int a26 = result.indexOf("27A");

                String b00 = result.substring(a00 + 3, a01);
                Log.i("근로소득", b00);
                et01.setText(b00);
                String b01 = result.substring(a01 + 3, a02);
                Log.i("사업소득", b01);
                et02.setText(b01);
                String b02 = result.substring(a02 + 3, a03);
                Log.i("재산소득", b02);
                et03.setText(b02);
                String b03 = result.substring(a03 + 3, a04);
                Log.i("공적이전소득", b03);
                et04.setText(b03);
                String b09 = result.substring(a09 + 3, a10);
                Log.d("무료임차소득", b09);
                et05.setText(b09);
                String b12 = result.substring(a12 + 3, a13);
                Log.d("건축물", b12);
                et06.setText(b12);
                String b13 = result.substring(a13 + 3, a14);
                Log.d("토지", b13);
                et07.setText(b13);
                String b14 = result.substring(a14 + 3, a15);
                Log.d("임차보증금", b14);
                et08.setText(b14);
                String b15 = result.substring(a15 + 3, a16);
                Log.d("기타재산", b15);
                et09.setText(b15);
                String b16 = result.substring(a16 + 3, a17);
                Log.d("회원권", b16);
                String b17 = result.substring(a17 + 3, a18);
                Log.d("금융재산", b17);
                et13.setText(b17);
                String b18 = result.substring(a18 + 3, a19);
                Log.d("자동차", b18);
                et12.setText(b18);
                String b19 = result.substring(a19 + 3, a20);
                Log.d("부채", b19);
                et14.setText(b19);
                String b20 = result.substring(a20 + 3, a21);
                Log.d("임대보증금", b20);
                et15.setText(b20);
                String b21 = result.substring(a21 + 3, a22);
                Log.d("항공기 선박", b21);
                et10.setText(b21);
                String b22 = result.substring(a22 + 3, a23);
                Log.d("회원권", b22);
                et11.setText(b22);
                city = result.substring(a25 + 3, a26);
                Log.d("도시", city);

            } catch (Exception e) {

            }
        }

        final Button bt = (Button) findViewById(R.id.abode01);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"대도시", "중소도시", "농어촌"};
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //제목 셋팅
                alertDialogBuilder.setTitle("거주지");
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Button button = (Button) findViewById(R.id.abode01);
                                button.setText(items[id]);
                                switch (id) {
                                    case 0:
                                        city = String.valueOf(0);
                                        break; //대도시일때
                                    case 1:
                                        city = String.valueOf(1);
                                        break; //중소도시일때
                                    case 2:
                                        city = String.valueOf(2);
                                        break; //농어촌일때
                                }
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        Button bt01 = (Button) findViewById(R.id.basic01_calc);
        bt01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                e01 = (et01.getText().toString());
                if (e01.matches("")) e01 = "0";
                Log.d("e01", e01);

                e02 = (et02.getText().toString());
                if (e02.matches("")) e01 = "0";
                Log.d("e02", e02);

                e03 = (et03.getText().toString());
                if (e03.matches("")) e01 = "0";
                Log.d("e03", e03);

                e04 = (et04.getText().toString());
                if (e04.matches("")) e01 = "0";
                Log.d("e04", e04);

                e05 = (et05.getText().toString());
                if (e05.matches("")) e01 = "0";
                Log.d("e05", e05);

                e06 = (et06.getText().toString());
                if (e06.matches("")) e01 = "0";
                Log.d("e06", e06);

                e07 = (et07.getText().toString());
                if (e07.matches("")) e01 = "0";
                Log.d("e07", e07);

                e08 = (et08.getText().toString());
                if (e08.matches("")) e01 = "0";
                Log.d("e08", e08);

                e09 = (et09.getText().toString());
                if (e09.matches("")) e01 = "0";
                Log.d("e09", e09);

                e10 = (et10.getText().toString());
                if (e10.matches("")) e01 = "0";
                Log.d("e10", e10);

                e11 = (et11.getText().toString());
                if (e11.matches("")) e01 = "0";
                Log.d("e11", e11);

                e12 = (et12.getText().toString());
                if (e12.matches("")) e01 = "0";
                Log.d("e12", e12);

                e13 = (et13.getText().toString());
                if (e13.matches("")) e01 = "0";
                Log.d("e01", e13);

                e14 = (et14.getText().toString());
                if (e14.matches("")) e01 = "0";
                Log.d("e14", e14);

                e15 = (et15.getText().toString());
                if (e15.matches("")) e01 = "0";
                Log.d("e15", e15);

                Button abode00 = (Button) findViewById(R.id.abode01);
                if (abode00.getText().equals("거주지")) {
                    city = "0";
                }

                intent = new Intent(Basic01.this, Basic01_01.class);
                intent.putExtra("01", e01); //근로소득
                intent.putExtra("02", e02); //사업소득
                intent.putExtra("03", e03); //재산소득
                intent.putExtra("04", e04); //공적이전소득
                intent.putExtra("05", e05); //무료임차소득
                intent.putExtra("06", e06); //건축물
                intent.putExtra("07", e07); //토지
                intent.putExtra("08", e08); //임차보증금
                intent.putExtra("09", e09); //기타재산
                intent.putExtra("10", e10); //항공기 선박
                intent.putExtra("11", e11); //회원권
                intent.putExtra("12", e12); //자동차
                intent.putExtra("13", e13); //금융재산
                intent.putExtra("14", e14); //대출금
                intent.putExtra("15", e15); //임대보증금
                intent.putExtra("16", city); //도시 고르기

                startActivity(intent);
            }
        });

    }
}