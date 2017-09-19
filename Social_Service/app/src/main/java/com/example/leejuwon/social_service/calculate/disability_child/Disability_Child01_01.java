package com.example.leejuwon.social_service.calculate.disability_child;

/**
 * 1인 대도시 기준 계산하기 (임시)
 *
 * 장애아동수당 모의계산 방법

 소득 인정액이 중위소득 50% 이하

 소득인정액 = 소득평가액 + 재산의 소득환산액

 -----------------------------------------------------------------------------------------------------------------------------------------------------------------

 소득평가액 = 실제소득 - 가구특성별 지출비용 - 근로소득 공제

 실제소득 : 근로소득, 사업소득, 재산소득


 가구특성별 지출비용 : 월평균의료비, 입학금수업료, 재활보조금, 연금보험료

 근로소득 공제

 -----------------------------------------------------------------------------------------------------------------------------------------------------------------

 재산의 소득환산액 =
 [{(일반 금융재산의 종류별 가액) - (기본재산액) + (부채)} * 재산의 종류별 소득환산율]

 기본 재산액 대도시 5400만원 / 중소도기 3400만원 / 농어촌 2900만원
 소득 환산율 : 일반재산(월 4.17%) / 금융재산(월 6.26%) / 자동차(월 100%)


 * */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.leejuwon.social_service.R;

import static com.example.leejuwon.social_service.R.id.land;
import static com.example.leejuwon.social_service.R.id.terrain;
import static com.example.leejuwon.social_service.R.id.work;

public class Disability_Child01_01 extends AppCompatActivity {
    Intent intent;
    int work01, //근로소득
            business01, //사업소득
            property01, //재산소득
            ect01, //기타소득
            medical01, //월 평균 의료비
            admission01, //입학금 수업료
            rehabilitation, //재활보조금
            pension, //연금보험료
            house01, //주거용 주택
            building01, //건축물
            land01, //토지
            deposit_for_lease01, //임차 보증금
            property_ect01, //기타 재산
            capital_property01, //금융재산
            car_price01, //차량가액
            liabilities01, //부채
            city; //도시 : 0은 대도시일때 / 1은 중소도시일때 / 2는 농어촌일때

