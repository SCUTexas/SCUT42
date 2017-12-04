package model;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/11/26.
 */

public class Card {
    private int bigger;	    //大点数
    private int smaller;     //小点数
    public Bitmap cardImage; //对应牌的图片
    public Card(int bigger,int smaller)
    {
        this.bigger=bigger;
        this.smaller=smaller;
        this.cardImage = null;
    }

    public void setCardImage(Bitmap ci)
    {
        cardImage = ci;
    }

    public void setBigger(int bigger) {
        this.bigger = bigger;
    }
    public int getBigger() {
        return bigger;
    }

    public void setSmaller(int smalleer) {
        this.smaller = smalleer;
    }
    public int getSmaller() {
        return smaller;
    }

}
