package model;

/**
 * Created by Administrator on 2017/11/26.
 */
import model.Card;

import java.util.ArrayList;
public class Player {
    public ArrayList<Card>handCards;
    public int[] pointsHave = new int[7];
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


    public Card select(int trump, int handle) {
        return null;
    }

    public void removeCard(Card card){
        this.handCards.remove(card);
    }
}
