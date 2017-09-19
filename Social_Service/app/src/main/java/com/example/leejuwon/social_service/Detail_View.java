package com.example.leejuwon.social_service;
/**
 * Search_Service 에서 넘어온 인텐트를 받아서 자세한 내용을 보여주는 부분
 * */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Detail_View extends AppCompatActivity{
    String tel = null;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__view);

        TextView dtv1 = (TextView)findViewById(R.id.dtextView1);
        TextView dtv2 = (TextView)findViewById(R.id.dtextView2);
        TextView dtv3 = (TextView)findViewById(R.id.dtextView3);
        TextView dtv4 = (TextView)findViewById(R.id.dtextView4);
        TextView dtv5 = (TextView)findViewById(R.id.subtitle);

        intent = getIntent();
        dtv1.setText(intent.getStringExtra("01"));
        dtv2.setText(intent.getStringExtra("02"));
        dtv3.setText(intent.getStringExtra("04"));
        dtv4.setText(intent.getStringExtra("05"));
        dtv5.setText(intent.getStringExtra("06"));

        tel = "tel:"+intent.getStringExtra("03").toString();

        Button bt = (Button)findViewById(R.id.call);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tel));
                startActivity(intent);
            }
        });
    }
}