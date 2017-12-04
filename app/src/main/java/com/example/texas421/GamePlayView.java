package com.example.texas421;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import model.Game;
import model.SetScale2;


/**
 * Created by Administrator on 2017/11/24.
 */

public class GamePlayView extends View implements Runnable{
    private Paint kpaint = null;
    private Context GamePlayViewContext;
    public Game game;    // 声明数据层的game

    public Bitmap[] cardPut0 = new Bitmap[10];
    public Bitmap[] cardPut1 = new Bitmap[10];
    public Bitmap[] cardPut2 = new Bitmap[10];
    public Bitmap[] cardPut3 = new Bitmap[10];

    public Bitmap[] cardBackRight =new Bitmap[10];
    public Bitmap[] cardBackTop =new Bitmap[10];
    public Bitmap[] cardBackLeft =new Bitmap[10];

    public Bitmap btnocallnew=null;
    public Bitmap btcallnew=null;
    public Bitmap btdecreasenew=null;
    public Bitmap btincreasenew=null;
    public Bitmap bttrumpnew=null;

    // 用于显示“叫分”“不叫”的图片
    public Bitmap btnocall=null;
    public Bitmap btcall=null;

    // 用于显示“+”“-”的图片
    public Bitmap btdecrease=null;
    public Bitmap btincrease=null;

    //用于显示“确认主牌”的图片
    public Bitmap bttrump=null;

    final SetScale2 PlayView1 = new SetScale2();

    // 响应事件时的坐标位置
    public int desx, desy = 0;

    //表示游戏进行时页面的不同状态 0：游戏界面 1：菜单界面 2：停止界面 3：停止菜单界面 4：个人信息界面5:提示界面6：结束界面
    int playViewState = 0;

    int thread=0;
    //记录用户叫分时的分数
    private int usercallline=30;

    //记录用户选择的主牌点数
    private int usertrump=0;
    //判断user是否出牌
    boolean take = false;


    public GamePlayView(Context context) {
        super(context);
        GamePlayViewContext = context;
        game=new Game(context);
        game.gameState=Game.GameState.callline;//第一次进入游戏默认为叫分阶段
        game.shuffle();
        game.deliver();

        Resources resource = getResources();

        //背面牌图片的初始化
        for (int j = 0; j < 7; j++)
        {
            cardBackRight[j] = BitmapFactory.decodeResource(resource, R.drawable.rightcard);
            cardPut1[j] = PlayView1.resizeImage(cardBackRight[j], 70*com.example.texas421.MainActivity.screenWidth/800, 50*com.example.texas421.MainActivity.screenHeight/480 );

        }
        for (int j = 0; j < 7; j++)
        {

            cardBackTop[j] = BitmapFactory.decodeResource(resource, R.drawable.topcard);
            cardPut2[j] = PlayView1.resizeImage(cardBackTop[j], 50*com.example.texas421.MainActivity.screenWidth/800, 70*com.example.texas421.MainActivity.screenHeight/480 );

        }
        for (int j = 0; j < 7; j++)
        {

            cardBackLeft[j] = BitmapFactory.decodeResource(resource, R.drawable.rightcard);
            cardPut3[j] = PlayView1.resizeImage(cardBackLeft[j], 70*com.example.texas421.MainActivity.screenWidth/800, 50*com.example.texas421.MainActivity.screenHeight/480 );


        }

        //按钮的初始化
        btnocall=BitmapFactory.decodeResource(resource, R.drawable.bt_nocall);
        btnocallnew=PlayView1.resizeImage(btnocall, 70*com.example.texas421.MainActivity.screenWidth/800, 40*com.example.texas421.MainActivity.screenHeight/480 );

        btcall=BitmapFactory.decodeResource(resource, R.drawable.bt_call);
        btcallnew=PlayView1.resizeImage(btcall, 70*com.example.texas421.MainActivity.screenWidth/800, 40*com.example.texas421.MainActivity.screenHeight/480 );

        btdecrease=BitmapFactory.decodeResource(resource, R.drawable.bt_decrease);
        btdecreasenew=PlayView1.resizeImage(btdecrease, 20*com.example.texas421.MainActivity.screenWidth/800, 10*com.example.texas421.MainActivity.screenHeight/480 );

        btincrease=BitmapFactory.decodeResource(resource, R.drawable.bt_increase);
        btincreasenew=PlayView1.resizeImage(btincrease, 20*com.example.texas421.MainActivity.screenWidth/800, 20*com.example.texas421.MainActivity.screenHeight/480 );

        bttrump=BitmapFactory.decodeResource(resource, R.drawable.bt_trump);
        bttrumpnew=PlayView1.resizeImage(bttrump, 70*com.example.texas421.MainActivity.screenWidth/800, 40*com.example.texas421.MainActivity.screenHeight/480 );

        kpaint = new Paint();
        kpaint.setTextSize(30*com.example.texas421.MainActivity.screenHeight/480f);
        kpaint.setColor(Color.WHITE);

        new Thread(this).start();

    }

