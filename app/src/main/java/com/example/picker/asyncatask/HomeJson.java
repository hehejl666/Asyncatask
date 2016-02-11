package com.example.picker.asyncatask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class HomeJson {
    private String josndata;
    private int Status;
    private List<String> Img=new ArrayList<String>();
    private int Num;

    public HomeJson() {
        parseJson(josndata);
    }

    public HomeJson(String josndata) {
        this.josndata = josndata;
        parseJson(josndata);
    }

    public String getJosndata() {
        return josndata;
    }

    public void setJosndata(String josndata) {
        this.josndata = josndata;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public List<String> getImg() {
        return Img;
    }

    public void setImg(List<String> img) {
        Img = img;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    private void parseJson(String jsond){
        String data=jsond;
        try {
            JSONObject object=new JSONObject(data);
            Status=object.getInt("status");
            Num=object.getInt("num");

            JSONArray array=object.getJSONArray("img");
            for (int i=0;i<array.length();i++){
                JSONObject json=array.getJSONObject(i);
                Img.add(json.getString("url"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



}

