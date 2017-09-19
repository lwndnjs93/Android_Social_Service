package com.example.leejuwon.social_service;

/**
 * 주변에 복지 서비스를 받을 수 있는 곳을 검색하고 경로 안내를 해준다
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.leejuwon.social_service.cluster.Info;
import com.example.leejuwon.social_service.cluster.OwnRendring;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static android.os.Build.VERSION_CODES.M;


public class Search_Course extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        PlacesListener {

    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker CurrentMarker = null;

    //디폴트 위치 서울
    private static final LatLng DEAFULT_LOCATION = new LatLng(37.56, 126.97);
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000; //1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000; //1초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;

    LatLng currentPosition;
    List<Marker> previous_marker = null;

    String tel = null;
    Intent Int_tel;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.search__course);

        previous_marker = new ArrayList<Marker>();

        //정부기관 위치 찍어주는 버튼
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goverment_office();
            }
        });

        //복지시설 위치 찍어주는 버튼
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welfare_facilities();
            }
        });

        //의료시설 위치 찍어주는 버튼
        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospital();
            }
        });

        //드림카드 가맹점 위치 찍어주는 버튼
        Button button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dream_card();
            }
        });


        mActivity = this;

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            /** 금정구 공공기관 리스트 시작 **/
            if (marker.getTitle().equals("근로복지공단 (부산동부지사)")) {
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (부곡3동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (구서1동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (장전1동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (구서2동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (부곡2동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("국민건강보험공단 (부산금정지사)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (장전3동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (장전2동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (부곡1동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (부곡4동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (서1동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (금사동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (서3동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (남산동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (선두구동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (금성동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (서2동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주민센터 (청룡노포동)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }
            /** 금정구 공공기관 리스트 끝 **/

            /** 금정구 복지시설 리스트 시작 **/
            else if (marker.getTitle().equals("금정구 정신건강 증진센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("예은노일방문요양센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사회복지 동행 재가센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사회복지 동행")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산소망재가장기요양센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("연천재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산특수치료교육연구원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("초원의집")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("글누리지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("화목재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("고려재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("참사랑 어른복지 센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이지특수교육연구소")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동부산수화통역센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하나플랜")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("노인세상 재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("구서지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("라파엘노인데이케어센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금샘아동발달센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한국종합재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("현대노인방문요양복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한우리 방문요양센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정내일의집")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("누리지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사랑의선교회")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("MSP건강복지사회서비스센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정안마원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성림의료기")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("어르신이 행복한 세상")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산인지학습연구소")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한빛노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("마음나누기 미술심리 상담센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("나래복지센터 금정점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산노인재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하나샘재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세영복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동성원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("효원재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("상록재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("정다운지역사회서비스센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사랑노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("돌봄노인재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산대학교신학협력단")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동래노인요양원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동래양로원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("신망애노인요양원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정예향 재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부곡장기요양지원센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산동부좋은이웃지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성심어머니집")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("보듬자리")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("인애원재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("지성안마지압원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부곡지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산아동일시보호소")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성애원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("은혜노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("안평노인요양원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("선아원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이룸데이케어센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("선아의집")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("가마실로직업재활원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("정심방문요양센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아이앤유케어 부산지점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("애광노인치매전문요양원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("섬기는 재가요양센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("가톨릭사회복지회")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("활기찬노후생활 재가요양원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사랑홈")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("예일지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("산모도우미119")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("행복바다주간보호센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("새롬재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("행복노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("신영노인재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("우리재가센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("해피앤드림")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조아 부산지점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("에이플사회서비스센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("해오름노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("크는나무아동상담소")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성림메디칼")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정구장애인복지관")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("꽃과 함께하는 집")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정자활장기요양센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정구지역자활센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산금정시니어클럽")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("다솜재가센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성실노인복지재가")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("소망의쉼터재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조은재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("희망그루터기 사회서비스센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정구장애인근로작업장")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세웅재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("우림재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("다경노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("협력공부방")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("희망을담은 휠체어수리센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("애담재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정희망의지")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사랑안마원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사랑나눔재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금천재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이웃나눔방문요양센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("서동성가공부방")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("운해재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금사나너울지역아동")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동부산재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("비전재가복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("브레인업")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("예닮공부방")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("도담도담인지발달센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("두레지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동래직업재활센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아이누리 인재개발교육원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("서동지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부성노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("꽃과 함께하는 친구들")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금샘마을 지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동부복지지원센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("삶의 터전 복지지원센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("엘림재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("해피케어복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("해오름지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("자비동산")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("열빙재가노인복지센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바오로 아람터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정노인요양원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정구종합사회복지관")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정구노인복지관")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("여성긴급전화 1366 부산센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("희락원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하회원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남광종합사회복지관")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("햇살지역아동센터")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("호메디")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }
            /** 금정구 복지시설 리스트 끝 **/

            /** 금정구 의료기관 리스트 시작 **/
            else if (marker.getTitle().equals("이진우이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산광역시 금정구 보건소")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("나라요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성심의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("항소담외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("프랜드금치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("경락한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("수석한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("수사랑의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("명인당한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미소유의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("진부부치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이현정소아청소년과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("턱편한잎새치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조이피부과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("우리정신겅강의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동안치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조내과김경미산부인과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한독의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("몸편한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("리더스차치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이상열마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김박산부인과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("강남미르비뇨기과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("굿모닝치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("BHS동래한서요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김성재한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("강지혜소아청년과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김건치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아카데미피부과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동희한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("봄빛안과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한솔내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("화창한외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("탑치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("구서치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("영이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("느티나무치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김진구내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김태균내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미소마취통증의학과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("최양숙소아청소년과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한빛한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세인이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("맨하탄치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("도원아이열자비한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("행복한치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("허태율한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김종주의행복의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("당당한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("뉴플란트치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("황윤권정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("구서한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("태곤한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("심우영이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성심치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("권한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("보리한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미소웰 산부인과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하이안치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("위더스예쁜이치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("늘편한내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산하나이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("자연한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("프라임치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("맑은숲한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("단골정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("새로운정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미래어린이치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("엘린의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바른몸청담한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("참조은치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("향사랑외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한결치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동경한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("장전치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김철수이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("순여성병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("신현성내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("구서바른이치과교정")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("주안치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("국민한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("백보한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("내성한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미로한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("최내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산마이크로병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("굿서울치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김옥영소아청소년과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("안치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("태연한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조성재치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김봉겸치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("행복한외과병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세종한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("지온정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("캐슬치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바른몸치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("경의상훈한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("행림요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("예담한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("지니산부인과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("거북이한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("노엘치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("갤러리안과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("올리브치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("브이엘의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("뉴아미치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("센텀린의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남산내과 신경정신병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("밝은홍안과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한빛치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("류청치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("창해한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("모철호치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("정인영치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("혜광한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하루미치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동신외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("보문정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("본치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("디킴스피부과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("우리메티컬의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("두실부부치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이장희외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남산안과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("두실한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("참사랑마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조인찬마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("윤정신의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("엘피부과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("청아치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("혜은한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부산휴내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("최근우내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동신라벨라의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성수치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("도영한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("심홍택치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("초하한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("메드월병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("우리들치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("마리여성의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이정삼이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("코끼리한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김종두소아청소년과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("장한기안과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("씨에프정훈비뇨기과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("명성치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("박기남마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("사랑요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김두정정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("최난금이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("소담한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("푸른사과치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김은경치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("태명산내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("보람요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하늘치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("소나무한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("맑은샘한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("윤치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하나의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("마음향기병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("삼세한방병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("패밀리치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김미경가정의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("하나가정의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바른몸맑은한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미소드림치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("양지요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이편한치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조미정치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이엔이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("규림요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부곡한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("차치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동비한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("효당한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("전명호내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("든든한덕모신경외과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김철언정형외과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("에스엔이치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("신공한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김경수내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("박성철소아청년과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이기범내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("봉황치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("최진섭신경외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("정영기내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("박기봉치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("관자재요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("베스트의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("천수한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("온정한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("박영근내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조생한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("다스림한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남산선치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김비뇨기과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("맑은샘가정의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세왕요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("패밀리병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("손한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("더고운피부과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("온천이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("수신제가김한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("온앤정신건강의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바로나치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("다나마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("진한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("노현태한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정형주요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부곡요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이강민치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한사랑의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("새우리남산병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("수가정의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동래미치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("경원한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이경일내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("해산한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("대도한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("송광한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("침례병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("예림한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남산하나정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한독외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김태관치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("정성일의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김경택이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("평광의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("제일요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("누가정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("광명한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한솔마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남산삼성의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("우리의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("제제한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김혁한치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("용한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("현대의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("민부부치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("청도한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("창성한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미소고운선치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("백용운소아청년과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("송지한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("나치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김태균마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미래의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미소치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("푸른내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("대평한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세종외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남산제일내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("삼성치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("우송한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세림한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이가정의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("새샘내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김상천치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("손동훈정형외과")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("조상호치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("정성모한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아름다운치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("윤희성치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("범어연합치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이종호피부과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("행복의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이만희이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("서동치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("후생한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이성한한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("다정한내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("옥승철내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("온새미가족사랑의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("남산치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("마디본정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("윤이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김앤박 치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("서동정안과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김안과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성봉훈치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("이상목이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("박종성내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세웅병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("서동한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("차병철치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세웅치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("채경석비뇨기과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("권미경치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("소림한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("강남의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("굿닥터의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("전윤숙소아청년과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미네소타치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("노덕현이비인후과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("수마취통증의학과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("큰으뜸내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아름다운강산병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("서동제일요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("제일한방병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("정한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동명의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("민들레내과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바른정치과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성모정형외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("보광한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("산업보건협회 부산의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("박민근외과의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("보광요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("화창한의원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금샘요양병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("동래병원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }
            /** 의료기관 리스트 금정구 끝 **/

            /** 드림카드 가맹점 리스트 금정구 시작 **/
            else if (marker.getTitle().equals("만권만권화밥부산대")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("민권 니뽀내뽕 부산대")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("고봉민김밥인남산점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금사탑베이커리")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금샘루")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금송짬뽕")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금정농협두구지점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("금화루")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("길림성황궁쟁반짜장")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥나라")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥마왕 서동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥사랑")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥천국 부곡동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥천국 서동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥천국 장전동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥천국 두실역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥천국 온천점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥천국 팔송점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김밥천국두실점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("김인규돈까스전문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("다래성")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("다옴")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("달려라 황궁쟁반짜장")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("대가호")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("뚜레쥬르부산대학교점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("뚱스밥버거 외대점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("몽쉐리과자점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("미원반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바니바니")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바이더웨이구서역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("바이더웨이부산서동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("번개반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("봉구스 밥버거")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("봉구스밥버거 구서점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("봉구스밥버거범어사점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("부곡반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("북경")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("산동반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("산들가는길")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("성화양분식")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세광(바비박스)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐구서선경점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐남산시장점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부대북문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산가톨릭대1호점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산가톨릭베리타스점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산구서우성점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산금사예원점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산금정점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산금정팰리스점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산남산럭키점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산남산팔송로점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산대북문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산대역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산대제2도서관점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산대제일점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산대학로점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산대후문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산부곡점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산삼한골든뷰점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산성우오스타점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산외대금샘점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산외대기숙사점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산외대럭키점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산외대엔젤점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산외대원룸점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산장전낙원점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산장전블루밍점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산장전사랑점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산장전소망점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산장전온천점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산장전원룸점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐부산청룡경동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐서동2호점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("세븐일레븐장전역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("송해반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("스콘과자점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아리랑")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아리산")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("아서원")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("오니기리와이규동(부산대지하철역점)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("오봉도시락부산대점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("와룡반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("용호각")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("원초김밥")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("유가네닭갈비부산외대점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("일송면옥")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("제일반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("진부령황태남산점(노들)")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("짱꼴라손짜장")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("찰스돈까스")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("천복반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("청룡각")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("친구야친구야분식")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("코코스 베이커리")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("큰집")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("파리바게뜨 부곡뉴그린점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한솥도시락")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한솥도시락")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("한스델리")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("행운반점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("호호돼지국밥")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("홍해루")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("황태자베이커리")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU구서럭키점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU구서롯데점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU구서벽산점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU구서센타점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU구서예가점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU구서유림점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU남산가람점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU남산네오점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU남산무지개점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU남산지하철역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU남산한마음점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU노포지하철역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU두실역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU범어사역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU범어사지하철역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU부곡쌍용점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU부곡점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU부산대정문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU부산대학교앞역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU부산대학역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU부산대행운점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU부산회동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU서동럭키점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU서동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU서동태광점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU세웅병원점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU장전그린코아점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU장전삼광점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU장전원룸점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("CU장전한신점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25구서골드점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25구서삼백점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25구서역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25구서참빛점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25금사대우점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25금사사랑점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25금정구청점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25금정점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25남산삼한점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25남산역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25남산점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25남산중앙점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25남산참빛점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25노포1호점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25두실선경점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25두실점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25범어사역점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부곡그린점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부곡쌍용점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부곡점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부곡중앙점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부대남문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부대북문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부대샛별점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부대정문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부대제일점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부대테라스파크점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부산고속도점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부산금사점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부산대학사점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부산청룡점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부산카톨릭대점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25부산회동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25서동금사점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25서동로점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25서동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25서동제일점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25서동중앙점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25외대정문점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25장전금강점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25장전대동점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25장전사랑점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25장전효원점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }else if (marker.getTitle().equals("GS25청룡점")){
                Int_tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+marker.getSnippet()));
                startActivity(Int_tel);
            }
            /** 드림카드 가맹점 리스트 금정구 끝 **/
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

        //앱 정보에서 퍼미션을 허가했는지 다시 검사해야 한다
        if (askPermissionOnceAgain) {
            if (Build.VERSION.SDK_INT >= M) {
                askPermissionOnceAgain = false;
                checkPermissions();
            }
        }
    }

    @Override
    public void onPause() {
        //위치 업데이트 중지
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);

            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        Log.d(TAG, "onMapReady");
        mGoogleMap = map;

        //런타임 퍼미션 요청 대화상자나 GPS활성 요청 대화상자 보이기 전에
        //지도의 초기위치를 서울로 이동
        setCurrentLocation(null, "위치정보 가져올 수 없음", "위치 퍼미션과 GPS 활성 여부 확인하세요");
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        if (Build.VERSION.SDK_INT >= M) {
            //API 23이상이면 런타임 퍼미션 처리 필요
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                if (mGoogleApiClient == null) {
                    buildGoogleApiClient();
                }
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
        } else {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentPosition = new LatLng(location.getLatitude(), location.getLongitude()); //currentPosition에 현재 위치 저장
        Log.d(TAG, "onLocationChanged");
        String markerTitle = getCurrentAddress(location);
        String markerSnippet = "위도" + String.valueOf(location.getLatitude())
                + "경도" + String.valueOf(location.getLongitude());
        //현재 위치에 마커 생성
        setCurrentLocation(location, markerTitle, markerSnippet);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        Log.d(TAG, "onConnected");
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        if (Build.VERSION.SDK_INT >= M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            }
        } else {
            Log.d(TAG, "onConnected : call FusedLocationAPI");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Location location = null;
        location.setLatitude(DEAFULT_LOCATION.latitude);
        location.setLongitude(DEAFULT_LOCATION.longitude);
        setCurrentLocation(location, "위치정보 가져올 수 없음", "위치 퍼미션과 GPS 활성 여부 확인하세요");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play Service" +
                    "connection lost, Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");

    }

    public String getCurrentAddress(Location location) {
        //지오코더 GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (CurrentMarker != null) CurrentMarker.remove();

        if (location != null) {

            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            //마커를 원하는 이미지로 변경해줘야함
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            CurrentMarker = mGoogleMap.addMarker(markerOptions);
            if (i == 0) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            }
            i++;
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEAFULT_LOCATION);
        markerOptions.title(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        CurrentMarker = mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(DEAFULT_LOCATION));
    }

    //여기서 부터는 런타임 퍼미션 처리를 위한 메소드들
    @TargetApi(M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야 합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음)" +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야 합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (permsRequestCode
                == PERMISSION_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {
            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {
                if (mGoogleApiClient == null) {
                    buildGoogleApiClient();
                }
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                }
            } else {
                checkPermissions();
            }
        }
    }

    @TargetApi(M)
    private void showDialogForPermission(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Search_Course.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Search_Course.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    //여기서부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Search_Course.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성화 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        if (Build.VERSION.SDK_INT >= M) {
                            if (ActivityCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                                mGoogleMap.setMyLocationEnabled(true);
                            }
                        } else mGoogleMap.setMyLocationEnabled(true);
                        return;
                    }
                } else {
                    setCurrentLocation(null, "위치정보 가져올 수 없음",
                            "위치 퍼미션과 GPS 활성 여부 확인하세요");
                }
                break;
        }
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {
                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(place.getVicinity());
                    Marker item = mGoogleMap.addMarker(markerOptions);
                    previous_marker.add(item);
                }
                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);
            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    //공공기관 위치 찍어주는 메소드
    public void goverment_office() {
        mGoogleMap.clear(); //지도 클리어

        if (previous_marker != null)
            previous_marker.clear(); //지역정보 마커 클리어

        ClusterManager<Info> mClusterManager = new ClusterManager<Info>(this, mGoogleMap);
        mGoogleMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mGoogleMap, mClusterManager));

        mClusterManager.addItem(new Info(new LatLng(35.2420443, 129.0914166), "근로복지공단 (부산동부지사)", "051-550-3296"));
        mClusterManager.addItem(new Info(new LatLng(35.2405477, 129.0938326), "주민센터 (부곡3동)", "051-519-5250"));
        mClusterManager.addItem(new Info(new LatLng(35.2450748, 129.0870553), "주민센터 (구서1동)", "051-519-5400"));
        mClusterManager.addItem(new Info(new LatLng(35.2377978, 129.0849724), "주민센터 (장전1동)", "051-519-5288"));
        mClusterManager.addItem(new Info(new LatLng(35.2552069, 129.0906033), "주민센터 (구서2동)", "051-519-5427"));
        mClusterManager.addItem(new Info(new LatLng(35.2298267, 129.0926941), "주민센터 (부곡2동)", "051-519-5226"));
        mClusterManager.addItem(new Info(new LatLng(35.2096651, 129.0095546), "국민건강보험공단 (부산금정지사)", "051-580-8110"));
        mClusterManager.addItem(new Info(new LatLng(35.2274935, 129.0855282), "주민센터 (장전3동)", "051-519-4912"));
        mClusterManager.addItem(new Info(new LatLng(35.2277712, 129.080973), "주민센터 (장전2동)", "051-519-5300"));
        mClusterManager.addItem(new Info(new LatLng(35.2243551, 129.0921388), "주민센터 (부곡1동)", "051-519-5207"));
        mClusterManager.addItem(new Info(new LatLng(35.2199111, 129.0885559), "주민센터 (부곡4동)", "051-519-4909"));
        mClusterManager.addItem(new Info(new LatLng(35.218356, 129.0991383), "주민센터 (서1동)", "051-519-5100"));
        mClusterManager.addItem(new Info(new LatLng(35.2203282, 129.1111093), "주민센터 (금사동)", "부산광역시 금정구 금사로 85번길 12 388-4"));
        mClusterManager.addItem(new Info(new LatLng(35.2172173, 129.1039434), "주민센터 (서3동)", "051-519-5150"));
        mClusterManager.addItem(new Info(new LatLng(35.2715718, 129.0922208), "주민센터 (남산동)", "051-519-4975"));
        mClusterManager.addItem(new Info(new LatLng(35.2982634, 129.1132736), "주민센터 (선두구동)", "051-519-5346"));
        mClusterManager.addItem(new Info(new LatLng(35.2503236, 129.0561692), "주민센터 (금성동)", "051-519-5446"));
        mClusterManager.addItem(new Info(new LatLng(35.2128845, 129.1045546), "주민센터 (서2동)", "051-519-4902"));
        mClusterManager.addItem(new Info(new LatLng(35.2752102, 129.0898598), "주민센터 (청룡노포동)", "051-519-4914"));

        mGoogleMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }

    //복지시설 위치 찍어주는 메소드0
    public void welfare_facilities() {
        mGoogleMap.clear();
        if (previous_marker != null)
            previous_marker.clear();

        ClusterManager<Info> mClusterManager = new ClusterManager<Info>(this, mGoogleMap);
        mGoogleMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mGoogleMap, mClusterManager));
        mClusterManager.addItem(new Info(new LatLng(35.243103, 129.092249), "금정구 정신건강 증진센터", "051-518-8700"));
        mClusterManager.addItem(new Info(new LatLng(35.2430821, 129.0941021), "예은노일방문요양센터", "070-8650-0077"));
        mClusterManager.addItem(new Info(new LatLng(35.2431, 129.092255), "사회복지 동행 재가센터", "051-582-3253"));
        mClusterManager.addItem(new Info(new LatLng(35.2438375, 129.0902007), "사회복지 동행", "051-582-3253"));
        mClusterManager.addItem(new Info(new LatLng(35.244916, 129.092726), "부산소망재가장기요양센터", "051-518-6571"));
        mClusterManager.addItem(new Info(new LatLng(35.2447327, 129.0899842), "연천재가노인복지센터", "051-515-8004"));
        mClusterManager.addItem(new Info(new LatLng(35.2465034, 129.0908429), "부산특수치료교육연구원", "051-514-2031"));
        mClusterManager.addItem(new Info(new LatLng(35.238776, 129.0953275), "초원의집", "051-582-1562"));
        mClusterManager.addItem(new Info(new LatLng(35.2398115, 129.0875474), "글누리지역아동센터", "051-581-6996"));
        mClusterManager.addItem(new Info(new LatLng(35.2417569, 129.0863131), "화목재가노인복지센터", "051-507-3855"));
        mClusterManager.addItem(new Info(new LatLng(35.244766, 129.092767), "고려재가노인복지센터", "051-818-5113"));
        mClusterManager.addItem(new Info(new LatLng(35.23644, 129.090111), "참사랑 어른복지 센터", "051-000-0000"));
        mClusterManager.addItem(new Info(new LatLng(35.2373286, 129.0919099), "이지특수교육연구소", "051-582-2078"));
        mClusterManager.addItem(new Info(new LatLng(35.2388871, 129.087301), "동부산수화통역센터", "051-513-6350"));
        mClusterManager.addItem(new Info(new LatLng(35.23982, 129.0861972), "하나플랜", "051-581-0117"));
        mClusterManager.addItem(new Info(new LatLng(35.2374986, 129.0876428), "노인세상 재가복지센터", "051-513-1341"));
        mClusterManager.addItem(new Info(new LatLng(35.2485024, 129.0886589), "구서지역아동센터", "051-505-1925"));
        mClusterManager.addItem(new Info(new LatLng(35.2447919, 129.1001714), "라파엘노인데이케어센터", "051-510-0821"));
        mClusterManager.addItem(new Info(new LatLng(35.2357688, 129.087455), "금샘아동발달센터", "051-515-9294"));
        mClusterManager.addItem(new Info(new LatLng(35.234123, 129.0926393), "한국종합재가복지센터", "051-581-8114"));
        mClusterManager.addItem(new Info(new LatLng(35.2340379, 129.0906815), "현대노인방문요양복지센터", "051-512-9955"));
        mClusterManager.addItem(new Info(new LatLng(35.234702, 129.087597), "한우리 방문요양센터", "051-527-8870"));
        mClusterManager.addItem(new Info(new LatLng(35.252083, 129.0907221), "금정내일의집", "051-995-1077"));
        mClusterManager.addItem(new Info(new LatLng(35.2325411, 129.0910416), "누리지역아동센터", "051-512-1044"));
        mClusterManager.addItem(new Info(new LatLng(35.2321123, 129.0939086), "사랑의선교회", "051-518-8425"));
        mClusterManager.addItem(new Info(new LatLng(35.2327719, 129.0866273), "MSP건강복지사회서비스센터", "051-516-8486"));
        mClusterManager.addItem(new Info(new LatLng(35.2544222, 129.0896128), "금정안마원", "051-582-9896"));
        mClusterManager.addItem(new Info(new LatLng(35.2546825, 129.0911338), "성림의료기", "051-512-3777"));
        mClusterManager.addItem(new Info(new LatLng(35.2549418, 129.0926603), "어르신이 행복한 세상", "051-552-0815"));
        mClusterManager.addItem(new Info(new LatLng(35.2546782, 129.0892041), "부산인지학습연구소", "070-4109-5721"));
        mClusterManager.addItem(new Info(new LatLng(35.256049, 129.08835), "한빛노인복지센터", "051-512-1110"));
        mClusterManager.addItem(new Info(new LatLng(35.2563151, 129.090503), "마음나누기 미술심리 상담센터", "051-909-8919"));
        mClusterManager.addItem(new Info(new LatLng(35.256931, 129.087144), "나래복지센터 금정점", "051-583-3991"));
        mClusterManager.addItem(new Info(new LatLng(35.2283649, 129.0918634), "부산노인재가복지센터", "051-583-2552"));
        mClusterManager.addItem(new Info(new LatLng(35.2282421, 129.093682), "하나샘재가복지센터", "051-902-7962"));
        mClusterManager.addItem(new Info(new LatLng(35.2574987, 129.0916936), "세영복지센터", "051-513-4567"));
        mClusterManager.addItem(new Info(new LatLng(35.2574949, 129.0906199), "동성원", "051-582-4635"));
        mClusterManager.addItem(new Info(new LatLng(35.2285892, 129.0862683), "효원재가복지센터", "051-504-0044"));
        mClusterManager.addItem(new Info(new LatLng(35.2286966, 129.0855414), "상록재가복지센터", "051-583-0767"));
        mClusterManager.addItem(new Info(new LatLng(35.2577178, 129.0904855), "정다운지역사회서비스센터", "051-517-7008"));
        mClusterManager.addItem(new Info(new LatLng(35.2578804, 129.0900459), "사랑노인복지센터", "051-508-5222"));
        mClusterManager.addItem(new Info(new LatLng(35.219933, 129.088323), "돌봄노인재가복지센터", "051-903-8030"));
        mClusterManager.addItem(new Info(new LatLng(35.2352264, 129.0684021), "부산대학교신학협력단", "051-510-1981"));
        mClusterManager.addItem(new Info(new LatLng(35.2317644, 129.0763159), "동래노인요양원", "051-518-8275"));
        mClusterManager.addItem(new Info(new LatLng(35.2317644, 129.0763159), "동래양로원", "051-582-1468"));
        mClusterManager.addItem(new Info(new LatLng(35.2316184, 129.0771214), "신망애노인요양원", "051-582-1664"));
        mClusterManager.addItem(new Info(new LatLng(35.2593338, 129.088306), "금정예향 재가복지센터", "051-514-9736"));
        mClusterManager.addItem(new Info(new LatLng(35.225736, 129.093812), "부곡장기요양지원센터", "051-581-5850"));
        mClusterManager.addItem(new Info(new LatLng(35.2257073, 129.0880951), "부산동부좋은이웃지역아동센터", "070-8931-2443"));
        mClusterManager.addItem(new Info(new LatLng(35.2299131, 129.0774185), "성심어머니집", "051-515-0834"));
        mClusterManager.addItem(new Info(new LatLng(35.2302631, 129.0768794), "보듬자리", "051-518-5849"));
        mClusterManager.addItem(new Info(new LatLng(35.2251043, 129.0914968), "인애원재가복지센터", "051-513-9066"));
        mClusterManager.addItem(new Info(new LatLng(35.2248031, 129.094708), "지성안마지압원", "051-515-9675"));
        mClusterManager.addItem(new Info(new LatLng(35.224461, 129.0929623), "부곡지역아동센터", "051-514-4111"));
        mClusterManager.addItem(new Info(new LatLng(35.2338538, 129.0804729), "부산아동일시보호소", "051-583-7314"));
        mClusterManager.addItem(new Info(new LatLng(35.2295891, 129.0761443), "성애원", "051-582-4287"));
        mClusterManager.addItem(new Info(new LatLng(35.2239886, 129.0899791), "은혜노인복지센터", "051-516-2483"));
        mClusterManager.addItem(new Info(new LatLng(35.2290013, 129.0762585), "안평노인요양원", "051-518-6838"));
        mClusterManager.addItem(new Info(new LatLng(35.2286525, 129.0765549), "선아원", "051-516-6661"));
        mClusterManager.addItem(new Info(new LatLng(35.2286525, 129.0765549), "이룸데이케어센터", "051-514-2842"));
        mClusterManager.addItem(new Info(new LatLng(35.2286525, 129.0765549), "선아의집", "051-582-0089"));
        mClusterManager.addItem(new Info(new LatLng(35.2240305, 129.0955159), "가마실로직업재활원", "051-582-5055"));
        mClusterManager.addItem(new Info(new LatLng(35.2617937, 129.0876937), "정심방문요양센터", "051-514-5857"));
        mClusterManager.addItem(new Info(new LatLng(35.225288, 129.081894), "아이앤유케어 부산지점", "051-361-0449"));
        mClusterManager.addItem(new Info(new LatLng(35.2292954, 129.0751111), "애광노인치매전문요양원", "051-514-7737"));
        mClusterManager.addItem(new Info(new LatLng(35.227415, 129.077089), "섬기는 재가요양센터", "070-7527-4805"));
        mClusterManager.addItem(new Info(new LatLng(35.223125, 129.0940813), "가톨릭사회복지회", "051-517-0615"));
        mClusterManager.addItem(new Info(new LatLng(35.2225359, 129.0946314), "활기찬노후생활 재가요양원", "051-513-8876"));
        mClusterManager.addItem(new Info(new LatLng(35.2246558, 129.0921586), "사랑홈", "051-513-9024"));
        mClusterManager.addItem(new Info(new LatLng(35.2635004, 129.0925477), "예일지역아동센터", "051-582-7912"));
        mClusterManager.addItem(new Info(new LatLng(35.2231508, 129.0856049), "산모도우미119", "051-805-3519"));
        mClusterManager.addItem(new Info(new LatLng(35.2635857, 129.0880859), "행복바다주간보호센터", "051-867-7557"));
        mClusterManager.addItem(new Info(new LatLng(35.2213628, 129.0908909), "새롬재가복지센터", "051-907-0047"));
        mClusterManager.addItem(new Info(new LatLng(35.2644719, 129.0876326), "행복노인복지센터", "051-515-7830"));
        mClusterManager.addItem(new Info(new LatLng(35.264581, 129.09418), "신영노인재가복지센터", "051-582-0995"));
        mClusterManager.addItem(new Info(new LatLng(35.2646362, 129.0878438), "우리재가센터", "051-518-3324"));
        mClusterManager.addItem(new Info(new LatLng(35.2646926, 129.0883864), "해피앤드림", "051-518-0114"));
        mClusterManager.addItem(new Info(new LatLng(35.2654557, 129.0921278), "조아 부산지점", "051-863-8111"));
        mClusterManager.addItem(new Info(new LatLng(35.265398, 129.087828), "에이플사회서비스센터", "051-582-8277"));
        mClusterManager.addItem(new Info(new LatLng(35.2197127, 129.0887445), "해오름노인복지센터", "051-513-3931"));
        mClusterManager.addItem(new Info(new LatLng(35.2194183, 129.0866975), "크는나무아동상담소", "051-513-6655"));
        mClusterManager.addItem(new Info(new LatLng(35.2667307, 129.0930769), "성림메디칼", "051-581-5896"));
        mClusterManager.addItem(new Info(new LatLng(35.21959, 129.098873), "금정구장애인복지관", "051-523-0100"));
        mClusterManager.addItem(new Info(new LatLng(35.2666195, 129.0907092), "꽃과 함께하는 집", "051-583-7194"));
        mClusterManager.addItem(new Info(new LatLng(35.2672617, 129.0924641), "금정자활장기요양센터", "051-508-2163"));
        mClusterManager.addItem(new Info(new LatLng(35.2672617, 129.0924641), "금정구지역자활센터", "051-508-2163"));
        mClusterManager.addItem(new Info(new LatLng(35.2671404, 129.088518), "부산금정시니어클럽", "051-516-3045"));
        mClusterManager.addItem(new Info(new LatLng(35.2671835, 129.0876394), "다솜재가센터", "051-582-3546"));
        mClusterManager.addItem(new Info(new LatLng(35.2180916, 129.0933931), "성실노인복지재가", "051-583-6627"));
        mClusterManager.addItem(new Info(new LatLng(35.2180894, 129.0967311), "소망의쉼터재가노인복지센터", "051-531-3333"));
        mClusterManager.addItem(new Info(new LatLng(35.2180921, 129.0971266), "조은재가노인복지센터", "051-527-2205"));
        mClusterManager.addItem(new Info(new LatLng(35.2178465, 129.0955767), "희망그루터기 사회서비스센터", "051-516-1070"));
        mClusterManager.addItem(new Info(new LatLng(35.2183002, 129.0991384), "금정구장애인근로작업장", "051-529-3145"));
        mClusterManager.addItem(new Info(new LatLng(35.2177574, 129.0866699), "세웅재가노인복지센터", "051-983-7491"));
        mClusterManager.addItem(new Info(new LatLng(35.2244323, 129.1144952), "우림재가노인복지센터", "051-522-8266"));
        mClusterManager.addItem(new Info(new LatLng(35.2263962, 129.1166729), "다경노인복지센터", "051-531-3328"));
        mClusterManager.addItem(new Info(new LatLng(35.2168545, 129.098719), "협력공부방", "051-526-0975"));
        mClusterManager.addItem(new Info(new LatLng(35.2174174, 129.1023413), "희망을담은 휠체어수리센터", "051-529-8801"));
        mClusterManager.addItem(new Info(new LatLng(35.2181696, 129.1051568), "애담재가노인복지센터", "051-523-6508"));
        mClusterManager.addItem(new Info(new LatLng(35.2172052, 129.1020814), "금정희망의지", "051-526-1033"));
        mClusterManager.addItem(new Info(new LatLng(35.2170578, 129.1019345), "사랑안마원", "051-525-9697"));
        mClusterManager.addItem(new Info(new LatLng(35.2173886, 129.1033218), "사랑나눔재가복지센터", "051-521-2357"));
        mClusterManager.addItem(new Info(new LatLng(35.2207313, 129.1115524), "금천재가노인복지센터", "051-522-1973"));
        mClusterManager.addItem(new Info(new LatLng(35.2167984, 129.1026043), "이웃나눔방문요양센터", "051-528-0500"));
        mClusterManager.addItem(new Info(new LatLng(35.218356, 129.0991383), "서동성가공부방", "051-522-4881"));
        mClusterManager.addItem(new Info(new LatLng(35.2195145, 129.1106692), "운해재가노인복지센터", "051-521-6000"));
        mClusterManager.addItem(new Info(new LatLng(35.2201808, 129.1119228), "금사나너울지역아동", "051-531-6228"));
        mClusterManager.addItem(new Info(new LatLng(35.2185961, 129.1090772), "동부산재가복지센터", "051-8526412"));
        mClusterManager.addItem(new Info(new LatLng(35.215145, 129.098042), "비전재가복지센터", "051-513-3534"));
        mClusterManager.addItem(new Info(new LatLng(35.2710104, 129.0923683), "브레인업", "051-917-0075"));
        mClusterManager.addItem(new Info(new LatLng(35.2165254, 129.1047731), "예닮공부방", "051-529-9096"));
        mClusterManager.addItem(new Info(new LatLng(35.2709684, 129.0876262), "도담도담인지발달센터", "051-515-1030"));
        mClusterManager.addItem(new Info(new LatLng(35.2711757, 129.0918375), "두레지역아동센터", "051-517-5080"));
        mClusterManager.addItem(new Info(new LatLng(35.2715718, 129.0922206), "동래직업재활센터", "051-581-4165"));
        mClusterManager.addItem(new Info(new LatLng(35.2717983, 129.0913644), "아이누리 인재개발교육원", "070-7310-0187"));
        mClusterManager.addItem(new Info(new LatLng(35.2158624, 129.1053232), "서동지역아동센터", "051-526-6279"));
        mClusterManager.addItem(new Info(new LatLng(35.225766, 129.115965), "부성노인복지센터", "051-522-5069"));
        mClusterManager.addItem(new Info(new LatLng(35.2739606, 129.0847491), "꽃과 함께하는 친구들", "051-911-7194"));
        mClusterManager.addItem(new Info(new LatLng(35.272181, 129.08691), "금샘마을 지역아동센터", "051-513-2866"));
        mClusterManager.addItem(new Info(new LatLng(35.2142578, 129.1028687), "동부복지지원센터", "051-525-9018"));
        mClusterManager.addItem(new Info(new LatLng(35.215185, 129.107256), "삶의 터전 복지지원센터", "051-902-3333"));
        mClusterManager.addItem(new Info(new LatLng(35.2124606, 129.1027455), "엘림재가노인복지센터", "051-527-9190"));
        mClusterManager.addItem(new Info(new LatLng(35.2739988, 129.0868259), "해피케어복지센터", "051-508-5395"));
        mClusterManager.addItem(new Info(new LatLng(35.2139898, 129.1022005), "해오름지역아동센터", "051-525-9797"));
        mClusterManager.addItem(new Info(new LatLng(35.2144888, 129.1122966), "자비동산", "051-555-1560"));
        mClusterManager.addItem(new Info(new LatLng(35.2142553, 129.1167043), "열빙재가노인복지센터", "051-522-9007"));
        mClusterManager.addItem(new Info(new LatLng(35.2776432, 129.0888179), "바오로 아람터", "051-514-6988"));
        mClusterManager.addItem(new Info(new LatLng(35.277745, 129.0890216), "금정노인요양원", "051-508-8822"));
        mClusterManager.addItem(new Info(new LatLng(35.214463, 129.118458), "금정구종합사회복지관", "051-532-0115"));
        mClusterManager.addItem(new Info(new LatLng(35.2762367, 129.0899457), "금정구노인복지관", "051-792-7200"));
        mClusterManager.addItem(new Info(new LatLng(35.2836825, 129.0963782), "여성긴급전화 1366 부산센터", "051-508-2969"));
        mClusterManager.addItem(new Info(new LatLng(35.2931263, 129.0867726), "희락원", "051-508-1133"));
        mClusterManager.addItem(new Info(new LatLng(35.293231, 129.099577), "하회원", "051-508-2894"));
        mClusterManager.addItem(new Info(new LatLng(35.293231, 129.099577), "남광종합사회복지관", "051-508-1997"));
        mClusterManager.addItem(new Info(new LatLng(35.3035939, 129.1101843), "햇살지역아동센터", "051-508-0340"));
        mClusterManager.addItem(new Info(new LatLng(35.302549, 129.113054), "호메디", "070-4174-8533"));

        mGoogleMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }

    //병원 위치 찍어주는 메소드
    public void hospital() {
        mGoogleMap.clear();
        if (previous_marker != null)
            previous_marker.clear();

        ClusterManager<Info> mClusterManager = new ClusterManager<Info>(this, mGoogleMap);
        mGoogleMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mGoogleMap, mClusterManager));
        mClusterManager.addItem(new Info(new LatLng(35.226616, 129.083309), "이진우이비인후과의원", "051-517-0112"));
        mClusterManager.addItem(new Info(new LatLng(35.2633454, 129.0954903), "부산광역시 금정구 보건소", "051-519-5033"));
        mClusterManager.addItem(new Info(new LatLng(35.2438805, 129.0938271), "나라요양병원", "051-513-1284"));
        mClusterManager.addItem(new Info(new LatLng(35.2409234, 129.0931908), "주치과의원", "051-516-5276"));
        mClusterManager.addItem(new Info(new LatLng(35.2405586, 129.0936693), "성심의원", "051-512-0288"));
        mClusterManager.addItem(new Info(new LatLng(35.2349811, 129.0879285), "항소담외과의원", "051-582-7517"));
        mClusterManager.addItem(new Info(new LatLng(35.2403742, 129.0914245), "프랜드금치과의원", "051-518-4800"));
        mClusterManager.addItem(new Info(new LatLng(35.2403742, 129.0914245), "경락한의원", "051-900-4343"));
        mClusterManager.addItem(new Info(new LatLng(35.2444545, 129.08834), "수석한의원", "051-518-7733"));
        mClusterManager.addItem(new Info(new LatLng(35.2392025, 129.0915142), "수사랑의원", "070-4099-4830"));
        mClusterManager.addItem(new Info(new LatLng(35.2392025, 129.0915142), "명인당한의원", "051-515-8575"));
        mClusterManager.addItem(new Info(new LatLng(35.2392752, 129.0909494), "미소유의원", "051-518-2114"));
        mClusterManager.addItem(new Info(new LatLng(35.2392752, 129.0909494), "진부부치과의원", "051-582-8655"));
        mClusterManager.addItem(new Info(new LatLng(35.2392937, 129.0907002), "이현정소아청소년과", "051-583-8002"));
        mClusterManager.addItem(new Info(new LatLng(35.2392937, 129.0907002), "턱편한잎새치과의원", "051-512-7572"));
        mClusterManager.addItem(new Info(new LatLng(35.2392937, 129.0907002), "조이피부과의원", "051-582-7573"));
        mClusterManager.addItem(new Info(new LatLng(35.2392937, 129.0907002), "우리정신겅강의학과의원", "051-581-8275"));
        mClusterManager.addItem(new Info(new LatLng(35.2393757, 129.0905036), "동안치과의원", "051-516-1121"));
        mClusterManager.addItem(new Info(new LatLng(35.2392937, 129.0907002), "조내과김경미산부인과의원", "051-512-2060"));
        mClusterManager.addItem(new Info(new LatLng(35.244657, 129.0880735), "한독의원", "051-582-1302"));
        mClusterManager.addItem(new Info(new LatLng(35.2431817, 129.0871463), "몸편한의원", "051-913-7582"));
        mClusterManager.addItem(new Info(new LatLng(35.2385955, 129.0921554), "리더스차치과의원", "051-518-0251"));
        mClusterManager.addItem(new Info(new LatLng(35.2550737, 129.0904992), "이상열마취통증의학과의원", "051-516-0888"));
        mClusterManager.addItem(new Info(new LatLng(35.2550737, 129.0904992), "김박산부인과의원", "051-514-2626"));
        mClusterManager.addItem(new Info(new LatLng(35.2550737, 129.0904992), "강남미르비뇨기과의원", "051-514-1808"));
        mClusterManager.addItem(new Info(new LatLng(35.2384165, 129.092119), "굿모닝치과의원", "051-517-2833"));
        mClusterManager.addItem(new Info(new LatLng(35.238294, 129.0916031), "BHS동래한서요양병원", "051-509-3000"));
        mClusterManager.addItem(new Info(new LatLng(35.2463327, 129.0889804), "김성재한의원", "051-516-2782"));
        mClusterManager.addItem(new Info(new LatLng(35.2466187, 129.0890425), "강지혜소아청년과의원", "051-513-7931"));
        mClusterManager.addItem(new Info(new LatLng(35.2466187, 129.0890425), "김건치과의원", "051-582-2211"));
        mClusterManager.addItem(new Info(new LatLng(35.2466187, 129.0890425), "아카데미피부과의원", "051-512-7577"));
        mClusterManager.addItem(new Info(new LatLng(35.2468619, 129.0894611), "동희한의원", "051-515-1116"));
        mClusterManager.addItem(new Info(new LatLng(35.2466187, 129.0890425), "봄빛안과의원", "051-5168-1823"));
        mClusterManager.addItem(new Info(new LatLng(35.2466187, 129.0890425), "한솔내과의원", "051-581-3242"));
        mClusterManager.addItem(new Info(new LatLng(35.2474302, 129.0920157), "화창한외과의원", "051-514-9119"));
        mClusterManager.addItem(new Info(new LatLng(35.2468706, 129.0893041), "탑치과의원", "051-515-7511"));
        mClusterManager.addItem(new Info(new LatLng(35.2468799, 129.0890625), "구서치과의원", "051-581-0081"));
        mClusterManager.addItem(new Info(new LatLng(35.2466089, 129.0885369), "영이비인후과의원", "051-513-1050"));
        mClusterManager.addItem(new Info(new LatLng(35.2466089, 129.0885369), "느티나무치과의원", "051-516-2875"));
        mClusterManager.addItem(new Info(new LatLng(35.2466089, 129.0885369), "김진구내과의원", "051-514-2280"));
        mClusterManager.addItem(new Info(new LatLng(35.2379424, 129.0913194), "김태균내과의원", "051-703-8875"));
        mClusterManager.addItem(new Info(new LatLng(35.2379424, 129.0913194), "미소마취통증의학과", "051-581-7581"));
        mClusterManager.addItem(new Info(new LatLng(35.24699, 129.0887563), "최양숙소아청소년과의원", "051-517-6038"));
        mClusterManager.addItem(new Info(new LatLng(35.2379202, 129.0909796), "한빛한의원", "051-515-2114"));
        mClusterManager.addItem(new Info(new LatLng(35.24699, 129.0887563), "세인이비인후과의원", "051-514-3332"));
        mClusterManager.addItem(new Info(new LatLng(35.2377496, 129.0920056), "맨하탄치과의원", "051-512-8270"));
        mClusterManager.addItem(new Info(new LatLng(35.2374954, 129.0913046), "도원아이열자비한의원", "051-514-1515"));
        mClusterManager.addItem(new Info(new LatLng(35.2469346, 129.0885006), "행복한치과의원", "051-516-8275"));
        mClusterManager.addItem(new Info(new LatLng(35.2469346, 129.0885006), "허태율한의원", "051-513-6644"));
        mClusterManager.addItem(new Info(new LatLng(35.2398443, 129.0867095), "김종주의행복의원", "051-581-4004"));
        mClusterManager.addItem(new Info(new LatLng(35.237678, 129.0901524), "당당한의원", "051-515-8275"));
        mClusterManager.addItem(new Info(new LatLng(35.237678, 129.0901524), "뉴플란트치과의원", "051-518-2830"));
        mClusterManager.addItem(new Info(new LatLng(35.2381865, 129.088632), "황윤권정형외과의원", "051-515-5713"));
        mClusterManager.addItem(new Info(new LatLng(35.2479296, 129.0899149), "구서한의원", "051-518-8500"));
        mClusterManager.addItem(new Info(new LatLng(35.2481344, 129.0908901), "태곤한의원", "051-582-7510"));
        mClusterManager.addItem(new Info(new LatLng(35.2371791, 129.0919002), "심우영이비인후과의원", "051-514-2330"));
        mClusterManager.addItem(new Info(new LatLng(35.2398135, 129.0863235), "성심치과의원", "051-512-7776"));
        mClusterManager.addItem(new Info(new LatLng(35.2377868, 129.090568), "권한의원", "051-512-2222"));
        mClusterManager.addItem(new Info(new LatLng(35.2484116, 129.0918442), "보리한의원", "051-581-0123"));
        mClusterManager.addItem(new Info(new LatLng(35.2484472, 129.0909843), "미소웰 산부인과의원", "051-516-0079"));
        mClusterManager.addItem(new Info(new LatLng(35.2484389, 129.0911073), "하이안치과의원", "051-518-7750"));
        mClusterManager.addItem(new Info(new LatLng(35.2484389, 129.0911073), "위더스예쁜이치과의원", "051-514-7475"));
        mClusterManager.addItem(new Info(new LatLng(35.2484472, 129.0909843), "늘편한내과의원", "051-529-1199"));
        mClusterManager.addItem(new Info(new LatLng(35.2484472, 129.0909843), "부산하나이비인후과의원", "051-513-7570"));
        mClusterManager.addItem(new Info(new LatLng(35.2484472, 129.0909843), "자연한의원", "051-911-7582"));
        mClusterManager.addItem(new Info(new LatLng(35.2484472, 129.0909843), "프라임치과의원", "051-582-7582"));
        mClusterManager.addItem(new Info(new LatLng(35.2484884, 129.0907234), "맑은숲한의원", "051-512-5150"));
        mClusterManager.addItem(new Info(new LatLng(35.2487752, 129.0917534), "단골정형외과의원", "051-582-0934"));
        mClusterManager.addItem(new Info(new LatLng(35.2486065, 129.0905184), "새로운정형외과의원", "051-514-8446"));
        mClusterManager.addItem(new Info(new LatLng(35.2487752, 129.0917534), "미래어린이치과의원", "051-517-2875"));
        mClusterManager.addItem(new Info(new LatLng(35.2499046, 129.0908031), "엘린의원", "051-714-3701"));
        mClusterManager.addItem(new Info(new LatLng(35.2486065, 129.0905184), "바른몸청담한의원", "051-504-1060"));
        mClusterManager.addItem(new Info(new LatLng(35.2486065, 129.0905184), "참조은치과의원", "051-515-2875"));
        mClusterManager.addItem(new Info(new LatLng(35.2486065, 129.0905184), "향사랑외과의원", "051-517-7582"));
        mClusterManager.addItem(new Info(new LatLng(35.2379311, 129.0876618), "한결치과의원", "051-518-2888"));
        mClusterManager.addItem(new Info(new LatLng(35.2378057, 129.0876711), "동경한의원", "051-513-7595"));
        mClusterManager.addItem(new Info(new LatLng(35.2385811, 129.0866465), "장전치과의원", "051-581-2220"));
        mClusterManager.addItem(new Info(new LatLng(35.2377021, 129.0876708), "김철수이비인후과의원", "051-582-5125"));
        mClusterManager.addItem(new Info(new LatLng(35.2364606, 129.0914094), "순여성병원", "051-515-0005"));
        mClusterManager.addItem(new Info(new LatLng(35.2377783, 129.0870072), "신현성내과의원", "051-513-6248"));
        mClusterManager.addItem(new Info(new LatLng(35.2489707, 129.0898123), "구서바른이치과교정", "051-515-2885"));
        mClusterManager.addItem(new Info(new LatLng(35.2378786, 129.0866789), "주안치과의원", "051-518-0900"));
        mClusterManager.addItem(new Info(new LatLng(35.2488077, 129.0884894), "국민한의원", "051-516-2761"));
        mClusterManager.addItem(new Info(new LatLng(35.2378157, 129.0863617), "백보한의원", "051-582-5522"));
        mClusterManager.addItem(new Info(new LatLng(35.2375181, 129.0858798), "내성한의원", "051-517-0121"));
        mClusterManager.addItem(new Info(new LatLng(35.2492551, 129.0872581), "미로한의원", "051-581-0124"));
        mClusterManager.addItem(new Info(new LatLng(35.2365799, 129.0866638), "최내과의원", "051-514-6822"));
        mClusterManager.addItem(new Info(new LatLng(35.2517073, 129.0910038), "부산마이크로병원", "051-580-9112"));
        mClusterManager.addItem(new Info(new LatLng(35.2512708, 129.0880246), "굿서울치과의원", "051-582-7998"));
        mClusterManager.addItem(new Info(new LatLng(35.2514223, 129.0880222), "김옥영소아청소년과", "051-517-1275"));
        mClusterManager.addItem(new Info(new LatLng(35.2514223, 129.0880222), "안치과의원", "051-518-2288"));
        mClusterManager.addItem(new Info(new LatLng(35.2321269, 129.0902912), "태연한의원", "051-518-0019"));
        mClusterManager.addItem(new Info(new LatLng(35.2321269, 129.0902912), "조성재치과의원", "051-581-1113"));
        mClusterManager.addItem(new Info(new LatLng(35.2341085, 129.0860464), "김봉겸치과의원", "051-518-5155"));
        mClusterManager.addItem(new Info(new LatLng(35.2322219, 129.0920535), "행복한외과병원", "051-583-8252"));
        mClusterManager.addItem(new Info(new LatLng(35.2532726, 129.0911468), "세종한의원", "051-571-7582"));
        mClusterManager.addItem(new Info(new LatLng(35.2535494, 129.0911406), "지온정형외과의원", "051-518-7007"));
        mClusterManager.addItem(new Info(new LatLng(35.2513462, 129.0864575), "캐슬치과의원", "051-582-2875"));
        mClusterManager.addItem(new Info(new LatLng(35.253502, 129.089706), "바른몸치과의원", "051-583-7588"));
        mClusterManager.addItem(new Info(new LatLng(35.2328718, 129.0858398), "경의상훈한의원", "051-583-1075"));
        mClusterManager.addItem(new Info(new LatLng(35.2544274, 129.0910337), "행림요양병원", "051-517-8211"));
        mClusterManager.addItem(new Info(new LatLng(35.2546825, 129.0911338), "예담한의원", "051-514-2580"));
        mClusterManager.addItem(new Info(new LatLng(35.2309821, 129.0887039), "지니산부인과의원", "051-582-8275"));
        mClusterManager.addItem(new Info(new LatLng(35.230858, 129.088838), "거북이한의원", "051-583-8989"));
        mClusterManager.addItem(new Info(new LatLng(35.231029, 129.088135), "노엘치과의원", "051-583-2822"));
        mClusterManager.addItem(new Info(new LatLng(35.2314673, 129.0858607), "갤러리안과의원", "051-516-0075"));
        mClusterManager.addItem(new Info(new LatLng(35.2315103, 129.0855888), "미의원", "051-518-5761"));
        mClusterManager.addItem(new Info(new LatLng(35.2310159, 129.0867445), "올리브치과의원", "051-583-7582"));
        mClusterManager.addItem(new Info(new LatLng(35.2311627, 129.0860587), "브이엘의원", "051-921-7979"));
        mClusterManager.addItem(new Info(new LatLng(35.2302732, 129.0897534), "뉴아미치과의원", "051-513-7525"));
        mClusterManager.addItem(new Info(new LatLng(35.2311453, 129.0856839), "센텀린의원", "051-583-8585"));
        mClusterManager.addItem(new Info(new LatLng(35.2555294, 129.0910792), "남산내과 신경정신병원", "051-582-9461"));
        mClusterManager.addItem(new Info(new LatLng(35.2555294, 129.0910792), "밝은홍안과의원", "051-514-3773"));
        mClusterManager.addItem(new Info(new LatLng(35.2296441, 129.0902437), "한빛치과의원", "051-583-2941"));
        mClusterManager.addItem(new Info(new LatLng(35.2563151, 129.090503), "류청치과의원", "051-515-2724"));
        mClusterManager.addItem(new Info(new LatLng(35.2563151, 129.090503), "창해한의원", "051-583-5445"));
        mClusterManager.addItem(new Info(new LatLng(35.2298323, 129.0865595), "모철호치과의원", "051-515-2275"));
        mClusterManager.addItem(new Info(new LatLng(35.2564161, 129.0911021), "정인영치과의원", "051-517-8000"));
        mClusterManager.addItem(new Info(new LatLng(35.2566685, 129.0911044), "혜광한의원 ", "051-517-1075"));
        mClusterManager.addItem(new Info(new LatLng(35.2566344, 129.0906809), "하루미치과의원", "051-516-2772"));
        mClusterManager.addItem(new Info(new LatLng(35.2295828, 129.0860366), "동신외과의원", "051-582-1001"));
        mClusterManager.addItem(new Info(new LatLng(35.2569134, 129.0910275), "보문정형외과의원", "051-581-0012"));
        mClusterManager.addItem(new Info(new LatLng(35.257184, 129.0911124), "본치과의원", "051-515-7475"));
        mClusterManager.addItem(new Info(new LatLng(35.2574697, 129.091014), "디킴스피부과의원", "051-512-3961"));
        mClusterManager.addItem(new Info(new LatLng(35.2574697, 129.091014), "우리메티컬의원", "051-517-8090"));
        mClusterManager.addItem(new Info(new LatLng(35.2574697, 129.091014), "두실부부치과의원", "051-512-2804"));
        mClusterManager.addItem(new Info(new LatLng(35.2286897, 129.0863136), "이장희외과의원", "051-518-7945"));
        mClusterManager.addItem(new Info(new LatLng(35.2575864, 129.0910607), "남산안과의원", "051-512-1115"));
        mClusterManager.addItem(new Info(new LatLng(35.2575864, 129.0910607), "두실한의원", "051-513-7500"));
        mClusterManager.addItem(new Info(new LatLng(35.2575864, 129.0910607), "참사랑마취통증의학과의원", "051-512-9001"));
        mClusterManager.addItem(new Info(new LatLng(35.2578782, 129.0910964), "조인찬마취통증의학과의원", "051-518-9038"));
        mClusterManager.addItem(new Info(new LatLng(35.2578782, 129.0910964), "윤정신의학과의원", "051-583-5002"));
        mClusterManager.addItem(new Info(new LatLng(35.2578782, 129.0910964), "엘피부과의원", "051-512-8275"));
        mClusterManager.addItem(new Info(new LatLng(35.2578782, 129.0910964), "청아치과의원", "051-583-2875"));
        mClusterManager.addItem(new Info(new LatLng(35.2578782, 129.0910964), "혜은한의원", "051-514-1075"));
        mClusterManager.addItem(new Info(new LatLng(35.2284426, 129.0853216), "부산휴내과의원", "051-714-1525"));
        mClusterManager.addItem(new Info(new LatLng(35.2577079, 129.0875864), "최근우내과의원", "051-514-9795"));
        mClusterManager.addItem(new Info(new LatLng(35.2580824, 129.0906365), "동신라벨라의원", "051-516-3383"));
        mClusterManager.addItem(new Info(new LatLng(35.228804, 129.0841291), "성수치과의원", "051-517-3463"));
        mClusterManager.addItem(new Info(new LatLng(35.2579007, 129.0874923), "도영한의원", "051-513-0855"));
        mClusterManager.addItem(new Info(new LatLng(35.2579007, 129.0874923), "심홍택치과의원", "051-514-3301"));
        mClusterManager.addItem(new Info(new LatLng(35.2581068, 129.0880269), "초하한의원", "051-518-2388"));
        mClusterManager.addItem(new Info(new LatLng(35.2585318, 129.0909865), "메드월병원", "051-519-8000"));
        mClusterManager.addItem(new Info(new LatLng(35.2580991, 129.0875762), "우리들치과의원", "051-581-7528"));
        mClusterManager.addItem(new Info(new LatLng(35.2269688, 129.0921479), "마리여성의원", "051-517-4591"));
        mClusterManager.addItem(new Info(new LatLng(35.2582415, 129.0875869), "이정삼이비인후과의원", "051-583-2672"));
        mClusterManager.addItem(new Info(new LatLng(35.2267942, 129.0921115), "코끼리한의원", "051-582-1611"));
        mClusterManager.addItem(new Info(new LatLng(35.2267942, 129.0921115), "김종두소아청소년과", "051-582-0280"));
        mClusterManager.addItem(new Info(new LatLng(35.2265737, 129.0920778), "장한기안과의원", "051-513-7700"));
        mClusterManager.addItem(new Info(new LatLng(35.2265737, 129.0920778), "씨에프정훈비뇨기과", "051-513-7576"));
        mClusterManager.addItem(new Info(new LatLng(35.2265737, 129.0920778), "명성치과의원", "051-514-3388"));
        mClusterManager.addItem(new Info(new LatLng(35.2265737, 129.0920778), "박기남마취통증의학과의원", "051-513-8080"));
        mClusterManager.addItem(new Info(new LatLng(35.2265835, 129.0896909), "사랑요양병원", "051-514-7277"));
        mClusterManager.addItem(new Info(new LatLng(35.2263848, 129.091638), "김두정정형외과의원", "051-583-8181"));
        mClusterManager.addItem(new Info(new LatLng(35.2263495, 129.0920506), "최난금이비인후과의원", "051-517-6735"));
        mClusterManager.addItem(new Info(new LatLng(35.2263495, 129.0920506), "소담한의원", "051-517-7975"));
        mClusterManager.addItem(new Info(new LatLng(35.2263495, 129.0920506), "푸른사과치과의원", "051-516-7528"));
        mClusterManager.addItem(new Info(new LatLng(35.2261946, 129.0920332), "김은경치과의원", "051-583-2806"));
        mClusterManager.addItem(new Info(new LatLng(35.2261742, 129.0917), "태명산내과의원", "051-513-0011"));
        mClusterManager.addItem(new Info(new LatLng( 35.2945273,129.1118283), "동래병원\n", "051-508-0011\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2768504,129.088272), "금샘요양병원\n", "051-931-0119\n"));
        mClusterManager.addItem(new Info(new LatLng(35.275684,129.086834), "화창한의원\n", "051-508-0614\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214467,129.112554), "보광요양병원\n", "051-527-5225\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2113447,129.1041329), "박민근외과의원\n", "051-521-3609\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2753665,129.092296), "산업보건협회 부산의원\n", "051-508-6088\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2746306,129.0848295), "보광한의원\n", "051-513-87873\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2127285,129.1068744), "성모정형외과의원\n", "051-525-3553\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2739517,129.0923627), "바른정치과의원\n", "051-710-0010\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2739517,129.0923627), "민들레내과의원\n", "051-714-5772\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2734041,129.092407), "동명의원\n", "051-508-6982\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2143895,129.1054285), "정한의원\n", "051-527-1075\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2143851,129.1052098), "제일한방병원\n", "051-523-7507\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2143851,129.1052098), "서동제일요양병원\n", "051-529-5500\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2162131,129.1098433), "아름다운강산병원\n", "051-553-9000\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2144376,129.1047932), "큰으뜸내과의원\n", "051-537-7005\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2144376,129.1047932), "수마취통증의학과의원\n", "051-524-8900\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2144376,129.1047932), "노덕현이비인후과의원\n", "051-527-2970\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2144376,129.1047932), "미네소타치과의원\n", "051-532-5323\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2144376,129.1047932), "전윤숙소아청년과의원\n", "051-522-8886\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2137594,129.1015736), "굿닥터의원\n", "051-524-0028\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2145315,129.1046769), "강남의원\n", "051-524-9996\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2142735,129.1033214), "소림한의원\n", "051-522-3735\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2145659,129.1044687), "권미경치과의원\n", "051-526-7081\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2145659,129.1044687), "채경석비뇨기과의원\n", "051-526-6391\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214399,129.1040842), "세웅치과의원\n", "051-523-1364\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2142735,129.1033214), "차병철치과의원\n", "051-531-3303\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2141593,129.1029947), "서동한의원\n", "051-522-7582\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214399,129.1040842), "세웅병원\n", "051-500-9700\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2147557,129.1046119), "박종성내과의원\n", "051-523-9339\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2147557,129.1046119), "이상목이비인후과의원\n", "051-528-1333\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2148145,129.1042821), "성봉훈치과의원\n", "051-527-6204\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2148145,129.1042821), "김안과의원\n", "051-527-9839\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2146697,129.1033641), "서동정안과의원\n", "051-714-7288\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2146697,129.1033641), "김앤박 치과의원\n", "051-532-2275\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2148422,129.1041111), "윤이비인후과의원\n", "051-529-8899\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214399,129.1040842), "마디본정형외과의원\n", "051-526-9191\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2722147,129.0886607), "남산치과의원\n", "051-515-1281\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2723174,129.0894721), "온새미가족사랑의원\n", "051-517-6675\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2147444,129.103174), "옥승철내과의원\n", "051-531-1195\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2144685,129.1028902), "다정한내과의원\n", "051-528-6471\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214991,129.103998), "이성한한의원\n", "051-5287-5756\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2151978,129.104664), "후생한의원\n", "051-527-6633\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214968,129.1036691), "서동치과의원\n", "051-529-7090\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214968,129.1036691), "이만희이비인후과의원\n", "051-523-1046\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2149329,129.103461), "행복의원\n", "051-522-2900\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214949,129.1032723), "이종호피부과의원\n", "051-527-7083\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2720036,129.0929883), "범어연합치과의원\n", "051-514-2080\n"));
        mClusterManager.addItem(new Info(new LatLng(35.214949,129.1032723), "윤희성치과의원\n", "051-529-7608\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2720036,129.0929883), "아름다운치과의원\n", "051-508-7676\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2720036,129.0929883), "정성모한의원\n", "051-517-1313\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2150544,129.1026861), "조상호치과의원\n", "051-522-6444\n"));
        mClusterManager.addItem(new Info(new LatLng(35.271873,129.0923744), "손동훈정형외과\n", "051-513-9300\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2151012,129.1026011), "김상천치과의원\n", "051-523-8603\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2152459,129.1032401), "새샘내과의원\n", "051-532-6660\n"));
        mClusterManager.addItem(new Info(new LatLng(35.271639,129.0924361), "이가정의학과의원\n", "051-517-0787\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2156461,129.1040264), "세림한의원\n", "051-521-1011\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2714111,129.0924231), "우송한의원\n", "051-583-0633\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2714111,129.0924231), "삼성치과의원\n", "051-514-1534\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2712071,129.0888472), "남산제일내과의원\n", "051-582-5526\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2181233,129.109631), "세종외과의원\n", "051-522-3028\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2154786,129.1020394), "대평한의원\n", "051-529-1075\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2711529,129.0923691), "푸른내과의원\n", "051-515-5311\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2709767,129.0889123), "미소치과의원\n", "051-582-7528\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2709767,129.0889123), "미래의원\n", "051-517-7177\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2710104,129.0923683), "김태균마취통증의학과의원\n", "051-622-8000\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2710104,129.0923683), "나치과의원\n", "051-582-4554\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2709778,129.0894882), "송지한의원\n", "051-517-2895\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2710104,129.0923683), "백용운소아청년과의원\n", "051-516-2612\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2193539,129.1113645), "미소고운선치과의원\n", "051-527-5785\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2707999,129.0897298), "창성한의원\n", "051-516-1075\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2158154,129.1009753), "청도한의원\n", "051-523-1997\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2200051,129.1113207), "민부부치과의원\n", "051-524-2511\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2164881,129.1026341), "현대의원\n", "051-521-6638\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2215555,129.1129927), "용한의원\n", "051-525-7577\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2212386,129.1123612), "김혁한치과의원\n", "051-531-5351\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2327151,129.0824728), "제제한의원\n", "051-516-5588"));
        mClusterManager.addItem(new Info(new LatLng(35.2210607,129.1120995), "우리의원\n", "051-5318-2912\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2327151,129.0824728), "남산삼성의원\n", "051-514-7747\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2684049,129.0912343), "한솔마취통증의학과의원\n", "051-512-8500\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2227409,129.1137603), "광명한의원\n", "051-532-5324\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2228554,129.1138585), "누가정형외과의원 \n", "051-531-2331\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2183286,129.1010565), "제일요양병원\n", "051-527-3800\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2177691,129.0859671), "평광의원\n", "051-515-8271\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2177115,129.0870592), "김경택이비인후과의원\n", "051-582-8861\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2176044,129.0957448), "정성일의원\n", "051-524-2947\n"));
        mClusterManager.addItem(new Info(new LatLng(35.217647,129.0954137), "김태관치과의원\n", "051-512-2820\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2178465,129.0955767), "한독외과의원\n", "051-515-4680\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2672617,129.0924641), "남산하나정형외과의원\n", "051-515-8033\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2668876,129.0883674), "예림한의원\n", "051-583-0311\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2675609,129.0940618), "침례병원\n", "051-580-2000\n"));
        mClusterManager.addItem(new Info(new LatLng(35.266422,129.0924265), "송광한의원\n", "051-515-5673\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2660858,129.0930243), "대도한의원\n", "051-512-6656\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2655654,129.09216), "이경일내과의원\n", "051-582-3399\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2655654,129.09216), "경원한의원\n", "051-517-8384\n"));
        mClusterManager.addItem(new Info(new LatLng( 35.2202412,129.0884154), "동래미치과의원\n", "051-512-2879\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2652963,129.0889132), "수가정의학과의원\n", "051-516-8787\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2204707,129.0885547), "금정한의원\n", "051-514-8875\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2652513,129.0921053), "새우리남산병원\n", "051-516-9999\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2207082,129.0889006), "한사랑의원\n", "051-512-3617\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2648637,129.0921088), "이강민치과의원\n", "051-516-9604\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2210589,129.0895972), "부곡요양병원\n", "051-512-6900\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2642182,129.0889131), "금정형주요양병원\n", "051-532-7533\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2641557,129.0919554), "노현태한의원\n", "051-512-8900\n"));
        mClusterManager.addItem(new Info(new LatLng(35.222317,129.0848084), "진한의원\n", "051-518-1275\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2223287,129.0850085), "다나마취통증의학과의원\n", "051-515-7141\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2223287,129.0850085), "바로나치과의원\n", "051-518-7582\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2223287,129.0850085), "온앤정신건강의학과의원\n", "051-504-7575\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2223287,129.0850085), "수신제가김한의원\n", "051-512-0123\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2223287, 129.0850085), "온천이비인후과의원\n", "051-512-4713\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2223287,129.0850085), "더고운피부과의원\n", "051-558-7575\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2639078,129.0918597), "손한의원\n", "051-5171-6145\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2183556,129.0809455), "패밀리병원\n", "051-515-0079\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2225355,129.0859701), "세왕요양병원\n", "051-468-9700\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2233144,129.0835474), "맑은샘가정의학과의원\n", "051-515-1665\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2632957,129.0917669), "김비뇨기과의원\n", "051-582-0807\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2632957,129.0917669), "남산선치과의원\n", "051-513-1275\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2242239,129.082438), "다스림한의원\n", "051-557-7588\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2245688,129.0821647), "조생한의원\n", "051-582-5533\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2245688,129.0821647), "박영근내과의원\n", "051-515-3004\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2238972,129.0839968), "온정한의원\n", "051-583-7325\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2247138,129.0826818), "천수한의원\n", "051-518-7801\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2252104,129.0818898), "베스트의원\n", "051-512-6206\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2615831,129.0937529), "관자재요양병원\n", "051-516-9898\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2614891,129.0913932), "박기봉치과의원\n", "051-516-8544\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2614891,129.0913932), "정영기내과의원\n", "051-517-7509\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2610888,129.0875953), "최진섭신경외과의원\n", "051-515-4845\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2610888,129.0875953), "봉황치과의원\n", "051-583-7522\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2610888, 129.0875953), "이기범내과의원\n", "051-515-7999\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2610888,129.0875953), "박성철소아청년과의원\n", "051-512-4142\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2241469,129.0911105), "김경수내과의원\n", "051-512-4142"));
        mClusterManager.addItem(new Info(new LatLng(35.2254239,129.0844235), "신공한의원\n", "051-583-1206\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2261029,129.0826827), "에스엔이치과의원\n", "051-244-2875\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2248502,129.0913923), "김철언정형외과\n", "051-513-5667\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2248502,129.0913923), "든든한덕모신경외과\n", "051-513-5666\n"));
        mClusterManager.addItem(new Info(new LatLng(35.22484,129.091402), "전명호내과의원\n", "051-518-1662\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2250043,129.0917964), "효당한의원\n", "051-583-8275\n"));
        mClusterManager.addItem(new Info(new LatLng(35.245575,129.0864434), "동비한의원\n", "051-582-5021\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2268289,129.0825352), "차치과의원\n", "051-516-2854\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2251043,129.0914968), "부곡한의원\n", "051-513-3232\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2307963,129.0761174), "규림요양병원\n", "051-582-1234\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2253111,129.0915512), "이엔이비인후과의원\n", "051-513-0081\n"));
        mClusterManager.addItem(new Info(new LatLng(129.0915512,129.0915512), "조미정치과의원\n", "051-515-7022\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2268884,129.0832358), "이편한치과의원\n", "051-583-1600\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2271285, 129.0827984), "양지요양병원\n", "051-582-5757\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2271285,129.0827984), "미소드림치과의원\n", "051-514-2875\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2271285,129.0827984), "바른몸맑은한의원\n", "051-512-6028\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2271285,129.0827984), "하나가정의학과의원\n", "051-517-1190\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2267207,129.083038), "김미경가정의학과의원\n", "051-582-7575"));
        mClusterManager.addItem(new Info(new LatLng(35.2600635,129.0911488), "패밀리치과의원\n", "051-517-4441\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2255835,129.089427), "삼세한방병원\n", "051-583-5400\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2308075,129.077088), "마음향기병원\n", "051-516-1220\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2256888,129.0919561), "하나의원\n", "051-517-5573\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2258513,129.092022), "윤치과의원\n", "051-516-2882\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2258513,129.092022), "맑은샘한의원\n", "051-582-0005\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2259611,129.0915379), "소나무한의원\n", "051-583-1010\n"));
        mClusterManager.addItem(new Info(new LatLng(35.2259611,129.0915379), "하늘치과의원\n", "051-582-2275\n"));
        mClusterManager.addItem(new Info(new LatLng(35.259389,129.0910848), "보람요양병원\n", "051-514-0075\n"));

        mGoogleMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }

    //드림카드 가맹점 위치 찍어주는 메소드
    public void dream_card() {
        mGoogleMap.clear();
        if (previous_marker != null)
            previous_marker.clear();

        ClusterManager<Info> mClusterManager = new ClusterManager<Info>(this, mGoogleMap);
        mGoogleMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), mGoogleMap, mClusterManager));

        mClusterManager.addItem(new Info(new LatLng(35.2300666, 129.0852565), "만권만권화밥부산대", "051-515-0633"));
        mClusterManager.addItem(new Info(new LatLng(35.2300666, 129.0852565), "민권 니뽀내뽕 부산대", "051-515-2848"));
        mClusterManager.addItem(new Info(new LatLng(35.2300666, 129.0852565), "고봉민김밥인남산점 ", "051-514-7896"));
        mClusterManager.addItem(new Info(new LatLng(35.2169752, 129.1107829), "금사탑베이커리", "051-523-8094"));
        mClusterManager.addItem(new Info(new LatLng(35.2507646, 129.0815529), "금샘루", "051-516-2989"));
        mClusterManager.addItem(new Info(new LatLng(35.2645956, 129.0864453), "금송짬뽕", "051-513-9841"));
        mClusterManager.addItem(new Info(new LatLng(35.2793976, 129.1079243), "금정농협두구지점", "051-508-6044"));
        mClusterManager.addItem(new Info(new LatLng(35.2142296, 129.1108762), "금화루", "051-527-3434"));
        mClusterManager.addItem(new Info(new LatLng(35.263795, 129.089246), "길림성황궁쟁반짜장", "051-515-8289"));
        mClusterManager.addItem(new Info(new LatLng(35.2295768, 129.0902675), "김밥나라", "051-512-5142"));
        mClusterManager.addItem(new Info(new LatLng(35.214714, 129.1048343), "김밥마왕 서동점 ", "051-531-8252"));
        mClusterManager.addItem(new Info(new LatLng(35.2689238, 129.0888639), "김밥사랑", "051-583-9078"));
        mClusterManager.addItem(new Info(new LatLng(35.2392422, 129.0896664), "김밥천국 부곡동점", "051-582-4555"));
        mClusterManager.addItem(new Info(new LatLng(35.214399, 129.1040842), "김밥천국 서동점", "051-531-1122"));
        mClusterManager.addItem(new Info(new LatLng(35.2349811, 129.0879285), "김밥천국 장전동점", "051-514-3989"));
        mClusterManager.addItem(new Info(new LatLng(35.2563862, 129.0907298), "김밥천국 두실역점", "051-518-6009"));
        mClusterManager.addItem(new Info(new LatLng(35.2206436, 129.0879737), "김밥천국 온천점", "051-583-4416"));
        mClusterManager.addItem(new Info(new LatLng(35.2718495, 129.0923597), "김밥천국 팔송점", "051-583-7211"));
        mClusterManager.addItem(new Info(new LatLng(35.251213, 129.0864163), "김밥천국두실점", "051-513-3039"));
        mClusterManager.addItem(new Info(new LatLng(35.2458482, 129.0915286), "김인규돈까스전문점", "051-518-0680"));
        mClusterManager.addItem(new Info(new LatLng(35.2752915, 129.0865134), "다래성", "051-583-5522"));
        mClusterManager.addItem(new Info(new LatLng(35.2695164, 129.0919431), "다옴", "051-515-0882"));
        mClusterManager.addItem(new Info(new LatLng(35.2377482, 129.0837594), "달려라 황궁쟁반짜장", "051-516-3222"));
        mClusterManager.addItem(new Info(new LatLng(35.2293352, 129.0832837), "대가호", "051-512-9044"));
        mClusterManager.addItem(new Info(new LatLng(35.2310159, 129.0867445), "뚜레쥬르부산대학교점", "051-516-0203"));
        mClusterManager.addItem(new Info(new LatLng(35.2646558, 129.0868604), "뚱스밥버거 외대점", "051-938-2378"));
        mClusterManager.addItem(new Info(new LatLng(35.2566094, 129.0902531), "몽쉐리과자점 ", "051-583-1400"));
        mClusterManager.addItem(new Info(new LatLng(35.2336596, 129.0937772), "미원반점", "051-515-1151"));
        mClusterManager.addItem(new Info(new LatLng(35.2232919, 129.0912715), "바니바니", "010-8419-2023"));
        mClusterManager.addItem(new Info(new LatLng(35.2461581, 129.0906661), "바이더웨이구서역점", "051-583-8302"));
        mClusterManager.addItem(new Info(new LatLng(35.2151796, 129.1072448), "바이더웨이부산서동점", "051-522-3095"));
        mClusterManager.addItem(new Info(new LatLng(35.2580653, 129.0896352), "번개반점", "051-582-2277"));
        mClusterManager.addItem(new Info(new LatLng(35.228542, 129.0909101), "봉구스 밥버거", "051-582-3000"));
        mClusterManager.addItem(new Info(new LatLng(35.2469079, 129.0882774), "봉구스밥버거 구서점", "051-502-9090"));
        mClusterManager.addItem(new Info(new LatLng(35.2753769, 129.0922485), "봉구스밥버거범어사점", "051-583-3000"));
        mClusterManager.addItem(new Info(new LatLng(35.2244542, 129.0918154), "부곡반점", "051-518-5310"));
        mClusterManager.addItem(new Info(new LatLng(35.2256148, 129.0802079), "북경", "051-517-8589"));
        mClusterManager.addItem(new Info(new LatLng(35.2142553, 129.1167043), "산동반점", "051-526-1666"));
        mClusterManager.addItem(new Info(new LatLng(35.2490465, 129.085972), "산들가는길", "051-582-0320"));
        mClusterManager.addItem(new Info(new LatLng(35.2176046, 129.0944169), "성화양분식", "051-581-6238"));
        mClusterManager.addItem(new Info(new LatLng(35.214399, 129.1040842), "세광(바비박스)", "051-529-7890"));
        mClusterManager.addItem(new Info(new LatLng(35.2582595, 129.0862871), "세븐일레븐구서선경점", "051-513-4852"));
        mClusterManager.addItem(new Info(new LatLng(35.2727032, 129.0883726), "세븐일레븐남산시장점", "051-516-8988"));
        mClusterManager.addItem(new Info(new LatLng(35.233634, 129.084199), "세븐일레븐부대북문점  ", "051-513-0755"));
        mClusterManager.addItem(new Info(new LatLng(35.2443806, 129.0973322), "세븐일레븐부산가톨릭대1호점", "070-4200-8931"));
        mClusterManager.addItem(new Info(new LatLng(35.2442247, 129.0997906), "세븐일레븐부산가톨릭베리타스점", "070-4201-8931"));
        mClusterManager.addItem(new Info(new LatLng(35.2546958, 129.087555), "세븐일레븐부산구서우성점", "051-514-9377"));
        mClusterManager.addItem(new Info(new LatLng(35.2250855, 129.1158981), "세븐일레븐부산금사예원점", "051-521-4700"));
        mClusterManager.addItem(new Info(new LatLng(35.2296277, 129.0924571), "세븐일레븐부산금정점", "051-581-2989"));
        mClusterManager.addItem(new Info(new LatLng(35.2234106, 129.0844451), "세븐일레븐부산금정팰리스점", "051-518-5955"));
        mClusterManager.addItem(new Info(new LatLng(35.264983, 129.089426), "세븐일레븐부산남산럭키점", "051-518-5744"));
        mClusterManager.addItem(new Info(new LatLng(35.271376, 129.091798), "세븐일레븐부산남산팔송로점", "051-512-8769"));
        mClusterManager.addItem(new Info(new LatLng(35.2351292, 129.0839902), "세븐일레븐부산대북문점", "051-516-4710"));
        mClusterManager.addItem(new Info(new LatLng(35.2313905, 129.0898864), "세븐일레븐부산대역점", "051-513 -3383"));
        mClusterManager.addItem(new Info(new LatLng(35.2352264, 129.0684021), "세븐일레븐부산대제2도서관점", "051-510-1274"));
        mClusterManager.addItem(new Info(new LatLng(35.2318767, 129.0874437), "세븐일레븐부산대제일점", "051-513-0425"));
        mClusterManager.addItem(new Info(new LatLng(35.2338203, 129.0867182), "세븐일레븐부산대학로점", "02-3284-8168"));
        mClusterManager.addItem(new Info(new LatLng(35.2296188, 129.0828216), "세븐일레븐부산대후문점", "070-7529-6675"));
        mClusterManager.addItem(new Info(new LatLng(35.2259611, 129.0915379), "세븐일레븐부산부곡점", "051-583-2793"));
        mClusterManager.addItem(new Info(new LatLng(35.259959, 129.091113), "세븐일레븐부산삼한골든뷰점", "051-514-5384"));
        mClusterManager.addItem(new Info(new LatLng(35.2227858, 129.0850003), "세븐일레븐부산성우오스타점 ", "051-557-2035"));
        mClusterManager.addItem(new Info(new LatLng(35.2507646, 129.0815529), "세븐일레븐부산외대금샘점", "051-583-1092"));
        mClusterManager.addItem(new Info(new LatLng(35.2673966, 129.0787513), "세븐일레븐부산외대기숙사점", "051-512-6552"));
        mClusterManager.addItem(new Info(new LatLng(35.264049, 129.087493), "세븐일레븐부산외대럭키점", "010-6718-7747"));
        mClusterManager.addItem(new Info(new LatLng(35.2671436, 129.0873597), "세븐일레븐부산외대엔젤점", ""));
        mClusterManager.addItem(new Info(new LatLng(35.2623065, 129.0860972), "세븐일레븐부산외대원룸점", "070-8879-6522"));
        mClusterManager.addItem(new Info(new LatLng(35.235736, 129.08536), "세븐일레븐부산장전낙원점", "010-5159-3306"));
        mClusterManager.addItem(new Info(new LatLng(35.2284579, 129.0797809), "세븐일레븐부산장전블루밍점", "051-583-8175"));
        mClusterManager.addItem(new Info(new LatLng(35.2285189, 129.0852611), "세븐일레븐부산장전사랑점", "051-516-7665"));
        mClusterManager.addItem(new Info(new LatLng(35.2311813, 129.0886979), "세븐일레븐부산장전소망점", ""));
        mClusterManager.addItem(new Info(new LatLng(35.2336458, 129.0882909), "세븐일레븐부산장전온천점", ""));
        mClusterManager.addItem(new Info(new LatLng(35.239041, 129.083519), "세븐일레븐부산장전원룸점", "051-518-8038"));
        mClusterManager.addItem(new Info(new LatLng(35.2792623, 129.0882812), "세븐일레븐부산청룡경동점", "051-463-0813"));
        mClusterManager.addItem(new Info(new LatLng(35.2535494, 129.0911406), "세븐일레븐서동2호점", "051-523-4037"));
        mClusterManager.addItem(new Info(new LatLng(35.2376988, 129.0889353), "세븐일레븐장전역점", "051-583-1421"));
        mClusterManager.addItem(new Info(new LatLng(35.2690721, 129.0942207), "송해반점", "051-517-4543"));
        mClusterManager.addItem(new Info(new LatLng(35.2378257, 129.0870278), "스콘과자점", "051-582-5537"));
        mClusterManager.addItem(new Info(new LatLng(35.2179117, 129.1063321), "아리랑", "051-525-8880"));
        mClusterManager.addItem(new Info(new LatLng(35.2666074, 129.0922509), "아리산", "051-517-7747"));
        mClusterManager.addItem(new Info(new LatLng(35.2426234, 129.0936872), "아서원", "051-518-1463"));
        mClusterManager.addItem(new Info(new LatLng(35.2306327, 129.0888596), "오니기리와이규동(부산대지하철역점)", "051-514-8588"));
        mClusterManager.addItem(new Info(new LatLng(35.2459168, 129.0862221), "오봉도시락부산대점", "051-582-5001"));
        mClusterManager.addItem(new Info(new LatLng(35.2203002, 129.1111092), "와룡반점", "051-521-5279"));
        mClusterManager.addItem(new Info(new LatLng(35.2670445, 129.0910544), "용호각", "051-512-8923"));
        mClusterManager.addItem(new Info(new LatLng(35.2671662, 129.0912594), "원초김밥", "051-514-1110"));
        mClusterManager.addItem(new Info(new LatLng(35.2651213, 129.0888251), "유가네닭갈비부산외대점", "051-583-0340"));
        mClusterManager.addItem(new Info(new LatLng(35.2286632, 129.0918073), "일송면옥", "051-514-3381"));
        mClusterManager.addItem(new Info(new LatLng(35.219828, 129.0993882), "제일반점", "051-527-7803"));
        mClusterManager.addItem(new Info(new LatLng(35.2715759, 129.091934), "진부령황태남산점(노들)", "051-514-4411"));
        mClusterManager.addItem(new Info(new LatLng(35.2200223, 129.0904724), "짱꼴라손짜장", "051-516-7387"));
        mClusterManager.addItem(new Info(new LatLng(35.2753716, 129.0895328), "찰스돈까스", "051-508-9239"));
        mClusterManager.addItem(new Info(new LatLng(35.2397422, 129.0934993), "천복반점", "051-515-5255"));
        mClusterManager.addItem(new Info(new LatLng(35.219652, 129.090910), "청룡각", "051-582-3536"));
        mClusterManager.addItem(new Info(new LatLng(35.2197724, 129.0946387), "친구야친구야분식", "051-532-6663"));
        mClusterManager.addItem(new Info(new LatLng(35.214399, 129.1040842), "코코스 베이커리", "051-529-6989"));
        mClusterManager.addItem(new Info(new LatLng(35.2513797, 129.0564081), "큰집", "051-518-3326"));
        mClusterManager.addItem(new Info(new LatLng(35.2180672, 129.0950408), "파리바게뜨 부곡뉴그린점", "051-518-0082"));
        mClusterManager.addItem(new Info(new LatLng(35.2403742, 129.0914245), "한솥도시락", "051-517-1800"));
        mClusterManager.addItem(new Info(new LatLng(35.2322339, 129.0846715), "한솥도시락", "051-583-9253"));
        mClusterManager.addItem(new Info(new LatLng(35.2306321, 129.0876946), "한스델리", "051-515-2969"));
        mClusterManager.addItem(new Info(new LatLng(35.2695164, 129.0919431), "행운반점", "051-512-6878"));
        mClusterManager.addItem(new Info(new LatLng(35.2377978, 129.0849724), "호호돼지국밥", "051-516-5222"));
        mClusterManager.addItem(new Info(new LatLng(35.2159349, 129.1025025), "홍해루", "051-527-4942"));
        mClusterManager.addItem(new Info(new LatLng(35.2256106, 129.0907445), "황태자베이커리", "051-581-4588"));
        mClusterManager.addItem(new Info(new LatLng(35.2525706, 129.0927263), "CU구서럭키점", "051-581-7949"));
        mClusterManager.addItem(new Info(new LatLng(35.2514223, 129.0880222), "CU구서롯데점", "051-513-8289"));
        mClusterManager.addItem(new Info(new LatLng(35.2442765, 129.0913987), "CU구서벽산점", "051-513-8414"));
        mClusterManager.addItem(new Info(new LatLng(35.2482933, 129.0910669), "CU구서센타점", "051-581-5075"));
        mClusterManager.addItem(new Info(new LatLng(35.2451859, 129.0875831), "CU구서예가점", "051-517-5955"));
        mClusterManager.addItem(new Info(new LatLng(35.2447138, 129.0928604), "CU구서유림점", "051-518-5714"));
        mClusterManager.addItem(new Info(new LatLng(35.2694934, 129.0900794), "CU남산가람점", "051-517-3715"));
        mClusterManager.addItem(new Info(new LatLng(35.2683408, 129.0882407), "CU남산네오점", "051-517-7498"));
        mClusterManager.addItem(new Info(new LatLng(35.2652392, 129.0872419), "CU남산무지개점", "051-516-4068"));
        mClusterManager.addItem(new Info(new LatLng(35.2654055, 129.0924986), "CU남산지하철역점", "051-514-7742"));
        mClusterManager.addItem(new Info(new LatLng(35.2680621, 129.091666), "CU남산한마음점", "051-517-7982"));
        mClusterManager.addItem(new Info(new LatLng(35.2847494, 129.0953841), "CU노포지하철역점", "051-861-5233"));
        mClusterManager.addItem(new Info(new LatLng(35.2587738, 129.0909348), "CU두실역점", "051-582-4322"));
        mClusterManager.addItem(new Info(new LatLng(35.2729158, 129.0933776), "CU범어사역점", "051-508-6038"));
        mClusterManager.addItem(new Info(new LatLng(35.273256, 129.092672), "CU범어사지하철역점", ""));
        mClusterManager.addItem(new Info(new LatLng(35.2392422, 129.0896664), "CU부곡쌍용점", "051-582-9623"));
        mClusterManager.addItem(new Info(new LatLng(35.237678, 129.0901524), "CU부곡점", "051-512-0117"));
        mClusterManager.addItem(new Info(new LatLng(35.2311449, 129.0862299), "CU부산대정문점", "051-513-3117"));
        mClusterManager.addItem(new Info(new LatLng(35.2326161, 129.0892222), "CU부산대학교앞역점", "051-518-3777"));
        mClusterManager.addItem(new Info(new LatLng(35.2290281, 129.0898839), "CU부산대학역점", "051-583-9656"));
        mClusterManager.addItem(new Info(new LatLng(35.2331959, 129.0854882), "CU부산대행운점", "051-512-9467"));
        mClusterManager.addItem(new Info(new LatLng(35.2304482, 129.1237503), "CU부산회동점", "051-522-1524"));
        mClusterManager.addItem(new Info(new LatLng(35.2143197, 129.1077307), "CU서동럭키점 ", "051-643-0607"));
        mClusterManager.addItem(new Info(new LatLng(35.2179268, 129.103808), "CU서동점", "051-532-6536"));
        mClusterManager.addItem(new Info(new LatLng(35.2169825, 129.109298), "CU서동태광점", "051-525-5390"));
        mClusterManager.addItem(new Info(new LatLng(35.214399, 129.1040842), "CU세웅병원점", "070-7779-6135"));
        mClusterManager.addItem(new Info(new LatLng(35.2233899, 129.0852774), "CU장전그린코아점", "010-2783-1529"));
        mClusterManager.addItem(new Info(new LatLng(35.2380154, 129.0860073), "CU장전삼광점", "051-582-3772"));
        mClusterManager.addItem(new Info(new LatLng(35.235122, 129.0888174), "CU장전원룸점", "051-904-4449"));
        mClusterManager.addItem(new Info(new LatLng(35.2271285, 129.0827984), "CU장전한신점", "051-515-1566"));
        mClusterManager.addItem(new Info(new LatLng(35.2474474, 129.0966901), "GS25구서골드점", "010-4880-6042"));
        mClusterManager.addItem(new Info(new LatLng(35.2452693, 129.0926381), "GS25구서삼백점", "051-516-8245"));
        mClusterManager.addItem(new Info(new LatLng(35.2464985, 129.0917834), "GS25구서역점", "051-583-5022"));
        mClusterManager.addItem(new Info(new LatLng(35.2427879, 129.0895384), "GS25구서참빛점", "051-516-5421"));
        mClusterManager.addItem(new Info(new LatLng(35.2145003, 129.1173095), "GS25금사대우점", "051-523-0305"));
        mClusterManager.addItem(new Info(new LatLng(35.2212934, 129.1127684), "GS25금사사랑점", "051-532-1767"));
        mClusterManager.addItem(new Info(new LatLng(35.2430821, 129.0941021), "GS25금정구청점", "051-513-7782"));
        mClusterManager.addItem(new Info(new LatLng(35.2381156, 129.0872962), "GS25금정점", "051-514-0635"));
        mClusterManager.addItem(new Info(new LatLng(35.2607325, 129.0896335), "GS25남산삼한점", "051-514-0165"));
        mClusterManager.addItem(new Info(new LatLng(35.2666279, 129.0930542), "GS25남산역점", "051-517-0835"));
        mClusterManager.addItem(new Info(new LatLng(35.2692941, 129.0854437), "GS25남산점", "051-517-3772"));
        mClusterManager.addItem(new Info(new LatLng(35.263804, 129.088553), "GS25남산중앙점", "051-583-7188"));
        mClusterManager.addItem(new Info(new LatLng(35.2671104, 129.0889474), "GS25남산참빛점", "051-513-6831"));
        mClusterManager.addItem(new Info(new LatLng(35.2847494, 129.0953841), "GS25노포1호점", "051-508-0293"));
        mClusterManager.addItem(new Info(new LatLng(35.2563151, 129.090503), "GS25두실선경점", "051-517-3889"));
        mClusterManager.addItem(new Info(new LatLng(35.2549424, 129.0910977), "GS25두실점", "051-517-5445"));
        mClusterManager.addItem(new Info(new LatLng(35.2727249, 129.0929651), "GS25범어사역점", "051-508-2394"));
        mClusterManager.addItem(new Info(new LatLng(35.219328, 129.094972), "GS25부곡그린점", "051-583-8211"));
        mClusterManager.addItem(new Info(new LatLng(35.2391595, 129.0887227), "GS25부곡쌍용점", "051-517-2987"));
        mClusterManager.addItem(new Info(new LatLng(35.2287356, 129.0916333), "GS25부곡점", "051-582-1077"));
        mClusterManager.addItem(new Info(new LatLng(35.2427141, 129.0915272), "GS25부곡중앙점", "051-583-5421"));
        mClusterManager.addItem(new Info(new LatLng(35.228804, 129.0841291), "GS25부대남문점", "051-517-5744"));
        mClusterManager.addItem(new Info(new LatLng(35.2377603, 129.0831913), "GS25부대북문점", "051-515-1963"));
        mClusterManager.addItem(new Info(new LatLng(35.230692, 129.0874773), "GS25부대샛별점", "051-515-0993"));
        mClusterManager.addItem(new Info(new LatLng(35.2313428, 129.0845726), "GS25부대정문점", "051-512-7333"));
        mClusterManager.addItem(new Info(new LatLng(35.2358814, 129.0835281), "GS25부대제일점", "051-516-3103"));
        mClusterManager.addItem(new Info(new LatLng(35.2295171, 129.0891292), "GS25부대테라스파크점", "051-581-6689"));
        mClusterManager.addItem(new Info(new LatLng(35.2394361, 129.0890831), "GS25부산고속도점", "051-582-2102"));
        mClusterManager.addItem(new Info(new LatLng(35.2215514, 129.1138231), "GS25부산금사점", "051-525-1767"));
        mClusterManager.addItem(new Info(new LatLng(35.236672, 129.077221), "GS25부산대학사점", "051-515-4804"));
        mClusterManager.addItem(new Info(new LatLng(35.2754362, 129.0900667), "GS25부산청룡점", "051-508-6925"));
        mClusterManager.addItem(new Info(new LatLng(35.2444362, 129.0990265), "GS25부산카톨릭대점", "051-582-0354"));
        mClusterManager.addItem(new Info(new LatLng(35.2210481, 129.1139479), "GS25부산회동점", "051-523-1767"));
        mClusterManager.addItem(new Info(new LatLng(35.2140999, 129.1076256), "GS25서동금사점", "051-521-7600"));
        mClusterManager.addItem(new Info(new LatLng(35.2166895, 129.0984995), "GS25서동로점", "051-525-2502"));
        mClusterManager.addItem(new Info(new LatLng(35.2145659, 129.1044687), "GS25서동점", "051-524-0365"));
        mClusterManager.addItem(new Info(new LatLng(35.214353, 129.105864), "GS25서동제일점", "051-529-9267"));
        mClusterManager.addItem(new Info(new LatLng(35.2151796, 129.1072448), "GS25서동중앙점", "051-528-3881"));
        mClusterManager.addItem(new Info(new LatLng(35.2654402, 129.0855301), "GS25외대정문점", "051-513-2845"));
        mClusterManager.addItem(new Info(new LatLng(35.2354593, 129.0868905), "GS25장전금강점", "051-517-2363"));
        mClusterManager.addItem(new Info(new LatLng(35.2397511, 129.0868467), "GS25장전대동점", "051-517-2401"));
        mClusterManager.addItem(new Info(new LatLng(35.2244286, 129.085043), "GS25장전사랑점", "051-581-8110"));
        mClusterManager.addItem(new Info(new LatLng(35.2332706, 129.0843892), "GS25장전효원점", "051-517-8074"));
        mClusterManager.addItem(new Info(new LatLng(35.275600, 129.086508), "GS25청룡점", "051-508-2422"));

        mGoogleMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }
}
