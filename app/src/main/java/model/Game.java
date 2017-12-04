package model;

/**
 * Created by Administrator on 2017/11/26.
 */
import android.content.Context;

import java.util.ArrayList;


public class Game {
    public enum GameState{Play,callline,settrump};
    public GameState gameState;
    public enum CurrentPlayerType{Robot,Human};
    public CurrentPlayerType currentPlayerType=CurrentPlayerType.Human;
    private int winline;//胜利的分数
    private int host;	//庄家是几号玩家
    private int trump;	//主牌点数
    private ArrayList<Player> players;	//玩家数组
    private int numberOfRound;	//局数
    private Deck deck;	//牌堆
    private int[] lines = new int[4];//记录每个玩家叫的分数,-1表示pass

    public Game(Context context)
    {
        winline=-1;
        host=-1;
        trump=-1;
        players=new ArrayList<>();
        players.add(new Player());	//加入一个玩家,这里到时候要进行修改
        players.add(new Robot());
        players.add(new Robot());
        players.add(new Robot());
        deck=new Deck(context);
    }



    public ArrayList<Card> getPlayerCards(int i)	//得到几号玩家的牌
    {
        if(i>=players.size())
            return null;
        return players.get(i).getHandCards();
    }

    public void setWinline(int winline) {
        this.winline = winline;
    }

    public int getWinline() {
        return winline;
    }

    public void setHost(int host) {
        this.host = host;
    }
    public int getHost() {
        return host;
    }

    //传递一个trump，int值，即用户选择的主牌数字
    //这个值应初始化为-1，因为有可能不是用户决定主牌，但是必须要有一个值
    //如果不是用户自己选择，则由AI根据算法生成一个
    //否则设置主牌为用户选择的数字
    //如果符合规则（0~6）以内，则trump = （0~6）
    //否则主牌值为-1，代表无法选择该数字为主牌
    public void setTrump(int trump) {
        int temp;
        if(this.host!=0){
            int h = this.getHost();
            temp = players.get(h).decideTrump(players.get(h).getHandCards());
        }
        else temp = trump;

        if(trump>=0&&trump<=6)
            this.trump = temp;
    }

    public int getTrump() {
        return trump;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int[] getLines() {
        return lines;
    }

    /***************************************************************/

    public void shuffle() { deck.shuffle(); }

    public void deliver()
    {
        for(int i=0;i<players.size();i++)
        {
            players.get(i).setHandCards(deck.deliver(i*7,i*7+6 ));
        }
    }

    //前端传递一个point，int值，即用户输入的叫分的分值。
    public void addline(int point){
        lines[0]=point;
        for(int i = 0;i<4;i++){
            if(i>0)
                lines[i] = players.get(i).callline(this.getWinline());
            if(lines[i]>this.getWinline()){
                this.setWinline(lines[i]);
                this.setHost(i);
            }
        }
        if(host!=0)
            setTrump(-1);

    }
    public void restate()
    {
        if(host==0)
            currentPlayerType=CurrentPlayerType.Human;
        else
            currentPlayerType=CurrentPlayerType.Robot;

    }
}
