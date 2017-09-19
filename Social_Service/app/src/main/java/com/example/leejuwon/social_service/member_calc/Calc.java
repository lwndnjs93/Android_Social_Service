package com.example.leejuwon.social_service.member_calc;

import android.util.Log;

/**
 * Created by leejuwon on 2017-08-02.
 * 소득분위 산출에 필요한 계산 메소드를 모아놓은 클래스
 * Member_Calc00 클래스에서 각각의 메소드를 사용하여 회원들에게 별 다른 동작 없이
 * 해당 서비스를 보여주는 기능 구현에 사용한다
 */

public class Calc {

    /***********************************************************************************************************************************/
    /** 기초연금 연산 메소드 시작 Basic **/
    //재산의 소득 환산액을 구하는 메소드 / 필요항목 : 일반재산, 금융재산, 부채, 연 소득 환산율, 자동차, 회원권
    /** 건축물, 토지, 임차보증금, 기타재산, 항공기 선박, 회원권, 자동차, 금융재산, 근로소득, 사업소득, 재산소득, 공적이전소득, 무료임차소득, 대출금, 임대보증금, 도시 **/
    public int basic_asset(int building01, int land01, int deposit_for_lease01, int ect_property01, int plane01, int membership01, int car01, //일반 재산 항목들
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
    /** 근로소득, 사업소득, 재산소득, 공적이전소득, 무료임차소득 **/
    public int basic_income(int work01, int business01, int property01, int public_result01, int free01){
        int work_property = work01-60; //근로소득 - 60만원
        int free = free01+(int)(free01*0.78); //무료임차소득 6억원 이상의 자녀 소유 주택 거주 시 시가 표준액의 연 0.78% 를 소득에 포함
        int ect_property = business01+property01+public_result01+free; //기타소득
        int total = (int)(work_property*0.7);
        total = total+ect_property;

        return total;
    }
    /** 기초연금 연산 메소드 시작 **/
    /***********************************************************************************************************************************/


    /***********************************************************************************************************************************/
    /** 국민기초생활보장 연산 메소드 시작 Basic_Life **/
    //재산의 소득 환산액 구하는 메소드
    /** 건축물, 토지, 금융소득, 도시, 부채, 차량가액, 주거용 주택, 임차보증금, 기타재산 **/
    public int basic_life_convert(int building01, int land01, int finance_income01, //일반 금융재산
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
    /** 근로소득, 사업소득, 재산소득, 기타소득, 월 평균 의료비, 재활보조금, 연금보험료, **/
    public int basic_life_appraisal(int work01, int business01, int property01, int ect01, //실제소득 항목
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

    /** 국민기초생활보장 연산 메소드 끝 **/
    /***********************************************************************************************************************************/

    /***********************************************************************************************************************************/
    /** 장애인 연금 연산 메소드 시작 Disability **/

    //재산의 소득 환산액 구하는 메소드
    /** 건축물, 토지, 임차보증금, 기타재산, 회원권, 금융재산, 자동차, 부채, 도시**/
    public int disablity_convert(
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
    /** 사업소득, 재산소득, 공적이전소득, 무료임차소득, 기본의식주 지원소득, 근로소득 **/
    public int disablity_appraisal(int business01, int property01, int public01, int free_hiring01, int free_food01, //이거 다 합치면 기타 월소득 함계
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

    /** 장애인연금 메소드 끝 **/
    /***********************************************************************************************************************************/

    /***********************************************************************************************************************************/
    /** 장애아동수당 메소드 시작 Disability_Child **/

    //소득 환산액을 계산하는 메소드
    /** 주거용 주택, 건축물, 토지, 임차보증금, 기타재산, 금융재산, 부채, 차량가액, 도시 **/
    public int disablity_child_convert(int house01, int building01, int land01, int deposit_for_lease01, int property_ect01, //일반 재산에 해당하는 항목
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
    /** 근로소득, 사업소득, 재산소득, 기타소득, 월 평균 의료비, 입학금 수업료, 재활보조금, 연금보험료 **/
    public int disablity_child_appraisal(int work01, int business01, int property01, int ect01, //실제 소득 계산에 필요한 값들
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

    /** 장애아동수당 메소드 끝 **/
    /***********************************************************************************************************************************/

    /***********************************************************************************************************************************/
    /** 한부모가정 지원 연산 메소드 시작 Single_Parent **/

    //재산의 소득 환산액 구하는 메소드
    /** 건축물, 토지, 금융소득, 도시, 부채, 차량가액, 주거용 주택, 임차 보증금, 기타재산 **/
    public int single_parent_convert(int building01, int land01, int finance_income01, //일반 금융재산
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
    /** 근로소득, 사업소득, 재산소득, 기타소득, 월 평균 의료비, 재활보조금, 연금보험료 **/
    public int single_parent_appraisal(int work01, int business01, int property01, int ect01, //실제소득 항목
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

    /** 한부모가정 지원 연산 메소드 끝 **/
    /***********************************************************************************************************************************/










}
