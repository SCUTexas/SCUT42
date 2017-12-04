package model;

/**
 * Created by Administrator on 2017/11/26.
 */
import model.Card;

import java.util.ArrayList;
public class Player {
    private ArrayList<Card>handCards;
    private int[] pointsHave = new int[7];
    public Player()
    {

    }
    public void setHandCards(ArrayList<Card> handCards) {
        this.handCards = handCards;
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    //class Robot重载
    public int callline(int i){
        return -1;
    }

    public int decideTrump(ArrayList<Card> hand_Cards){
        return 20;
    }
}
