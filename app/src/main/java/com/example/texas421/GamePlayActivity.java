package com.example.texas421;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;

public class GamePlayActivity extends Activity {

    public GamePlayView  myGamePlayView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        AbsoluteLayout playing=new AbsoluteLayout(this);
        playing.setBackgroundResource(R.drawable.background2);

        setContentView(playing);
        myGamePlayView = new GamePlayView(this);
        playing.addView( myGamePlayView, new AbsoluteLayout.LayoutParams(800*com.example.texas421.MainActivity.screenWidth/800,480*com.example.texas421.MainActivity.screenHeight/480,0,0));

    }
}
