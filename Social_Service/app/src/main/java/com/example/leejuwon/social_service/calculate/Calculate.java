package com.example.leejuwon.social_service.calculate;
/**
 * 예상 수령액 계산하기 페이지
 * 계산하는 페이지 넘어가기 전의 선택 리스트를 보여준다
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.leejuwon.social_service.R;
import com.example.leejuwon.social_service.calculate.basic01.Basic01;
import com.example.leejuwon.social_service.calculate.basic_life.Basic_Life01;
import com.example.leejuwon.social_service.calculate.disability.Disability01;
import com.example.leejuwon.social_service.calculate.disability_child.Disability_Child01;
import com.example.leejuwon.social_service.calculate.singleparent.Single_Parent01;

public class Calculate extends AppCompatActivity {
    Intent intent;
    String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate);
        intent = getIntent();
        if (intent.getExtras().getString("01") != null){
            email = intent.getExtras().getString("01");
        }

        //기초연금 계산하기
        ImageButton button1 = (ImageButton) findViewById(R.id.basic01);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculate.this, Basic01.class);
                if (email != null) {
                    intent.putExtra("01", email);
                }
                startActivity(intent);
            }
        });

        //장애(아동)수당
        ImageButton button02 = (ImageButton) findViewById(R.id.disability_child);
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculate.this, Disability_Child01.class);
                if (email != null) {
                    intent.putExtra("01", email);
                }
                startActivity(intent);
            }
        });

        //장애인 연금
        ImageButton button03 = (ImageButton) findViewById(R.id.disability);
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculate.this, Disability01.class);
                if (email != null) {
                    intent.putExtra("01", email);
                }
                startActivity(intent);
            }
        });

        //한부모 가족 지원
        ImageButton button04 = (ImageButton) findViewById(R.id.single_parent);
        button04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculate.this, Single_Parent01.class);
                if (email != null) {
                    intent.putExtra("01", email);
                }
                startActivity(intent);
            }
        });

        //국민기초생활보장
        ImageButton button05 = (ImageButton) findViewById(R.id.basic02);
        button05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculate.this, Basic_Life01.class);
                if (email != null) {
                    intent.putExtra("01", email);
                }
                startActivity(intent);
            }
        });
    }
}