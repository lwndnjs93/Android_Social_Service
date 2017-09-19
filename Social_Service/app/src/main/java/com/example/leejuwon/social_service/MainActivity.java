package com.example.leejuwon.social_service;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.leejuwon.social_service.calculate.Calculate;
import com.example.leejuwon.social_service.member_calc.Member_Calc00;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.leejuwon.social_service.R.id.membercalc;


public class MainActivity extends AppCompatActivity {

    ImageButton search_service, search_course, calculate;
    Intent intent_search_service, intent_serarch_course, intent_calculate, imemberinfo, imembercalc;
    Intent intent;
    String email = null;
    Button memberInfo1, membercalcbt;

    Adapter adapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        memberInfo1 = (Button) findViewById(R.id.memberInfo);
        membercalcbt = (Button) findViewById(membercalc);

        if (intent.getExtras() != null) {
            email = intent.getExtras().getString("01");//카카오 로그인의 경우 email을 받아와 저장한다
        }
        //카카오톡 로그인을 안해서 email주소가 null값이라면 버튼을 사라지게 한다
        if (email == null) {
            memberInfo1.setVisibility(View.GONE);
            membercalcbt.setVisibility(View.GONE);
        }
        //카카오톡 로그인을 해서 email주소가 null값이 아니라면 버튼을 나타나게 한다
        if (email != null) {
            memberInfo1.setVisibility(View.VISIBLE);
            membercalcbt.setVisibility(View.VISIBLE);
        }

        Log.d("email전달확인", email + "\t" + "카카오 로그인을 했을때 본인의 메일 주소가 넘어왔는지 확인한다");

        //회원정보 입력 액티비티로 넘어가는 버튼
        memberInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imemberinfo = new Intent(MainActivity.this, Member.class);
                imemberinfo.putExtra("01", email);
                Log.d("이메일체크", email);
                startActivity(imemberinfo);
            }
        });

        //회원 모의계산 페이지로 넘어가는 버튼
        membercalcbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imembercalc = new Intent(MainActivity.this, Member_Calc00.class);
                imembercalc.putExtra("01", email);
                startActivity(imembercalc);
            }
        });

        //내게 맞는 서비스 검색하기 액티비티로 넘어가는 버튼
        search_service = (ImageButton) findViewById(R.id.search_service);
        search_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_search_service = new Intent(MainActivity.this, Service_Choice.class);
                startActivity(intent_search_service);
            }
        });

        //주변 탐색하기 기능으로 넘어가는 버튼
        search_course = (ImageButton) findViewById(R.id.search_course);
        search_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_serarch_course = new Intent(MainActivity.this, Search_Course.class);
                startActivity(intent_serarch_course);
            }
        });

        //예상 수령액 계산하기 페이지로 넘어가는 버튼
        calculate = (ImageButton) findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_calculate = new Intent(MainActivity.this, Calculate.class);
                intent_calculate.putExtra("01", email);
                //Log.d("이메일체크", email);
                startActivity(intent_calculate);

            }
        });

        ImageButton qabt = (ImageButton) findViewById(R.id.qa0);
        qabt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.0.12:8080/spring_ptj/portfolio2?kid=null"));
                startActivity(intent);

            }
        });


        viewPager = (ViewPager) findViewById(R.id.view);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        adapter = new Adapter(this);

        viewPager.setAdapter(adapter);

        dotscount = adapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

    }

    public class MyTimerTask extends TimerTask {

        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);

                    }else if(viewPager.getCurrentItem() == 2){
                        viewPager.setCurrentItem(3);
                    } else {
                        viewPager.setCurrentItem(0);

                    }
                }


            });
        }
    }

}