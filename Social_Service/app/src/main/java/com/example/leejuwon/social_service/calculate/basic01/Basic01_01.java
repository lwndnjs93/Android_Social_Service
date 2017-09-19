package com.example.leejuwon.social_service.calculate.basic01;
/**
 * 무료임차소득 지분율100% / 차량은 3000cc 이상의 고급차량인 경우로 가정
 *
 * 1인 대도시 기준 계산하기(임시)
 *
 * 단독가구 1,190,000원 부부가구 1,904,000원 이하
 *
 * 소득인정액 = 월 소득 평가액 + 재산의 월 소득 환산액
 *
 * -------------------------------------------------------------------------------------------------
 * 소득 평가액 = {0.7 * (근로소득 - 60만원)} + 기타소득
 * 기타소득 : 사업소득, 재산소득, 공적이전소득(국민연금 등), 무료임차소득
 * 근로소득 공제 : 1인당 월 60만원 공제 후 30% 추가 공제
 * 무료 임차소득 : 6억 원 이상의 자녀 소유 주택에 거주 시 시가 표준액의 연 0.78% 를 소득에 포함
 *
 *  -------------------------------------------------------------------------------------------------
 *
 * 재산의 소득 환산액 = [{(일반재산 - 기본재산) + (금융재산 - 2000) - 부채} * 연 소득환산율(4%) / 12월] + p
 *
 * 재산의 소득 환산 시 금융재산을 제외한 일반재산 총액에서 기본재산액을 차감하고, 금융재산은 금융재산의 총액에서 금융재산 기본재산액을 차감한다
 *
 * P : 고급자동차(3000cc 이상 또는 4000만원 이상) 및 회원권의 가액
 * 재산의 종류 : 일반재산, 금융재산
 * 기본재산액 : 대도시 1억3천5백 / 중소도시 8천5백 / 농어촌 7천2백5십 / 금융재산 가구당 2천만원
 *
 * 소득 환산율 : 연4%
 * 고급자동차(3000cc 이상 또는 4000만원 이상) 및 회원권의 경우 월100%
 * 항공기, 선박의 경우 보정계수 3.5를, 임차보증금의 경우 보정계수 0.95를 적용
 * */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.leejuwon.social_service.R;

public class Basic01_01 extends AppCompatActivity {
    Intent intent;

    int work01, //근로소득
            business01,  //사업소득
            property01, //재산소득
            public_result01, //공적이전소득
            free01, //무료임차소득
            building01, //건축물
            land01, //토지
            deposit_for_lease01, //임차보증금
            ect_property01, //기타재산
            plane01, //항공기 선박
            membership01, //회원권
            car01, //자동차
            capital_property01, //금융재산
            loan01, //대출금
            security_deposit01, //임대보증금
            city; //도시 : 0은 대도시일때 / 1은 중소도시일때 / 2는 농어촌일때


