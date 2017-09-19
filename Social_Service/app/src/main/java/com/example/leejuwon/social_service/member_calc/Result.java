package com.example.leejuwon.social_service.member_calc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.leejuwon.social_service.R;

public class Result extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        intent = getIntent();
        String title = intent.getStringExtra("01");
        String result = intent.getStringExtra("02");
        String a00 = intent.getStringExtra("03");
        String a01 = intent.getStringExtra("04");
        String a02 = intent.getStringExtra("05");


        TextView tv_title = (TextView)findViewById(R.id.title);
        tv_title.setText(title);

        TextView tv_result = (TextView)findViewById(R.id.result);
        tv_result.setText(result);

        TextView tv_result2 = (TextView)findViewById(R.id.a);
        tv_result2.setText(a00);

        TextView tv_result3 = (TextView)findViewById(R.id.a2);
        tv_result3.setText(a01);

        TextView tv_result4 = (TextView)findViewById(R.id.a3);
        tv_result4.setText(a02);

    }
}