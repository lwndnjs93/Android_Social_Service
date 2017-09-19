package com.example.leejuwon.social_service;

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

import com.example.leejuwon.social_service.Task.Member_Task;
/**
 * et23 = 금융소득 EditText 아이디
 * */
public class Member extends AppCompatActivity {
    final Context context = this;
    String email = null;
    Intent intent;
    String[] arr = new String[26];
    //int[] iar = new int[24];
    EditText[] et = new EditText[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member);
        intent = getIntent();
        email = intent.getExtras().getString("01");
        Log.i("맴버액티비티 이메일 확인" ,email);
//        try{
//            Email_Check ec = new Email_Check();
//            String result = ec.execute(email).get();
//            Log.i("email값으로 통신 결과", "mainactivity에서 member로 넘어온 email값으로 서버와 통신한 결과 : "+result);
//        }catch (Exception e){
//
//        }


        //가구주 연령 고르는 박스
        final Button button00 = (Button)findViewById(R.id.bt0);
        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"만25세 이상", "만25세 미만 : 1992년 이후 출생"};
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //제목 셋팅
                alertDialogBuilder.setTitle("가구주 연령");
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //프로그램을 종료한다
                                dialog.dismiss();
                                Button button = (Button)findViewById(R.id.bt0);
                                button.setText(items[id]);
                                switch (id){
                                    case 0 : arr[24] = Integer.toString(0); break; //만 25세 이상일때
                                    case 1 : arr[24] = Integer.toString(1); break; //만 25세 미만일때
                                }
                                Log.d("나이선택", String.valueOf(arr[24])+"나이는 잘 들어왔나?");
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });//가구주 연령 고르는 박스 끝

        //도시 고르기 박스 시작
        final Button button01 = (Button)findViewById(R.id.bt1);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items2 = {"대도시", "중소도시", "농어촌"};
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);
                //제목 셋팅
                alertDialogBuilder2.setTitle("거주지");
                alertDialogBuilder2.setItems(items2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Button button010 = (Button)findViewById(R.id.bt1);
                                button010.setText(items2[id]);
                                switch (id){
                                    case 0 : arr[25] = Integer.toString(0); break; //대도시일때
                                    case 1 : arr[25] = Integer.toString(1); break; //중소도시일때
                                    case 2 : arr[25] = Integer.toString(2); break; //농어촌일때
                                }
                                Log.d("도시선택", String.valueOf(arr[25])+"도시 값 잘 들어왔나?");
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder2.create();
                alertDialog.show();
            }
        });//도시 고르기 끝

        et[0] = (EditText)findViewById(R.id.et0);
        et[1] = (EditText)findViewById(R.id.et1);
        et[2] = (EditText)findViewById(R.id.et2);
        et[3] = (EditText)findViewById(R.id.et3);
        et[4] = (EditText)findViewById(R.id.et4);
        et[5] = (EditText)findViewById(R.id.et5);
        et[6] = (EditText)findViewById(R.id.et6);
        et[7] = (EditText)findViewById(R.id.et7);
        et[8] = (EditText)findViewById(R.id.et8);
        et[9] = (EditText)findViewById(R.id.et9);
        et[10] = (EditText)findViewById(R.id.et10);
        et[11] = (EditText)findViewById(R.id.et11);
        et[12] = (EditText)findViewById(R.id.et12);
        et[13] = (EditText)findViewById(R.id.et13);
        et[14] = (EditText)findViewById(R.id.et14);
        et[15] = (EditText)findViewById(R.id.et15);
        et[16] = (EditText)findViewById(R.id.et16);
        et[17] = (EditText)findViewById(R.id.et17);
        et[18] = (EditText)findViewById(R.id.et18);
        et[19] = (EditText)findViewById(R.id.et19);
        et[20] = (EditText)findViewById(R.id.et20);
        et[21] = (EditText)findViewById(R.id.et21);
        et[22] = (EditText)findViewById(R.id.et22);
        et[23] = (EditText)findViewById(R.id.et23); //금융소득 버튼 연결

        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0;  i < 24; i++){
                    arr[i] = et[i].getText().toString();
                }
                try{
                    String result;
                    Member_Task task = new Member_Task();
                    result = task.execute(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9],
                            arr[10], arr[11], arr[12], arr[13], arr[14], arr[15], arr[16], arr[17], arr[18],
                            arr[19], arr[20], arr[21], arr[22], arr[23], arr[24], arr[25], email).get();
                    Log.i("리턴 값", result);
                    int a00 = result.indexOf("01a");
                    int a01 = result.indexOf("02b");
                    int a02 = result.indexOf("03c");
                    int a03 = result.indexOf("04d");
                    int a04 = result.indexOf("05e");
                    int a05 = result.indexOf("06f");
                    int a06 = result.indexOf("07g");
                    int a07 = result.indexOf("08h");
                    int a08 = result.indexOf("09i");
                    int a09 = result.indexOf("10j");
                    int a10 = result.indexOf("11k");
                    int a11 = result.indexOf("12l");
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
                    int a24 = result.indexOf("25y");
                    int a25 = result.indexOf("26z");
                    int a26 = result.indexOf("27A");

                    String b00 = result.substring(a00+3, a01);
                    Log.i("근로소득", b00);
                    String b01 = result.substring(a01+3, a02);
                    Log.i("사업소득", b01);
                    String b02 = result.substring(a02+3, a03);
                    Log.i("재산소득", b02);
                    String b03 = result.substring(a03+3, a04);
                    Log.i("공적이전소득", b03);
                    String b04 = result.substring(a04+3, a05);
                    Log.i("기타소득", b04);
                    String b05 = result.substring(a05+3, a06);
                    Log.i("월평균의료비", b05);
                    String b06 = result.substring(a06+3, a07);
                    Log.i("입학금 수업료", b06);
                    String b07 = result.substring(a07+3, a08);
                    Log.i("재활보조금", b07);
                    String b08 = result.substring(a08+3, a09);
                    Log.d("연금보험료", b08);
                    String b09 = result.substring(a09+3, a10);
                    Log.d("무료임차소득", b09);
                    String b10 = result.substring(a10+3, a11);
                    Log.d("기본의식주 지원소득", b10);
                    String b11 = result.substring(a11+3, a12);
                    Log.d("주거용 주택", b11);
                    String b12 = result.substring(a12+3, a13);
                    Log.d("건축물", b12);
                    String b13 = result.substring(a13+3, a14);
                    Log.d("토지", b13);
                    String b14 = result.substring(a14+3, a15);
                    Log.d("임차보증금", b14);
                    String b15 = result.substring(a15+3, a16);
                    Log.d("기타재산", b15);
                    String b16 = result.substring(a16+3, a17);
                    Log.d("회원권", b16);
                    String b17 = result.substring(a17+3, a18);
                    Log.d("금융재산", b17);
                    String b18 = result.substring(a18+3, a19);
                    Log.d("자동차", b18);
                    String b19 = result.substring(a19+3, a20);
                    Log.d("부채", b19);
                    String b20 = result.substring(a20+3, a21);
                    Log.d("임대보증금", b20);
                    String b21 = result.substring(a21+3, a22);
                    Log.d("항공기 선박", b21);
                    String b22 = result.substring(a22+3, a23);
                    Log.d("회원권", b22);
                    String b23 = result.substring(a23+3, a24);
                    Log.d("금융소득", b23);
                    String b24 = result.substring(a24+3, a25);
                    Log.d("가구주 연령", b24);
                    String b25 = result.substring(a25+3, a26);
                    Log.d("도시", b25);
                    String b26 = result.substring(a26+3);
                    Log.d("메일주소", b26);

                    if (!result.equals(null)){
                        AlertDialog.Builder alert = new AlertDialog.Builder(Member.this);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Member.this, MainActivity.class);
                                intent.putExtra("01", email);
                                startActivity(intent);
                            }
                        });
                        alert.setMessage("회원정보가 저장되었습니다");
                        alert.show();
                    }
                }catch (Exception e){

                }
            }
        });

    }
}