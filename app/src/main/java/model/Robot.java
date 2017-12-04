package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/29.
 */

public class Robot extends Player {

    private ArrayList<Card> handCards;
    private int[] pointsHave = new int[7];
/*
    public void setHandCards(ArrayList<Card> handCards) {
        this.handCards = handCards;
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }*/

    //AI叫分的算法：随机30-42；
    public int callline(int win_line){
        Random random = new Random();
        int temp = random.nextInt(42)%(42-30+1) + 30;

        if(temp>win_line) {
            return  temp;
        }
        else return -1;
    }

    //AI选择主牌的算法：哪个数字重复多就哪个当主牌。
    public int decideTrump(ArrayList<Card> hand_Cards){

        for(int j = 0;j<hand_Cards.size();j++){

            if(hand_Cards.get(j).getBigger()==j){
                this.pointsHave[j]++;
            }
            if(hand_Cards.get(j).getSmaller()==j){
                this.pointsHave[j]++;
            }
        }

        int temp = -1;
        for(int i = 1;i<7;i++){
            if(this.pointsHave[i]>this.pointsHave[i-1])
                temp = i;
            else temp = this.pointsHave[i];
        }

        return temp;
    }
}
