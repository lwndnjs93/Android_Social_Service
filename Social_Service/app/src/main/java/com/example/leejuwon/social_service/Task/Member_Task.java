package com.example.leejuwon.social_service.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by leejuwon on 2017-07-26.
 */

public class Member_Task extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;

    @Override
    protected String doInBackground(String... params) {

        try{
            String str;
            URL url = new URL("http://192.168.0.14:8080/spring_ptj/email_check");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            sendMsg = "01="+params[0]+"&02="+params[1]+"&03="+params[2]+"&04="+params[3]+"&05="+params[4]+"&06="+params[5]+"&07="+params[6]+
                    "&08="+params[7]+"&09="+params[8]+"&10="+params[9]+"&11="+params[10]+"&12="+params[11]+"&13="+params[12]+"&14="+params[13]+
                    "&15="+params[14]+"&16="+params[15]+"&17="+params[16]+"&18="+params[17]+"&19="+params[18]+"&20="+params[19]+"&21="+params[20]+
                    "&22="+params[21]+"&23="+params[22]+"&24="+params[23]+"&25="+params[24]+"&26="+params[25]+"&27="+params[26];
            osw.write(sendMsg);
            osw.flush();
            if (conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null){
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            }else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return receiveMsg;
    }
}