    int recognition; //소득 인정액
    int appraisal; //소득 평가액
    int convert; //재산의 소득 환산액


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disability__child01_01);

        intent = getIntent();
        //실제 소득 시작
        if (intent.getStringExtra("01").isEmpty()){work01 = 0;}
        else {work01 = Integer.parseInt(intent.getStringExtra("01"));}//근로소득
        if (intent.getStringExtra("02").isEmpty()){business01 = 0;}
        else {business01 = Integer.parseInt(intent.getStringExtra("02"));} //사업소득
        if (intent.getStringExtra("03").isEmpty()){property01 = 0;}
        else {property01 = Integer.parseInt(intent.getStringExtra("03"));} //재산소득
        if (intent.getStringExtra("04").isEmpty()){ect01 = 0;}
        else {ect01 = Integer.parseInt(intent.getStringExtra("04"));} //기타소득
        //실제 소득 끝

        //가구 특성별 지출비용 시작
        if (intent.getStringExtra("05").isEmpty()){medical01 = 0;}
        else {medical01 = Integer.parseInt(intent.getStringExtra("05"));}//월 평균 의료비
        if (intent.getStringExtra("06").isEmpty()){admission01 = 0;}
        else {admission01 = Integer.parseInt(intent.getStringExtra("06"));}//입학금 수업료
        if (intent.getStringExtra("07").isEmpty()){rehabilitation = 0;}
        else{rehabilitation = Integer.parseInt(intent.getStringExtra("07"));} //재활보조금
        if (intent.getStringExtra("08").isEmpty()){pension = 0;}
        else{pension = Integer.parseInt(intent.getStringExtra("08"));} //연금 보험료
        //가구 특성별 지출비용 끝

        //일반 재산 시작
        if (intent.getStringExtra("09").isEmpty()){house01 = 0;}
        else{house01 = Integer.parseInt(intent.getStringExtra("09"));} //주거용 주택
        if (intent.getStringExtra("10").isEmpty()){building01 = 0;}
        else{building01 = Integer.parseInt(intent.getStringExtra("10"));} //건축물
        if (intent.getStringExtra("11").isEmpty()){land01 = 0;}
        else{land01 = Integer.parseInt(intent.getStringExtra("11"));} //토지
        if (intent.getStringExtra("12").isEmpty()){deposit_for_lease01 = 0;}
        else{deposit_for_lease01 = Integer.parseInt(intent.getStringExtra("12"));} //임차 보증금
        if (intent.getStringExtra("13").isEmpty()){property_ect01 = 0;}
        else{property_ect01 = Integer.parseInt(intent.getStringExtra("13"));} //기타재산
        //일반 재산 끝

        if (intent.getStringExtra("14").isEmpty()){capital_property01 = 0;}
        else{capital_property01 = Integer.parseInt(intent.getStringExtra("14"));} //금융 재산
        if (intent.getStringExtra("15").isEmpty()){car_price01 = 0;}
        else{car_price01 = Integer.parseInt(intent.getStringExtra("15"));} //차량가액
        if (intent.getStringExtra("16").isEmpty()){liabilities01 = 0;}
        else{liabilities01 = Integer.parseInt(intent.getStringExtra("16"));} //부채

        //기본재산액: 대도시(5,400만 원), 중소도시(3,400만 원), 농어촌(2,900만 원)
        if (intent.getStringExtra("17").isEmpty()){city = 0;}
        else {city = Integer.parseInt(intent.getStringExtra("17"));} //도시 : 0은 대도시일때 / 1은 중소도시일때 / 2는 농어촌일때

        appraisal = appraisal(work01, business01, property01, ect01, medical01, admission01, rehabilitation, pension);
        convert = convert(house01, building01, land01, deposit_for_lease01, property_ect01, capital_property01, liabilities01, car_price01, city);
        recognition = appraisal + convert;

        if(rehabilitation < 81){
            TextView tv01 = (TextView)findViewById(R.id.tv01);
            tv01.setText("중위소득 50% 이하로  수급대상자로 선정될 가능성이 있습니다.");
        }
        else{
            TextView tv01 = (TextView)findViewById(R.id.tv01);
            tv01.setText("중위소득 50% 이상로  수급대상자로 선정될 가능성이 없습니다.");
        }
    }

    //소득 환산액을 계산하는 메소드
    public int convert(int house01, int building01, int land01, int deposit_for_lease01, int property_ect01, //일반 재산에 해당하는 항목
                       int capital_property01, //금융 재산에 해당하는 항목
                       int liabilities01, //부채
                       int car_price01, //차량 가용액
                       int city
                       ){

        int a1 = house01+building01+land01+deposit_for_lease01+property_ect01+capital_property01; //일반 금융재산의 가액
        int a2 = 5400; //일단 대도시 기준으로 5400만원으로 설정

        switch (city){
            case 0: a2 = 5400; break;
            case 1: a2 = 3400; break;
            case 2: a2 = 2900; break;
        }

        int a3 = liabilities01; //부채
        int a4 = (int)((house01+building01+land01+deposit_for_lease01+property_ect01) * 0.0417); //일반 재산의 소득환산율
        int a5 = (int)(capital_property01 * 0.0626); //금융재산의 소득환산율
        int a6 = car_price01; //자량의 소득환산율
        int a7 = a4+a5+a6; //재산의 종류별 소득환산율
        //[{(일반 금융재산의 종류별 가액) - (기본재산액) + (부채)} * 재산의 종류별 소득환산율]
        int a8 = a1-a2; //(일반 금융재산의 종류별 가액) - (기본재산액)

        //일반 금융재산의 종류별 가액이 0보다 낮으면 0으로 바꿔준다
        if(a8 < 0){
            a8 = 0;
        }

        int a9 = a8+a3; //{(일반 금융재산의 종류별 가액) - (기본재산액) + (부채)}
        int a10 = a9*a7;

        return a10;
    }

    //소득 평가액을 계산하는 메소드
    public int appraisal(int work01, int business01, int property01, int ect01, //실제 소득 계산에 필요한 값들
                         int medical01, int admission01, int rehabilitation, int pension)//가구 특성별 지출비용 계산에 필요한 값들
    {
        int real; //실제 소득
        int expense; //가구 특성별 지출비용
        int deduction = 0; //소득 공제
        int temp = work01*12;
        double temp00;

        real = work01+business01+property01+ect01; //실제 소득 총합합
        expense= medical01+admission01+rehabilitation+pension; //기구 특성별 지출비용 총합

        if (temp <= 500 ){
            temp00 = temp*0.7;
            deduction = (int)(temp00/12);
        }
        else if (500 < temp && temp <= 1500){
            temp = temp - 500;
            temp = temp + 350;
            temp00 = temp * 0.4;
            deduction = (int)(temp00/12);
        }
        else if (1500 < temp && temp <= 4500){
            temp = temp - 1500;
            temp = temp + 750;
            temp00 = temp*0.15;
            deduction = (int)(temp00/12);
        }
        else if (4500 < temp && temp < 10000){
            temp = temp - 4500;
            temp = temp + 1200;
            temp00 = temp*0.05;
            deduction = (int)(temp00/12);
        }
        else if (10000 < temp){
            temp = temp - 10000;
            temp = temp + 1475;
            temp00 = temp * 0.02;
            deduction = (int)(temp00/12);
        }

        String a = Integer.toString(deduction);

        Log.d("소득공제결과", a+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return real + expense + deduction;
    }
}
