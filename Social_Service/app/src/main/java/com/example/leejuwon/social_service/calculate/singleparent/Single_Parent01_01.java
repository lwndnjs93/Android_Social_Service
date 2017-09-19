package com.example.leejuwon.social_service.calculate.singleparent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.leejuwon.social_service.R;

import static com.example.leejuwon.social_service.R.id.ect;

public class Single_Parent01_01 extends AppCompatActivity {
    Intent intent;
    int work01, //근로소득
            business01, //사업소득
            property01, //재산소득
            ect01, //기타소득
            medical01, //월평균 의료비
            rehabilitation01, //재활보조금
            pension01, //연금보험료
            house01, //주거용 주택
            building01, //건축물
            land01, //토지
            deposit_for_lease01, //임차 보증금
            ect_property01, //기타재산
            finance_income01, //금융소득
            car01, //차량 가액
            debt01, //부채
            age, //가구주 연령 : 0은 만 25세 이상일때 / 1은 만 25세 미만일때
            city; //도시 : 0은 대도시일때 / 1은 중소도시일때 / 2는 농어촌일때

    int recognition; //소득 인정액
    int appraisal; //소득 평가액
    int convert; //재산의 소득 환산액

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single__parent01_01);

        intent = getIntent();

        //소득재산 시작
        if (intent.getStringExtra("01").isEmpty()){work01 = 0;}
        else {work01 = Integer.parseInt(intent.getStringExtra("01"));}//근로소득

        if (intent.getStringExtra("02").isEmpty()){business01 = 0;}
        else {business01 = Integer.parseInt(intent.getStringExtra("02"));}//사업소득

        if (intent.getStringExtra("03").isEmpty()){property01 = 0;}
        else {property01 =  Integer.parseInt(intent.getStringExtra("03"));}//재산소득

        if (intent.getStringExtra("04").isEmpty()){ect01 = 0;}
        else {ect01 = Integer.parseInt(intent.getStringExtra("04"));}//기타소득
        //소득재산 끝

        //지출요인 시작
        if (intent.getStringExtra("05").isEmpty()){medical01 = 0;}
        else {medical01 = Integer.parseInt(intent.getStringExtra("05"));}//월평균 의료비

        if (intent.getStringExtra("06").isEmpty()){rehabilitation01 = 0;}
        else {rehabilitation01 = Integer.parseInt(intent.getStringExtra("06"));}//재활보조금

        if (intent.getStringExtra("07").isEmpty()){pension01 = 0;}
        else {pension01 = Integer.parseInt(intent.getStringExtra("07"));}//연금보험료
        //지출요인 끝

        if (intent.getStringExtra("08").isEmpty()){house01 = 0;}
        else {house01 = Integer.parseInt(intent.getStringExtra("08"));}//주거용 주택

        //일반재산 시작
        if (intent.getStringExtra("09").isEmpty()){business01 = 0;}
        else {building01 = Integer.parseInt(intent.getStringExtra("09"));}//건축물

        if (intent.getStringExtra("10").isEmpty()){land01 = 0;}
        else {land01 = Integer.parseInt(intent.getStringExtra("10"));}//토지
        //일반재산 끝

        if (intent.getStringExtra("11").isEmpty()){deposit_for_lease01 = 0;}
        else{deposit_for_lease01 = Integer.parseInt(intent.getStringExtra("11"));}//임차 보증금

        if (intent.getStringExtra("12").isEmpty()){ect_property01 = 0;}
        else {ect_property01 = Integer.parseInt(intent.getStringExtra("12"));}//기타재산

        if (intent.getStringExtra("13").isEmpty()){finance_income01 = 0;}
        else {finance_income01 = Integer.parseInt(intent.getStringExtra("13"));}//금융소득

        if (intent.getStringExtra("14").isEmpty()){car01 = 0;}
        else {car01 = Integer.parseInt(intent.getStringExtra("14"));}//차량 가액

        if (intent.getStringExtra("15").isEmpty()){debt01 = 0;}
        else {debt01 = Integer.parseInt(intent.getStringExtra("15"));}//부채

        if (intent.getStringExtra("16").isEmpty()){age = 0;}
        else {age = Integer.parseInt(intent.getStringExtra("16"));}

        if (intent.getStringExtra("17").isEmpty()){city = 0;}
        else {city = Integer.parseInt(intent.getStringExtra("17"));}

        convert = convert(building01, land01, finance_income01, city, debt01, car01, house01, deposit_for_lease01, ect_property01);
        appraisal = appraisal(work01, business01, property01, ect01, medical01, rehabilitation01, pension01);
        recognition = convert+appraisal;

        Log.d("소득인정액", recognition+"####################################################################################################");
        Log.d("소득평가액", appraisal+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        Log.d("소득환산액", convert+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        switch (age){
            case 0:
                if (recognition < 85){
                    TextView tv01 = (TextView)findViewById(R.id.tv01);
                    tv01.setText("중위소득 52% 이하로 한부모가족  수급대상자로 선정될 가능성이 있습니다.");
                }else {
                    TextView tv01 = (TextView)findViewById(R.id.tv01);
                    tv01.setText("중위소득 52% 이상으로 한부모가족  수급대상자로 선정될 가능성이 낮은 것으로 예상됩니다.");
                }
                break;

            case 1:
                if (recognition < 98){
                    TextView tv01 = (TextView)findViewById(R.id.tv01);
                    tv01.setText("중위소득 60% 이하로 한부모가족  수급대상자로 선정될 가능성이 있습니다.");
                }
                else {
                    TextView tv01 = (TextView)findViewById(R.id.tv01);
                    tv01.setText("중위소득 60% 이상으로 한부모가족  수급대상자로 선정될 가능성이 낮은 것으로 예상됩니다.");
                }
                break;
        }

    }

    //재산의 소득 환산액 구하는 메소드
    public int convert(int building01, int land01, int finance_income01, //일반 금융재산
                       int city, /*도시 : 0은 대도시일때 / 1은 중소도시일때 / 2는 농어촌일때
                                    기본재산액 => 대도시 5400, 중소도시 3400, 농어촌 2900만원원*/
                       int debt01, int car01, int house01, int deposit_for_lease01, int ect_property01
                        )
    {
        int general_property = building01 + land01 + house01 + deposit_for_lease01 + ect_property01; //일반재산 = 건축물+토지
        int finance = finance_income01; //금융재산
        int basic = 0; //기본재산
        int debt = debt01; //부채
        int car = car01; //승용차 재산가액

        double c00 = house01*0.0104;
        Log.d("주거재산", c00+"double타입 주거재산");

        double c01 = general_property*0.0417;
        Log.d("일반재산", c01+"double타입 일반재산");

        double c02 = finance*0.0626;
        Log.d("금융재산", c02+"double타입 금융재산");

        int b00 = (int)c00; //주거용 재산의 소득환산율
        Log.d("주거재산", b00+"int타입 주거재산");

        int b01 = (int)c01; //일반재산의 소득환산율
        Log.d("일반재산", b01+"int타입 일반재산");

        int b02 = (int)c02; //금융재산의 소득환율
        Log.d("금융재산", b02+"int타입 금융재산");


//        int b00 = (int)(house01*0.0104); //주거용 재산의 소득환산율
//        int b01 = (int)(general_property*0.0417); //일반재산의 소득환산율
//        int b02 = (int)(finance*0.0626); //금융재산의 소득환율
        int b03 = b00 + b01 + b02;

        switch (city){
            case 0 : basic = 5400; break;
            case 1 : basic = 3400; break;
            case 2 : basic = 2900; break;
        }

        int a00 = general_property+finance; //일반재산+금융재산
        int a01 = a00-basic; //일반·금융재산의 종류별가액 - 기본재산액
        if (a01 < 0)
            a01 = 0;

        int a02 = a01-debt; //일반·금융재산의 종류별가액 - 기본재산액 - 부채
        if (a02 < 0)
            a02 = 0;

        int a03 = a02+car;
        int a04 = a03*b03;

        Log.d("일반재산", general_property+"일반재산가액");
        Log.d("금융재산", finance+"금융재산가액");
        Log.d("일반,금융재산", a00+"일반 금융재산의 종류별 가액");
        Log.d("기본재산액", basic+"기본재산액");
        Log.d("-기본재산", a01+"일반,금융재산 가액 - 기본재산");
        Log.d("부채", debt+"현재 부채");
        Log.d("-부채", a02+"-기본재산액-부채");
        Log.d("차량가액", car+"승용차 재산가액");
        Log.d("환산율", b03+"재산의 종류별 소득 환산율");

        return a04;
    }

    //소득 평가액 구하는 메소드
    public int appraisal(int work01, int business01, int property01, int ect01, //실제소득 항목
                         int medical01, int rehabilitation01, int pension01 //가구특성별 지출비용
                        )
    {
        int real_income = work01 + business01 + property01 + ect01; //실제소득
        int expense = medical01+rehabilitation01+pension01; //가구특성별 지출비용
        int temp = work01*12; //근로소득 구하기 위해서 월급*12
        int deduction = 0; //소득 공제
        int total;
        double temp00;

        if (temp <= 500 ){
            temp00 = temp*0.7;
            deduction = (int)(temp00/12);
        }
        else if (500 < temp && temp <= 1500){
            temp = temp - 500;
            temp = (int)(temp * 0.4);
            temp00 = temp + 350;
            deduction = (int)(temp00/12);
        }
        else if (1500 < temp && temp <= 4500){
            temp = temp - 1500;
            temp = (int)(temp*0.15);
            temp00 = temp+ 750;
            deduction = (int)(temp00/12);
        }
        else if (4500 < temp && temp < 10000){
            temp = temp - 4500;
            temp = (int)(temp*0.05);
            temp00 = temp+ 1200;
            deduction = (int)(temp00/12);
        }
        else if (10000 < temp){
            temp = temp - 10000;
            temp = (int)(temp* 0.02);
            temp00 = temp+ 1475;
            deduction = (int)(temp00/12);
        }

        total = real_income - expense - deduction;

        return total;
    }
}
