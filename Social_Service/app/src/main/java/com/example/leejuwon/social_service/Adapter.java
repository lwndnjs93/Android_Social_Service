package com.example.leejuwon.social_service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by yoon on 2017-07-25.
 */

public class Adapter extends PagerAdapter {

    private int[] images = {

            R.drawable.slide4,
            R.drawable.slide5,
            R.drawable.slide2,
            R.drawable.slide3
    };

    private LayoutInflater inflater;
    private Context context;

    public Adapter(Context context){
        this.context = context;
    };

    @Override
    public int getCount() {
        return images.length;//이미지 개수
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout)object);
    }


    //뷰페이저에서 사용할 뷰객체 생성/등록
    public Object instantiateItem(final ViewGroup container, final int position){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container,false);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class c = null;
                switch (position) {
                    case 0:
                        c = WebviewActivity.class;
                        break;
                    case 1:
                        c = WebviewActivity2.class;
                        break;

                    case 2:
                        c = WebviewActivity.class;
                        break;

                    case 3:
                        c = WebviewActivity.class;
                        break;


                    //and……
                }


                Intent intent = new Intent(context, c);
                context.startActivity(intent);

            }
        });




        container.addView(v);
        return v;

    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.invalidate();

    }

    public int[] getImages() {
        return images;
    }




}









