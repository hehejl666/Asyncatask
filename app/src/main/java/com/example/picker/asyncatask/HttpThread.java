package com.example.picker.asyncatask;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/1/20.
 */
public class HttpThread extends Thread {

    private String url;
    private String postData;
    private String requst;
    int doflag=0;
    public HttpThread(String url, String postData, String imagefile) {
        this.url = url;
        this.postData = postData;

        this.imagefile = imagefile;
        doflag=2;
    }

    private String imagefile;

    public String getRequst() {
        return requst;
    }

    public void setRequst(String requst) {
        this.requst = requst;
    }



    public HttpThread(String url) {
        this.url=url;
        doflag=0;


    }

    public HttpThread(String url, String postData) {
        this.url=url;
        this.postData=postData;
        doflag=1;

    }

    private void PostImage(){
        try {
            URL httpurl=new URL(url);
            HttpURLConnection conn=(HttpURLConnection)httpurl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(5000);
            OutputStream out=conn.getOutputStream();
            String content=postData+"pic="+imagefile;
            out.write(content.getBytes());

            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            StringBuffer sb=new StringBuffer();

            while ((str=reader.readLine())!=null){
                sb.append(str);
            }

            requst=sb.toString();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void doPost() {
        try {
            URL httpurl=new URL(url);
            HttpURLConnection conn=(HttpURLConnection)httpurl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            OutputStream out=conn.getOutputStream();
            String content=postData;
            out.write(content.getBytes());

            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            StringBuffer sb=new StringBuffer();

            while ((str=reader.readLine())!=null){
                sb.append(str);
            }






            requst=sb.toString();
            System.out.print("result"+sb.toString());
            Log.w("result", sb.toString());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doGet() {

        try {
            URL httpurl=new URL(url);
            HttpURLConnection conn=(HttpURLConnection)httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);

            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            StringBuffer sb=new StringBuffer();
            while ((str=reader.readLine())!=null){
                sb.append(str);
            }



            System.out.print("result" + sb.toString());
            Log.w("result",sb.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        if(doflag==0) {
            doGet();
            Log.w("do","get");
        }
        else if(doflag==1){
            doPost();
            Log.w("do", "Post");
        }
        else if(doflag==2){
            PostImage();
            Log.w("do", "PostIamge");
        }
    }
}