    //每次对所持有的牌和所处的牌的初始化
    void initCardsView(Canvas canvas)
    {
        //playerPos=0
        if (!game.getPlayerCards(0).isEmpty() )
        {

            for(int i=0; i<game.getPlayerCards(0).size(); i++)
            {


                cardPut0[i]=PlayView1.resizeImage(game.getPlayerCards(0).get(i).cardImage, 50*com.example.texas421.MainActivity.screenWidth/800, 90*com.example.texas421.MainActivity.screenHeight/480 );
                canvas.drawBitmap(cardPut0[i], (130+i*55)*com.example.texas421.MainActivity.screenWidth/800, 370*com.example.texas421.MainActivity.screenHeight/480,null);
            }
        }
        //playerPos=1
        for (int i = 0; i < game.getPlayerCards(1).size(); i++)
        {
            canvas.drawBitmap(cardPut1[i], 26*com.example.texas421.MainActivity.screenWidth/800,(135 + i * 20)*com.example.texas421.MainActivity.screenHeight/480, null);
        }

        //playerPos=2
        for (int i = 0; i < game.getPlayerCards(2).size(); i++)
        {

            canvas.drawBitmap(cardPut2[i], (230 + i * 20)*com.example.texas421.MainActivity.screenWidth/800, 28*com.example.texas421.MainActivity.screenHeight/480, null);

        }

        //playerPos=3
        for (int i = 0; i < game.getPlayerCards(3).size(); i++)
        {
            canvas.drawBitmap(cardPut3[i], 550*com.example.texas421.MainActivity.screenWidth/800, (135 + i * 20)*com.example.texas421.MainActivity.screenHeight/480, null);

        }
    }

