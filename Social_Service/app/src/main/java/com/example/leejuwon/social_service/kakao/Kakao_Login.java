package com.example.leejuwon.social_service.kakao;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.leejuwon.social_service.MainActivity;
import com.example.leejuwon.social_service.R;
import com.example.leejuwon.social_service.SplashActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import de.hdodenhof.circleimageview.CircleImageView;

public class Kakao_Login extends AppCompatActivity {
    private SessionCallback callback;
    TextView user_nickname, user_email;
    CircleImageView user_img;
    LinearLayout success_layout;
    Button logout_btn;
    LoginButton loginButton;

    String email = null;

    AQuery aQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao__login);
        aQuery = new AQuery(this);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        startActivity(new Intent(this, SplashActivity.class));

ImageView iv = (ImageView)findViewById(R.id.iv);

        GlideDrawableImageViewTarget ImageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(this).load(R.raw.kakao2).into(iv);




        //카카오톡 로그인 버튼
        loginButton = (LoginButton)findViewById(R.id.com_kakao_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (!isConnected()){
                        Toast.makeText(Kakao_Login.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                }

                if (isConnected()){
                    return false;
                }else {
                    return true;
                }
            }
        });

        //로그인 성공 시 사용할 뷰
        success_layout = (LinearLayout)findViewById(R.id.success_layout);
        user_nickname = (TextView)findViewById(R.id.user_nickname);
        user_img = (CircleImageView)findViewById(R.id.user_img);
        user_email = (TextView)findViewById(R.id.user_email);
        logout_btn = (Button)findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getCurrentSession().isOpened()){
                    requestLogout();
                }
            }
        });

        if (Session.getCurrentSession().isOpened()){
            requestMe();
        }else {
            success_layout.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }

        Button bt00 = (Button)findViewById(R.id.guest);
        bt00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Kakao_Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
        requestLogout();
    }

    //인터넷 연결상태 확인
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback{

        @Override
        public void onSessionOpened() {
            //access 토큰을 성공적으로 발급받아 valid access token을 가지고 있는 상태.
            //일반적으로 로그인 후의 다음 액티비티로 이동한다
            if (Session.getCurrentSession().isOpened()){
                //requestMe();
                Intent intent = new Intent(Kakao_Login.this, MainActivity.class);
                intent.putExtra("01", email);
                startActivity(intent);
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null){
                Logger.e(exception);
            }
        }
    }

    private void requestLogout(){
        success_layout.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(Kakao_Login.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void requestMe(){
        success_layout.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult){
                Log.e("onFailure", errorResult+"");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("onSessionClosed", errorResult+"");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("onSuccess", userProfile.toString());
                user_nickname.setText(userProfile.getNickname());
                user_email.setText(userProfile.getEmail());
                email = userProfile.getEmail();
                aQuery.id(user_img).image(userProfile.getThumbnailImagePath()); // <- 프로필 작은 이미지, user
            }

            @Override
            public void onNotSignedUp() {
                Log.e("onNotSignedUp", "onNotSignedUp");

            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        requestLogout();
        Session.getCurrentSession().removeCallback(callback);
    }
}
