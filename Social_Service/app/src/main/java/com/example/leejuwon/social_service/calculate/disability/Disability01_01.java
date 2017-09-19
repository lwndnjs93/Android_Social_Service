package com.example.leejuwon.social_service.calculate.disability;
/**
 * 대도시 단독가구 기준
 * - 기본재산액: 대도시(1억3천5백만 원), 중소도시(8천5백만 원), 농어촌(7천2백5십만 원), 금융재산(가구당 2천만 원)
 * - 금융재산 공제 : 가구별 2,000만 원(사람별로 적용하는 것이 아님)
 * */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.leejuwon.social_service.R;

import static com.example.leejuwon.social_service.R.id.land;
import static com.example.leejuwon.social_service.R.id.work;

public class Disability01_01 extends AppCompatActivity {
    Intent intent;
    int work01, //근로소득
            business01, //사업소득
            property01, //재산소득
            public01, //공적이전소득
            free_hiring01, //무료임차소득
            free_food01, //기본의식주 지원소득
            building01, //건축물
            land01, //토지
            deposit_for_lease01, //임차보증금
            ect_property01, //기타재산
            membership01, //회원권
            capital_property01, //금융재산
            car01, //자동차
            debt01, //부채
            standard_solution01, //주택 등 시가표준액
            security_deposit01, //임대보증금
            city; //도시 선택