    //界面显示分数的初始化
    void initScoreView(Canvas canvas)
    {
        canvas.drawText("敌方0/100", 30 * com.example.texas421.MainActivity.screenWidth / 800, 50 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
        canvas.drawText("我方0/100", 30 * com.example.texas421.MainActivity.screenWidth / 800, 90 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
        if(game.gameState!= Game.GameState.callline)
        {
            canvas.drawText("0 / "+(42-game.getWinline()), 690 * com.example.texas421.MainActivity.screenWidth / 800, 40 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            canvas.drawText("0 / "+game.getWinline(), 690 * com.example.texas421.MainActivity.screenWidth / 800, 460 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
        }
    }
    public void onDraw(Canvas canvas)
    {
        initCardsView(canvas);
        initScoreView(canvas);
        //叫分阶段并且当且玩家是人
        if(game.currentPlayerType==Game.CurrentPlayerType.Human&&game.gameState==Game.GameState.callline) {
            canvas.drawBitmap(btnocallnew, 370 * com.example.texas421.MainActivity.screenWidth / 800, 270 * com.example.texas421.MainActivity.screenHeight / 480, null);
            canvas.drawBitmap(btcallnew, 220 * com.example.texas421.MainActivity.screenWidth / 800, 270 * com.example.texas421.MainActivity.screenHeight / 480, null);

            canvas.drawBitmap(btincreasenew, 370 * com.example.texas421.MainActivity.screenWidth / 800, 330 * com.example.texas421.MainActivity.screenHeight / 480, null);
            canvas.drawBitmap(btdecreasenew, 265 * com.example.texas421.MainActivity.screenWidth / 800, 335 * com.example.texas421.MainActivity.screenHeight / 480, null);

            canvas.drawText("" + usercallline, 315 * com.example.texas421.MainActivity.screenWidth / 800, 350 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
        }
        //叫分阶段并且当前玩家是人机
        if(game.currentPlayerType==Game.CurrentPlayerType.Robot&&game.gameState==Game.GameState.callline) {

            if(game.getLines()[0]!=-1)
                canvas.drawText("" + usercallline, 315 * com.example.texas421.MainActivity.screenWidth / 800, 330 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            else
                canvas.drawBitmap(btnocallnew, 300 * com.example.texas421.MainActivity.screenWidth / 800, 310 * com.example.texas421.MainActivity.screenHeight / 480, null);

            if(game.getLines()[1]!=-1)
                canvas.drawText("" + game.getLines()[1], 500 * com.example.texas421.MainActivity.screenWidth / 800, 240 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            else
                canvas.drawBitmap(btnocallnew, 470 * com.example.texas421.MainActivity.screenWidth / 800, 210 * com.example.texas421.MainActivity.screenHeight / 480, null);

            if(game.getLines()[2]!=-1)
                canvas.drawText("" + game.getLines()[2], 315 * com.example.texas421.MainActivity.screenWidth / 800, 130 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            else
                canvas.drawBitmap(btnocallnew, 300 * com.example.texas421.MainActivity.screenWidth / 800, 110 * com.example.texas421.MainActivity.screenHeight / 480, null);

            if(game.getLines()[3]!=-1)
                canvas.drawText("" + game.getLines()[3], 140 * com.example.texas421.MainActivity.screenWidth / 800, 240 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            else
                canvas.drawBitmap(btnocallnew, 130 * com.example.texas421.MainActivity.screenWidth / 800, 210 * com.example.texas421.MainActivity.screenHeight / 480, null);
            //设置延时，跳转到确定主牌阶段
            if(thread==0) {
                thread++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        game.gameState = Game.GameState.settrump;
                        game.restate();
                    }
                }, 500);
            }



        }

        //确定主牌阶段并且当前玩家是人机
        if(game.currentPlayerType==Game.CurrentPlayerType.Robot&&game.gameState==Game.GameState.settrump) {
            if(game.getHost()==1)
            {
                canvas.drawText("" + game.getHost(), 500 * com.example.texas421.MainActivity.screenWidth / 800, 240 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            }
            if(game.getHost()==2)
            {
                canvas.drawText("" + game.getHost(), 315 * com.example.texas421.MainActivity.screenWidth / 800, 130 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            }
            if(game.getHost()==3)
            {
                canvas.drawText("" + game.getHost(), 140 * com.example.texas421.MainActivity.screenWidth / 800, 240 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
            }

        }

        if(game.gameState==Game.GameState.settrump && game.currentPlayerType==Game.CurrentPlayerType.Human)
        {
            canvas.drawBitmap(bttrumpnew, 295 * com.example.texas421.MainActivity.screenWidth / 800, 270 * com.example.texas421.MainActivity.screenHeight / 480, null);
            canvas.drawBitmap(btincreasenew, 370 * com.example.texas421.MainActivity.screenWidth / 800, 330 * com.example.texas421.MainActivity.screenHeight / 480, null);
            canvas.drawBitmap(btdecreasenew, 265 * com.example.texas421.MainActivity.screenWidth / 800, 335 * com.example.texas421.MainActivity.screenHeight / 480, null);

            canvas.drawText("" + usertrump, 315 * com.example.texas421.MainActivity.screenWidth / 800, 350 * com.example.texas421.MainActivity.screenHeight / 480, kpaint);
        }
    }

    public boolean downCallLineViewListener()
    {
        //判断点击“+”的区域
        if (usercallline<42&&desx > 360*com.example.texas421.MainActivity.screenWidth/800 && desx < 395*com.example.texas421.MainActivity.screenWidth/800 && desy > 320*com.example.texas421.MainActivity.screenHeight/480 && desy < 355*com.example.texas421.MainActivity.screenHeight/480 && game.gameState==Game.GameState.callline)
        {

            usercallline++;
            return true;
        }

        //判断点击“-”的区域
        if (usercallline>30&&desx > 260*com.example.texas421.MainActivity.screenWidth/800 && desx < 295*com.example.texas421.MainActivity.screenWidth/800 && desy > 325*com.example.texas421.MainActivity.screenHeight/480 && desy < 350*com.example.texas421.MainActivity.screenHeight/480 && game.gameState==Game.GameState.callline)
        {
            usercallline--;
            take = true;
            return true;
        }
        //判断点击“不叫”的区域
        if (desx > 370*com.example.texas421.MainActivity.screenWidth/800 && desx < 440*com.example.texas421.MainActivity.screenWidth/800 && desy > 270*com.example.texas421.MainActivity.screenHeight/480 && desy < 310*com.example.texas421.MainActivity.screenHeight/480 &&game.gameState==Game.GameState.callline)
        {
            game.addline(-1);
            game.currentPlayerType=Game.CurrentPlayerType.Robot;
            return true;
        }
        //判断点击“叫分”的区域
        if (desx > 220*com.example.texas421.MainActivity.screenWidth/800 && desx < 290*com.example.texas421.MainActivity.screenWidth/800 && desy > 270*com.example.texas421.MainActivity.screenHeight/480 && desy < 310*com.example.texas421.MainActivity.screenHeight/480 && game.gameState==Game.GameState.callline)
        {

            game.addline(usercallline);
            game.currentPlayerType=Game.CurrentPlayerType.Robot;
            return true;
        }
        return false;
    }

    public boolean downSetTrumpViewListener()
    {
        if (usertrump<6&&desx > 360*com.example.texas421.MainActivity.screenWidth/800 && desx < 395*com.example.texas421.MainActivity.screenWidth/800 && desy > 320*com.example.texas421.MainActivity.screenHeight/480 && desy < 355*com.example.texas421.MainActivity.screenHeight/480 && game.gameState==Game.GameState.settrump)
        {

            usertrump++;
            return true;
        }

        //判断点击“-”的区域
        if (usertrump>0&&desx > 260*com.example.texas421.MainActivity.screenWidth/800 && desx < 295*com.example.texas421.MainActivity.screenWidth/800 && desy > 325*com.example.texas421.MainActivity.screenHeight/480 && desy < 350*com.example.texas421.MainActivity.screenHeight/480 && game.gameState==Game.GameState.settrump)
        {
            usertrump++;
            take = true;
            return true;
        }
        //判断点击确定主牌区域
        if(desx > 295*com.example.texas421.MainActivity.screenWidth/800 && desx < 365*com.example.texas421.MainActivity.screenWidth/800 && desy > 270*com.example.texas421.MainActivity.screenHeight/480 && desy < 310*com.example.texas421.MainActivity.screenHeight/480 && game.gameState==Game.GameState.settrump)
        {
            game.setTrump(usertrump);
            game.gameState=Game.GameState.Play;
            return true;
        }
        return false;

    }
    public boolean onTouchEvent(MotionEvent event)
    {
        int iAction = event.getAction();
        if (iAction == MotionEvent.ACTION_DOWN)
        {
            desx = (int) event.getRawX();
            desy = (int) event.getRawY();

            if(game.gameState==Game.GameState.callline) {
                downCallLineViewListener();
            }
            if(game.gameState==Game.GameState.settrump) {
                downSetTrumpViewListener();
            }
            return true;
        }


        if (iAction == MotionEvent.ACTION_UP)
        {
            return true;
        }

        else
            return false;
    }
    public void run()
    {
        // TODO Auto-generated method stub

        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                Thread.sleep(100);

            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            postInvalidate();

			/*
			 * //游戏循环 while(whetherEnd()) { if(game.currentPlayer.playerType ==
			 * Player.PlayerType.AI) { game.currentPlayer.selectCardsAction();
			 * postInvalidate(); } if(game.currentPlayer.playerType ==
			 * Player.PlayerType.User) { postInvalidate(); } }*/


        }
    }
}
