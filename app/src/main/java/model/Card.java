package model;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/11/26.
 */

public class Card {
    private int bigger;	    //大点数
    private int smaller;     //小点数
    public Bitmap cardImage; //对应牌的图片
    public boolean isSelect;
    public Card(int bigger,int smaller)
    {
        this.bigger=bigger;
        this.smaller=smaller;
        this.cardImage = null;
        isSelect=false;
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
    //比较牌之间点数的大小
    public boolean basic_comp(int b1,int s1 ,int b2, int s2,int handler){

        if( b1 == s1 ){        //如果double牌一定最大
            return true;
        }
        if( b2 == s2) {
            return false;
        }

        if ( b1 == handler) {   //如果没有double牌，比较剩余点数的大小

            if (s1 < s2)
                return false;
            else return true;
        }
        else {
            if (b1 < b2)
                return false;
            else return true;
        }
    }

    public boolean comp(int handler, int trump, Card _card) {
        int b = this.getBigger();
        int s = this.getSmaller();
        int _b = _card.getBigger();
        int _s = _card.getSmaller();

        //两张牌都没有牌柄的情况
        if(b!= handler&&s!=handler&&_b!= handler&&_s!=handler){
            return false;
        }
        if(handler==trump){

            //其中一张牌有牌柄,另一张牌没有牌柄的情况
            if(b==handler || s==handler){
                if(_b!=handler&&_s!=handler)
                    return true;
            }
            if(_b==handler || _s==handler){
                if(b!=handler&&s!=handler)
                    return false;
            }
        }
        else{
            //如果牌柄不等于主牌，则若后续的人出了（含主
            //牌不含牌柄）的牌则大。
            if(b==handler || s==handler){
                if(_b!=handler&&_s!=handler) {
                    if (_b == trump || _s == trump)
                        return false;
                    else return true;
                }
            }
            if(_b==handler || _s==handler){
                if(b!=handler&&s!=handler) {
                    if (b == trump || s == trump)
                        return true;
                    else return false;
                }
            }
        }

        //两张牌都有牌柄的情况
        return basic_comp(b,s,_b,_s,handler);
    }
}