    int recognition; //소득 인정액
    int appraisal; //소득 평가액
    int convert; //재산의 소득 환산액

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disability01_01);

        intent = getIntent();

        //---------------------------------- 소득 재산 시작 -----------------------------------------
        if (intent.getStringExtra("01").isEmpty()){work01 = 0;}
        else{work01 = Integer.parseInt(intent.getStringExtra("01"));} //근로소득
        if (intent.getStringExtra("02").isEmpty()){business01 = 0;}
        else {business01 = Integer.parseInt(intent.getStringExtra("02"));} //사업소득
        if (intent.getStringExtra("03").isEmpty()){property01 = 0;}
        else {property01 = Integer.parseInt(intent.getStringExtra("03"));} //재산소득
        if (intent.getStringExtra("04").isEmpty()){public01 = 0;}
        else {public01 = Integer.parseInt(intent.getStringExtra("04"));} //공적이전소득
        //---------------------------------- 소득 재산 종료 -----------------------------------------

        //-------------------------------- 사적 이전 소득 시작 --------------------------------------
        if (intent.getStringExtra("05").isEmpty()){free_hiring01=0;}
        else {free_hiring01 = Integer.parseInt(intent.getStringExtra("05"));} //무료임차소득
        if (intent.getStringExtra("06").isEmpty()){free_food01=0;}
        else {free_food01 = Integer.parseInt(intent.getStringExtra("06"));} //기본의식주 지원소득
        //-------------------------------- 사적 이전 소득 종료 --------------------------------------

        //---------------------------------- 일반 재산 시작 -----------------------------------------
        if (intent.getStringExtra("07").isEmpty()){building01 = 0;}
        else {building01 = Integer.parseInt(intent.getStringExtra("07"));} //건축물
        if (intent.getStringExtra("08").isEmpty()){land01 = 0;}
        else {land01 = Integer.parseInt(intent.getStringExtra("08"));} //토지
        if (intent.getStringExtra("09").isEmpty()){deposit_for_lease01 = 0;}
        else {deposit_for_lease01 = Integer.parseInt(intent.getStringExtra("09"));}//임차보증금
        if (intent.getStringExtra("10").isEmpty()){ect_property01 = 0;}
        else {ect_property01 = Integer.parseInt(intent.getStringExtra("10"));} //기타재산
        if (intent.getStringExtra("11").isEmpty()){membership01 = 0;}
        else {membership01 = Integer.parseInt(intent.getStringExtra("11"));} //회원권
        //---------------------------------- 일반 재산 종료 -----------------------------------------
        if (intent.getStringExtra("12").isEmpty()){capital_property01 = 0;}
        else {capital_property01 = Integer.parseInt(intent.getStringExtra("12"));} //금융재산
        if (intent.getStringExtra("13").isEmpty()){car01 = 0;}
        else {car01 = Integer.parseInt(intent.getStringExtra("13"));} //자동차
        if (intent.getStringExtra("14").isEmpty()){debt01 = 0;}
        else {debt01 = Integer.parseInt(intent.getStringExtra("14"));} //부채
        //---------------------------------- 임대보증금 시작 -----------------------------------------
        if (intent.getStringExtra("15").isEmpty()){standard_solution01 = 0;}
        else {standard_solution01 = Integer.parseInt(intent.getStringExtra("15"));} //주택 등 시가표준액
        if (intent.getStringExtra("16").isEmpty()){security_deposit01 = 0;}
        else {security_deposit01 = Integer.parseInt(intent.getStringExtra("16"));} //임대보증금
        //---------------------------------- 임대보증금 종료 -----------------------------------------

        //기본재산액: 대도시(1억3천5백만 원), 중소도시(8천5백만 원), 농어촌(7천2백5십만 원)
        //0 : 대도시 / 1 : 중소도시 / 2: 농어촌
        if (intent.getStringExtra("17").isEmpty()){city = 0;}
        else {city = Integer.parseInt(intent.getStringExtra("17"));}

        //소득 평가액
        appraisal = appraisal(business01,  property01,  public01,  free_hiring01,  free_food01, work01);
        //재산의 소득 환산액
        convert = convert(building01,  land01,  deposit_for_lease01,  ect_property01,  membership01, capital_property01, car01, debt01, city);
        recognition = appraisal + convert;

        if (recognition < 119){
            TextView tv01 = (TextView)findViewById(R.id.tv01);
            tv01.setText("장애인 단독가구 1,190,000원 이하로  \t 수급대상자로 선정될 가능성이 있습니다.");
        }
        else {
            TextView tv01 = (TextView)findViewById(R.id.tv01);
            tv01.setText("장애인 단독가구 1,190,000원 이하로  \t 수급대상자가 아닌것으로 예상됩니다.");
        }

    }

    //재산의 소득 환산액 구하는 메소드
    public int convert(
                       int building01, int land01, int deposit_for_lease01, int ect_property01, int membership01, //일반재산
                       int capital_property01,  //금융재산
                       int car01,  //자동차
                       int debt01, //부채
                       int city
                       ){
        int a00 = building01 + land01; //일반재산
        int a01 = capital_property01; //금융재산
        int a02 = car01; //자동차 가격
        int a03 = debt01; //부채
        int ct = 13500; //도시
        switch (city){
            case 0 : ct = 13500; break;
            case 1 : ct = 8500; break;
            case 2 : ct = 7250; break;
        }

        //일반재산 - 기본재산 한 가격이 0보다 작으면 0으로 처리한다
        int a04 = a00-ct;
        if(a04 < 0)
            a04 = 0;

        //금융재산 - 2000한 액수가 0보다 작으면 0으로 처리한다
        int a05 = a01-2000;
        if (a05 < 0)
            a05 = 0;

        int a06 = a04+a05; //(일반재산 - 기본재산) + (금융재산 - 2,000만 원)
        int a07 = a06+a02; //(일반재산 - 기본재산) + (금융재산 - 2,000만 원) + 자동차가액
        int a08 = a07-a03; //{(일반재산 - 기본재산) + (금융재산 - 2,000만 원) + 자동차가액 - 부채}

        int a09 = a00; //재산의 소득 환산율 구할때 어떤 재산 가격을 넣어야될까?
        int a10 = (int)(a09*0.04); //재산의 소득환산율
        int a11 = a08*a10;
        int a12 = a11/12;
        int a13 = a12+car01+membership01;


        return a13;
    }

    //소득 평가액 구하는 메소드 (기타 월소득 합계 + (상시근로소득 - 상시근로소득 공제))
    public int appraisal(int business01, int property01, int public01, int free_hiring01, int free_food01, //이거 다 합치면 기타 월소득 함계
                         int work01 //월 소득 이거로 상시근로소득, 상시근로소득 공제 구하자
                        ){
        int ect_month = business01 + property01 + public01 + free_hiring01 + free_food01; //기타 월소득 합계
        int temp = work01*12; //근로소득 구하기 위해서 월급*12
        int deduction = 0; //소득 공제
        double temp00;

        if (temp <= 500 ){
            temp00 = temp*0.7;
            deduction = (int)(temp00);
        }
        else if (500 < temp && temp <= 1500){
            temp = temp - 500;
            temp = (int)(temp * 0.4);
            temp00 = temp + 350;
            deduction = (int)(temp00);
        }
        else if (1500 < temp && temp <= 4500){
            temp = temp - 1500;
            temp = (int)(temp*0.15);
            temp00 = temp+ 750;
            deduction = (int)(temp00);
        }
        else if (4500 < temp && temp < 10000){
            temp = temp - 4500;
            temp = (int)(temp*0.05);
            temp00 = temp+ 1200;
            deduction = (int)(temp00);
        }
        else if (10000 < temp){
            temp = temp - 10000;
            temp = (int)(temp* 0.02);
            temp00 = temp+ 1475;
            deduction = (int)(temp00);
        }

        int a00 = work01-deduction; //(상시근로소득 - 상시근로소득 공제)
        int a01 = ect_month+a00;

        return a01;
    }
}