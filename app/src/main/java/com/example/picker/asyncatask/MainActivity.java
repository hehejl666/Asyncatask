package com.example.picker.asyncatask;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.id_fourth_viewpager)
    ViewPager mviewpager;
    @Bind(R.id.id_fourth_imggroup)
    LinearLayout mimagegroup;
    @Bind(R.id.id_timer)
    TextView txttimer;
    @Bind(R.id.id_main_swiper)
    SwipeRefreshLayout mswiperRefreashlayout;



    //private SwipeRefreshLayout mswiperRefreashlayout;
    //private ViewPager mviewpager;
   // private LinearLayout mimagegroup;
    private HomeJson mhomeJson;
    private String url = "http://api.heitongxue.cn/index.php?mod=mobile";
    private Handler imghandler;
    private Runnable imgrun;
    private int FLAG_PAGENOW;
    private int FLAG_RADIONOW;
    private boolean FLAG_THIEFIRST = true;

    private Handler timehandler;
    private Runnable timeRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }


    private void initEvent() {

        SetViewpager(mhomeJson.getImg());
       // mviewpager.setAdapter(new MyPageAdapter(mhomeJson.getImg(),MainActivity.this));

        mswiperRefreashlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Myasynctask asy = new Myasynctask();
                asy.execute(url);
            }
        });

        mviewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        imghandler.removeCallbacks(imgrun);

                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP: {
                        imghandler.postDelayed(imgrun, 3000);
                        //Log.e("touch", "手指离开到视图开始计时器");
                        break;
                    }

                }
                return false;
            }

        });
        mviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                FLAG_PAGENOW = position;
                FLAG_RADIONOW = FLAG_PAGENOW % 5;

                for (int i = 0; i < mimagegroup.getChildCount(); i++) {
                    ImageView img = (ImageView) mimagegroup.getChildAt(i);
                    img.setImageResource(R.drawable.dian_lin);
                }
                ImageView img = (ImageView) mimagegroup.getChildAt(FLAG_RADIONOW);
                img.setImageResource(R.drawable.dian);

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        // mradiogroup.check(FLAG_PAGENOW%5+1);
        imgrun = new Runnable() {
            @Override
            public void run() {
                FLAG_PAGENOW = mviewpager.getCurrentItem() + 1;
                //FLAG_PAGENOW=200;
                Log.e("F", FLAG_PAGENOW + "");
                mviewpager.setCurrentItem(FLAG_PAGENOW);
                imghandler.postDelayed(imgrun, 3000);
            }
        };
        timeRun = new Runnable() {
            @Override
            public void run() {
                txttimer.setText(GettimeNow());
                timehandler.postDelayed(timeRun, 500);
            }
        };
        imghandler.post(imgrun);
        timehandler.post(timeRun);


    }

    private void initView() {

        mswiperRefreashlayout.setProgressViewEndTarget(true, 300);
        //txttimer = (TextView) findViewById(R.id.id_timer);

        HttpThread httpthread=new HttpThread(url,"a=index");
        httpthread.start();
        try {
            new Thread().sleep(2000);
            mhomeJson=new HomeJson(httpthread.getRequst());
           // SetViewpager(homeJson.getImg());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        imghandler = new Handler();
        timehandler = new Handler();
    }

    private void SetViewpager(List<String> iamgeurl) {
        List<String> mlist = iamgeurl;
        List<View> viewlist = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            viewlist.add(getImage(mlist.get(i)));
        }
        MyPageAdapter page = new MyPageAdapter(viewlist, MainActivity.this);
        mviewpager.setAdapter(page);

        mviewpager.setCurrentItem(Integer.MAX_VALUE/2);

        //设置点点
        mimagegroup.removeAllViews();
        for (int i = 0; i < mhomeJson.getNum(); i++) {
            //RadioButton rad=new RadioButton(getActivity());
            ImageView img = new ImageView(MainActivity.this);
            img.setImageResource(R.drawable.dian_lin);
            if (i == mviewpager.getCurrentItem() % 5) {
                img.setImageResource(R.drawable.dian);
            }
            img.setPadding(5, 0, 0, 0);
            mimagegroup.addView(img);
        }
        //imghandler.postDelayed(imgrun,3000);
    }

    private void InitViewpage() {
        FLAG_PAGENOW = Integer.MAX_VALUE / 2;
        mviewpager.setCurrentItem(FLAG_PAGENOW);
        imghandler.postDelayed(imgrun, 300);
    }

    //获取网络图片
    private SimpleDraweeView getImage(String url) {
        Uri uri = Uri.parse(url);
        SimpleDraweeView draweeView = new SimpleDraweeView(MainActivity.this);
        draweeView.setImageURI(uri);
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        return draweeView;

    }


    private class Myasynctask extends AsyncTask<String, HomeJson, HomeJson> {

        HttpThread htthread;

        @Override
        protected void onPreExecute() {
            imghandler.removeCallbacks(imgrun);
            super.onPreExecute();
        }

        @Override
        protected HomeJson doInBackground(String... strings) {
            String url = strings[0];
            htthread = new HttpThread(url, "a=index");
            htthread.start();
            Log.e("asy", "url=" + url);
            try {
                new Thread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("asy", "request" + htthread.getRequst());
            HomeJson hj = new HomeJson(htthread.getRequst());
            Log.e("asy", "hj解析成功");
            publishProgress(hj);
            return hj;

        }

        @Override
        protected void onProgressUpdate(HomeJson... homeJsons) {



            super.onProgressUpdate(homeJsons);
        }

        @Override
        protected void onPostExecute(HomeJson homeJson) {
            super.onPostExecute(homeJson);


            mhomeJson = homeJson;
            mswiperRefreashlayout.setRefreshing(false);
            SetViewpager(mhomeJson.getImg());
            FLAG_THIEFIRST = true;
            imghandler.post(imgrun);
//            Newasy nes=new Newasy();
//            nes.execute();

            //
        }
    }

    private String GettimeNow() {
        String time = "";
        Calendar cl = Calendar.getInstance();
        int YEAR = cl.get(Calendar.YEAR);
        int MOUTH = cl.get(Calendar.MONTH);
        int DAY = cl.get(Calendar.DAY_OF_MONTH);
        int HOUR = cl.get(Calendar.HOUR_OF_DAY);
        int MINUTE = cl.get(Calendar.MINUTE);
        time = YEAR + "-" + MOUTH + "-" + DAY + " " + HOUR + ":" + MINUTE;
        return time;
    }
}
