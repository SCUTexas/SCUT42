package model;

/**
 * Created by Administrator on 2017/11/26.
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.texas421.R;

import model.Card;

import java.util.*;
public class Deck {
    ArrayList<Card>cards;
    public Bitmap cardImage;
    public Deck(Context context)
    {
        cards=new ArrayList<>();
        Resources resource =context.getResources();
        int num=0;
        for(int i=0;i<=6;i++)
        {
            for(int j=0;j<=i;j++)
            {
                Card tmp=new Card(i,j);
                cardImage = BitmapFactory.decodeResource(resource, R.drawable.ya+num);
                tmp.setCardImage(cardImage);
                cards.add(tmp);
                num++;
            }
        }
    }
    public void shuffle()	//洗牌
    {
        Collections.shuffle(cards);
    }
    public ArrayList<Card> deliver(int start,int end)	//发牌,得到start到end的牌，两边都包括
    {
        ArrayList<Card>tmp=new ArrayList<>();
        for(int i=start;i<=end;i++)
        {
            tmp.add(cards.get(i));
        }
        return tmp;
    }
}