    int recognition; //소득인정액 / 소득평가액 + 재산의 소득 환산액
    int income; //소득 평가액
    int asset;  //재산의 소득 환산액

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic01_01);

        intent = getIntent();

        //------------------------ 소득 재산 정보 시작 ----------------------------------------------
        if (intent.getStringExtra("01").isEmpty()){work01 = 0;}
        else {work01 = Integer.parseInt(intent.getStringExtra("01"));}//근로소득
        if (intent.getStringExtra("02").isEmpty()){business01 = 0;}
        else {business01 = Integer.parseInt(intent.getStringExtra("02"));}//사업소득
        if (intent.getStringExtra("03").isEmpty()){property01 = 0;}
        else {property01 = Integer.parseInt(intent.getStringExtra("03"));}//재산소득
        if (intent.getStringExtra("04").isEmpty()){public_result01 = 0;}
        else {public_result01 = Integer.parseInt(intent.getStringExtra("04"));}//공적이전소득
        if (intent.getStringExtra("05").isEmpty()){free01 = 0;}
        else {free01 = Integer.parseInt(intent.getStringExtra("05"));}//무료임차소득
        //------------------------ 소득 재산 정보 끝 ------------------------------------------------

        //------------------------------------- 일반재산 시작 ---------------------------------------
        if (intent.getStringExtra("06").isEmpty()){building01 = 0;}
        else {building01 = Integer.parseInt(intent.getStringExtra("06"));}//건축물
        if (intent.getStringExtra("07").isEmpty()){land01 = 0;}
        else {land01 = Integer.parseInt(intent.getStringExtra("07"));}//토지
        if (intent.getStringExtra("08").isEmpty()){deposit_for_lease01 = 0;}
        else {deposit_for_lease01 = Integer.parseInt(intent.getStringExtra("08"));}//임차보증금
        if (intent.getStringExtra("09").isEmpty()){ect_property01 = 0;}
        else {ect_property01 = Integer.parseInt(intent.getStringExtra("09"));}//기타재산
        if (intent.getStringExtra("10").isEmpty()){plane01 = 0;}
        else {plane01 = Integer.parseInt(intent.getStringExtra("10"));}//항공기 선박
        if (intent.getStringExtra("11").isEmpty()){membership01 = 0;}
        else {membership01 = Integer.parseInt(intent.getStringExtra("11"));}//회원권
        if (intent.getStringExtra("12").isEmpty()){car01 = 0;}
        else {car01 = Integer.parseInt(intent.getStringExtra("12"));}//자동차
        //------------------------------------- 일반재산 끝 -----------------------------------------

        //------------------------------------- 금융재산 시작 ---------------------------------------
        if (intent.getStringExtra("13").isEmpty()){capital_property01 = 0;}
        else {capital_property01 = Integer.parseInt(intent.getStringExtra("13"));}//금융재산
        //------------------------------------- 금융재산 끝 -----------------------------------------

        //------------------------------------- 부채 시작 -------------------------------------------
        if (intent.getStringExtra("14").isEmpty()){loan01 = 0;}
        else {loan01 = Integer.parseInt(intent.getStringExtra("14"));}//대출금
        if (intent.getStringExtra("15").isEmpty()){security_deposit01 = 0;}
        else {security_deposit01 = Integer.parseInt(intent.getStringExtra("15"));}//임대보증금
        //--------------------------------------- 부채 끝 -------------------------------------------

        //기본재산액: 대도시(1억3천5백만 원), 중소도시(8천5백만 원), 농어촌(7천2백5십만 원), 금융재산(가구당 2천만 원)
        if (intent.getStringExtra("16").isEmpty()){city = 0;}
        else {city = Integer.parseInt(intent.getStringExtra("16"));} //도시 : 0은 대도시일때 / 1은 중소도시일때 / 2는 농어촌일때

        income = income(work01, business01, property01, public_result01, free01);
        asset = asset(building01, land01, deposit_for_lease01, ect_property01, plane01, membership01, car01,
                capital_property01,
                work01, business01, property01, public_result01, free01,
                loan01, security_deposit01,
                city);

        recognition = income+asset;

        if(recognition <= 119){
            TextView tv01 = (TextView)findViewById(R.id.tv01);
            tv01.setText("노인 단독가구 1,190,000원 이하로  \t 수급대상자로 선정될 가능성이 있습니다.");
        }
        else {
            TextView tv01 = (TextView)findViewById(R.id.tv01);
            tv01.setText("노인 단독가구 1,190,000원 이상으로  \t 지원대상이 아닌 걸로 예상됩니다.");
        }

    }
    //재산의 소득 환산액을 구하는 메소드 / 필요항목 : 일반재산, 금융재산, 부채, 연 소득 환산율, 자동차, 회원권
    public int asset(int building01, int land01, int deposit_for_lease01, int ect_property01, int plane01, int membership01, int car01, //일반 재산 항목들
                     int capital_property01, //금융재산
                     int work01, int business01, int property01, int public_result01, int free01, //소득 환산율 계산에 필요한 항목
                     int loan01, int security_deposit01, //부채
                     int city
    ){
        //일반재산
        int property_general = building01 + land01 + deposit_for_lease01 + ect_property01 + plane01 + membership01 + car01;

        //기본재산
        int property_basic = 13500; //대도시 기준 1억3천5백(임시)

        //금융재산
        int property_capital = capital_property01;

        switch (city){
            case 0: property_basic = 13500; break;
            case 1: property_basic = 8500; break;
            case 2: property_basic = 7500; break;
        }

        //연소득
        int property_year = work01*12;
        int rate = (int)(property_year*0.04); //연소득 환산율

        //부채
        int debt = loan01+security_deposit01; //부채

        int a1 = property_general - property_basic; //일반재산-기본재산
        if(a1 < 0)
            a1=0;

        int a2 = property_capital - 2000; //금융재산 - 2000만원
        if(a2 < 0)
            a2 = 0;

        int a3 = a1+a2;

        int a4 = a3-debt; //일반재산-기본재산 + 금융재산-2000 - 부채
        if (a4 < 0)
            a4=0;

        int a5  =a4*rate; //[{(일반재산 - 기본재산) + (금융재산 - 2000) - 부채} * 연 소득환산율(4%)
        int a6 = a5/12; //[{(일반재산 - 기본재산) + (금융재산 - 2000) - 부채} * 연 소득환산율(4%) / 12월]
        int a7 = a6+membership01+car01;

        return a7;
    }

    //소득 평가액 구하는 메소드 / 필요항목: : 근로소득, 기타소득
    /*
    * 소득 평가액 = {0.7 * (근로소득 - 60만원)} + 기타소득
    * 기타소득 : 사업소득, 재산소득, 공적이전소득(국민연금 등), 무료임차소득 => 5개 항목
    * 근로소득 공제 : 1인당 월 60만원 공제 후 30% 추가 공제
    * 무료 임차소득 : 6억 원 이상의 자녀 소유 주택에 거주 시 시가 표준액의 연 0.78% 를 소득에 포함
    * */
    public int income(int work01, int business01, int property01, int public_result01, int free01){
        int work_property = work01-60; //근로소득 - 60만원
        int free = free01+(int)(free01*0.78); //무료임차소득 6억원 이상의 자녀 소유 주택 거주 시 시가 표준액의 연 0.78% 를 소득에 포함
        int ect_property = business01+property01+public_result01+free; //기타소득
        int total = (int)(work_property*0.7);
        total = total+ect_property;

        return total;
    }
}
