package com.example.leejuwon.social_service.member_calc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.leejuwon.social_service.MainActivity;
import com.example.leejuwon.social_service.R;
import com.example.leejuwon.social_service.Task.Email_Check;

public class Member_Calc00 extends AppCompatActivity {
    Intent intent;
    int[] b = new int [27];
    String email = null;
    //기초연금, 국민기초생활보장, 장애인연금, 장애아동수당, 한부모가정
    int basic, basic_life, disability, disability_child, singleparent;
    Calc calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member__calc);
        calc = new Calc();
        intent = getIntent();
        email = intent.getExtras().getString("01");
        Log.i("Member_Calc00의 메일 체크", email);
        try{
            Email_Check ec = new Email_Check();
            String result = ec.execute(email).get();
            Log.i("email값으로 통신 결과", "mainactivity에서 member로 넘어온 email값으로 서버와 통신한 결과 : "+result);

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

            b[0] = Integer.parseInt(result.substring(a00+3, a01));
            Log.i("근로소득", Integer.toString(b[0]));
            b[1] = Integer.parseInt(result.substring(a01+3, a02));
            Log.i("사업소득", Integer.toString(b[1]));
            b[2] = Integer.parseInt(result.substring(a02+3, a03));
            Log.i("재산소득", Integer.toString(b[2]));
            b[3] = Integer.parseInt(result.substring(a03+3, a04));
            Log.i("공적이전소득", Integer.toString(b[3]));
            b[4] = Integer.parseInt(result.substring(a04+3, a05));
            Log.i("기타소득", Integer.toString(b[4]));
            b[5] = Integer.parseInt(result.substring(a05+3, a06));
            Log.i("월평균의료비", Integer.toString(b[5]));
            b[6] = Integer.parseInt(result.substring(a06+3, a07));
            Log.i("입학금 수업료", Integer.toString(b[6]));
            b[7] = Integer.parseInt(result.substring(a07+3, a08));
            Log.i("재활보조금", Integer.toString(b[7]));
            b[8] = Integer.parseInt(result.substring(a08+3, a09));
            Log.d("연금보험료", Integer.toString(b[8]));
            b[9] = Integer.parseInt(result.substring(a09+3, a10));
            Log.d("무료임차소득", Integer.toString(b[9]));
            b[10] = Integer.parseInt(result.substring(a10+3, a11));
            Log.d("기본의식주 지원소득",Integer.toString(b[10]));
            b[11] = Integer.parseInt(result.substring(a11+3, a12));
            Log.d("주거용 주택", Integer.toString(b[11]));
            b[12] = Integer.parseInt(result.substring(a12+3, a13));
            Log.d("건축물", Integer.toString(b[12]));
            b[13] = Integer.parseInt(result.substring(a13+3, a14));
            Log.d("토지", Integer.toString(b[13]));
            b[14] = Integer.parseInt(result.substring(a14+3, a15));
            Log.d("임차보증금", Integer.toString(b[14]));
            b[15] = Integer.parseInt(result.substring(a15+3, a16));
            Log.d("기타재산", Integer.toString(b[15]));
            b[16] = Integer.parseInt(result.substring(a16+3, a17));
            Log.d("회원권", Integer.toString(b[16]));
            b[17] = Integer.parseInt(result.substring(a17+3, a18));
            Log.d("금융재산", Integer.toString(b[17]));
            b[18] = Integer.parseInt(result.substring(a18+3, a19));
            Log.d("자동차", Integer.toString(b[18]));
            b[19] = Integer.parseInt(result.substring(a19+3, a20));
            Log.d("부채", Integer.toString(b[19]));
            b[20] = Integer.parseInt(result.substring(a20+3, a21));
            Log.d("임대보증금", Integer.toString(b[20]));
            b[21] = Integer.parseInt(result.substring(a21+3, a22));
            Log.d("항공기 선박", Integer.toString(b[21]));
            b[22] = Integer.parseInt(result.substring(a22+3, a23));
            Log.d("회원권", Integer.toString(b[22]));
            b[23] = Integer.parseInt(result.substring(a23+3, a24));
            Log.d("금융소득", Integer.toString(b[23]));
            b[24] = Integer.parseInt(result.substring(a24+3, a25));
            Log.d("가구주 연령", Integer.toString(b[24]));
            b[25] = Integer.parseInt(result.substring(a25+3, a26));
            Log.d("도시", Integer.toString(b[25]));
            email = result.substring(a26+3);
            Log.d("메일주소", email);

        }catch (Exception e){

        }

        /**
         * 0 = 근로소득
         * 1 = 사업소득
         * 2 = 재산소득
         * 3 = 공적이전소득
         * 4 = 기타소득
         * 5 = 월평균의료비
         * 6 = 입학금 수헙료
         * 7 = 재활보조금
         * 8 = 연금보험료
         * 9 = 무료임차소득
         * 10 = 기본의식주 지원소득
         * 11 = 주거용 주택
         * 12 = 건축물
         * 13 = 토지
         * 14 = 임차보증금
         * 15 = 기타재산
         * 16 = 회원권
         * 17 = 금융재산
         * 18 = 자동차
         * 19 = 부채
         * 20 = 임대보증금
         * 21 = 항공기 선박
         * 22 = 회원권
         * 23 = 금융소득
         * 24 = 가구주 연령
         * 25 = 도시
         * **/


        basic = calc.basic_asset(b[12], b[13], b[14], b[15], b[21], b[22], b[18], b[17], b[0], b[1], b[2], b[3], b[9], b[19], b[20], b[25]) -
                calc.basic_income(b[0], b[1], b[2], b[3], b[9]);

        basic_life = calc.basic_life_convert(b[12], b[13], b[23], b[25], b[19], b[18], b[11], b[14], b[15]) -
                calc.basic_life_appraisal(b[0], b[1], b[2], b[4], b[5], b[7], b[8]);

        disability = calc.disablity_convert(b[12], b[13], b[14], b[15], b[16], b[17], b[18],b[19], b[25]) -
                calc.disablity_appraisal(b[1], b[2], b[3], b[9], b[10], b[0]);

        disability_child = calc.disablity_child_convert(b[11], b[12], b[13], b[14], b[15], b[17], b[19], b[18], b[25]) -
                calc.disablity_child_appraisal(b[0], b[1], b[2], b[4], b[5], b[6], b[7], b[8]);

        singleparent = calc.single_parent_convert(b[12], b[13], b[23], b[25], b[19], b[18], b[11], b[14], b[15]) -
                calc.single_parent_appraisal(b[0], b[1], b[2], b[4], b[5], b[7], b[8]);

        //기초연금 결과 화면 넘어가는 버튼
        ImageButton bt00 = (ImageButton)findViewById(R.id.mc_basic);
        bt00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_Calc00.this, Result.class);
                intent.putExtra("01", "기초연금");
                intent.putExtra("02", "노인 단독가구 1,190,000원 이하로 수급 대상자로 선정될 가능성이 있습니다");
                intent.putExtra("03", "만65세 이상의 어르신 중에서 상대적으로 형편이 어려우신 70%의 어르신게 드립니다.");
                intent.putExtra("04", "일부 어르신들은 국민연금액 또는 소득 수준, 부부 2인 수급 여부 등을 고려하여 204,010원보다 적은 연금(최소 2만원)을 받을 수 있습니다.");
                intent.putExtra("05", "주소지 읍.면 사무소 및 동 주민센터 또는 가까운 국민연금공단 지사.상담센터에서 신청하실 수 있습니다.");
                startActivity(intent);
            }
        });

        if (basic >= 119){
            bt00.setVisibility(View.GONE);
        }else if (basic <= 119){
            bt00.setVisibility(View.VISIBLE);
        }

        //국민기초생활보장 화면 넘어가는 버튼
        ImageButton bt01 = (ImageButton)findViewById(R.id.mc_basic_life);
        bt01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_Calc00.this, Result.class);
                intent.putExtra("01", "국민기초생활보장");
                if (basic_life <= 50){
                    intent.putExtra("02", "중위소득 30%이하로 생계급여 / 의료급여 / 주거급여 / 교육급여 수급 대상자로 선정될 가능성이 있습니다.");
                }else if (50 <= basic_life && basic_life < 66){
                    intent.putExtra("02", "중위소득 40%이하로 의료급여 / 주거급여 / 교육급여 수급대상자로 선정될 가능성이 있습니다.");
                }else if (66 <= basic_life && basic_life < 71){
                    intent.putExtra("02", "중위소득 43%이하로 주거급여 / 교육급여 수급대상자로 선정될 가능성이 있습니다.");
                }
                startActivity(intent);
            }
        });

        if (basic_life > 83){
            bt01.setVisibility(View.GONE);
        }else if (basic_life <= 83){
            bt01.setVisibility(View.VISIBLE);
        }

        //장애인 연금 화면으로 넘어가는 버튼
        ImageButton bt02 = (ImageButton)findViewById(R.id.mc_disablity);
        bt02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_Calc00.this, Result.class);
                intent.putExtra("01", "장애인 연금");
                intent.putExtra("02", "장애인 단독가구 1,190,000원 이하로 수급대상자로 선정될 가능성이 있습니다");
                intent.putExtra("03", "만 18세 이상의 등록 중중장애인(1급, 2급, 3급 중복) 중 소득인정액이 보건복지부장관이 매년 결정·고시하는 금액 이하인 경우에 지원합니다.");
                intent.putExtra("04", "만 18세~만 64세까지 매월 최고 206,050원(2017년 4월~2018년 3월)의 기초급여를 지급합니다.\n" +
                        "부부가 모두 기초급여를 받는 경우에는 각각 기초급여액 20%를 감액하여 1인당 164,840원을 지급합니다.(2017년 4월~2018년 3월)\n" +
                        "약간의 소득인정액 차이로 인해 발생하는 기초급여를 받는 자와 못 받는 자의 소득역진을 최소화하기 위해 기초급여액의 일부를 단계별로 감액하여 지급합니다.\n" +
                        "65세 이상은 동일한 성격의 급여인 기초연금으로 전환하고(별도 신청 필요), 기초급여는 미지급됩니다.");
                intent.putExtra("05", "읍/면/동 주민센터에 방문하여 신청합니다.");
                startActivity(intent);
            }
        });

        if (disability < 119){
            bt02.setVisibility(View.VISIBLE);
        }
        else if (disability > 119){
            bt02.setVisibility(View.GONE);
        }

        //장애아동수당 화면으로 넘어가는 버튼
        ImageButton bt03 = (ImageButton)findViewById(R.id.mc_disability_child);
        bt03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_Calc00.this, Result.class);
                intent.putExtra("01", "장애아동 수당");
                intent.putExtra("02", "중위소득 50% 이하로 수급대상자로 선정될 가능성이 있습니다");
                intent.putExtra("03", "만 18세 미만의 등록 장애인으로 생계·의료·주거·교육급여 수급자 및 차상위계층을 지원합니다.");
                intent.putExtra("04", "중증장애인은 다음과 같이 지원합니다.\n" +
                        "국민기초생활보장 생계 또는 의료급여 수급자: 매월 20만원\n" +
                        "국민기초생활보장 주거 또는 교육급여 수급자 및 차상위계층: 매월 15만원\n" +
                        "보장시설에서 생활하는 생계 또는 의료급여 수급자: 매월 7만원\n" +
                        "경증장애인은 다음과 같이 지원합니다.\n" +
                        "국민기초생활보장 생계 또는 의료급여 수급자: 매월 10만원\n" +
                        "국민기초생활보장 주거 또는 교육급여 수급자 및 차상위계층: 매월 10만원\n" +
                        "보장시설에서 생활하는 생계 또는 의료급여 수급자: 매월 2만원");
                intent.putExtra("05", "읍/면/동 주민센터에 직접 방문하여 신청합니다.");
                startActivity(intent);
            }
        });

        if (disability_child < 81){
            bt03.setVisibility(View.VISIBLE);
        }else if (disability_child >= 81){
            bt03.setVisibility(View.GONE);
        }

        //한부모가정수당 화면으로 넘어가는 버튼
        ImageButton bt04 = (ImageButton)findViewById(R.id.mc_singleparent);
        bt04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_Calc00.this, Result.class);
                intent.putExtra("01", "한부모가정수당");
                intent.putExtra("03","한부모가족 중 모 또는 부의 연령이 만 24세 이하이고 소득인정액이 기준 중위소득 60% 이하인 청소년한부모가족");
                intent.putExtra("04","• 아동양육비 : 소득인정액 기준 중위소득 60% 이하(국민기초생활보장 생계급여 수급자 제외) 청소년한부모 가구의 아동 1인당 월 150,000원 지원\n" +
                        "• 검정고시 학습비 : 소득인정액 기준 중위소득 60% 이하(국민기초생활보장 수급자 포함) 청소년한부모가구의 부 또는 모가 검정고시 학습시 연 1,540,000원 이내 지원\n" +
                        "• 고교생 학비 : 소득인정액 기준 중위소득 60% 이하(국민기초생활보장 생계급여 수급자 제외) 청소년한부모가구의 부 또는 모가 정규 고교 과정 이수시 입학금·수업료 실비 지원\n" +
                        "• 자립지원촉진수당 : 국민기초생활보장 생계급여·의료급여 수급자인 청소년한부모가구의 부 또는 모가 학업이나 취업 등 자립활동 시 월 100,000원 지원\n");
                intent.putExtra("05","• 세대주(보호자)의 주소지 읍 · 면사무소 또는 동 주민센터에 방문하여 상담 · 신청, 온라인 신청(http://online.bokjiro.go.kr)\n" +
                        "• 주소지 시 · 군 · 구에서 가구 및 소득조사를 한 후 지원대상가구에 해당이 되면 서면으로 통보\n");

                if (b[24] == 0 && singleparent <85){
                    intent.putExtra("02","중위소득 52% 이하로 한부모가족 수급대상자로 선정될 가능성이 있습니다");
                }
                if (b[24] == 1 && singleparent <98){
                    intent.putExtra("02", "중위소득 60% 이하로 한부모가족 수급대상자로 선정될 가능성이 있습니다");
                }
                startActivity(intent);
            }
        });

        switch (b[24]){
            case 0 :
                if (singleparent < 85){
                    bt04.setVisibility(View.VISIBLE);
                }else if (singleparent >= 85){
                    bt04.setVisibility(View.GONE);
                }
                break;

            case 1 :
                if (singleparent < 98){
                    bt04.setVisibility(View.VISIBLE);
                }else if (singleparent >= 98){
                    bt04.setVisibility(View.GONE);
                }
                break;
        }

        if (bt00.getVisibility() == View.GONE &&
                bt01.getVisibility() == View.GONE &&
                bt02.getVisibility() == View.GONE &&
                bt03.getVisibility() == View.GONE &&
                bt04.getVisibility() == View.GONE){
            AlertDialog.Builder alert = new AlertDialog.Builder(Member_Calc00.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Member_Calc00.this, MainActivity.class);
                    intent.putExtra("01", email);
                    startActivity(intent);
                }
            });
            alert.setMessage("해당하는 복지 서비스가 없습니다");
            alert.show();
        }
    }
}