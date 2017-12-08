package model;

/**
 * Created by Administrator on 2017/11/26.
 */
import android.content.Context;

import java.util.ArrayList;


public class Game {
    public enum GameState{Play,callline,settrump,END};
    public GameState gameState;
    public enum CurrentPlayerType{Robot,Human};
    public CurrentPlayerType currentPlayerType=CurrentPlayerType.Human;
    private int winline;//胜利的分数
    private int host;	//庄家是几号玩家
    private int trump;	//主牌点数
    private ArrayList<Player> players;	//玩家数组
    private int numberOfRound;	//轮数
    private Deck deck;	//牌堆
    private int[] lines = new int[4];//记录每个玩家叫的分数,-1表示pass
    private Card[] cardPlayerSelected=new Card[4];  //玩家出的牌，0 1 2 3代表位置
    private int winner=-1;  //赢家的位置
    private int myTeamScore=0;      //我们队所得的分数
    private int eneTeamScore=0;    //别人队所得的分数
    private int handle=-1;      //牌柄


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

    public int getHandle(){return handle;}  //得到牌柄
    public void setHandle(int handle) {this.handle=handle; }

    public void setHandle(int trump,Card c)     //根据主牌点数和牌判断牌柄
    {
        int bigger=c.getBigger();
        int smaller=c.getSmaller();
        if(bigger==trump){     setHandle(bigger);}  //如果大的一端或者小的一端包含主牌，就把他当作牌柄
        else if(smaller==trump){    setHandle(smaller);}
        else {  setHandle(bigger); } //如果不包含牌柄，就把大的一端作为牌柄
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
    public void initial()
    {
        if(numberOfRound==1)
        {
            winner=-1;      //证明是第一轮,还没有赢家
            setHandle(trump);       //如果是第一轮，牌柄就是主牌
            eneTeamScore=0;     //初始化敌人的分
            myTeamScore=0;      //初始化我们的分
        }
        else
            setHandle(-1);
        for(int i=0;i<4;i++)
        {
            cardPlayerSelected[i]=null; //初始化玩家出的牌
        }
    }

    //在人前面的机器人出牌
    public void runStart()
    {
        /*
        if(numberOfRound!=1) {      //如果不是第一轮，就假设没有牌柄
            setHandle(-1);      //-1的handle代表目前没有牌柄
        }
        else
        {
            winner=-1;      //证明是第一轮
            setHandle(trump);       //如果是第一轮，牌柄就是主牌
            eneTeamScore=0;
            myTeamScore=0;
        }*/
        initial();
        int turn=(winner==-1)?host:winner;  //如果winner等于-1，说明第一轮，由庄家先出牌，其它情况下由赢家出牌


        if(turn!=0)
        {
            cardPlayerSelected[turn]=players.get(turn).select(handle,cardPlayerSelected);       //选择一张牌
            setHandle(trump,cardPlayerSelected[turn]);
            turn=(turn+1)%4;
        }
        while(turn!=0)      //其他人继续出牌
        {
            cardPlayerSelected[turn]=players.get(turn).select(handle,cardPlayerSelected);
            turn=(turn+1)%4;
        }

    }

    public void runLeft(Card cardPlayerDrop)
    {
        cardPlayerSelected[0]= cardPlayerDrop;
        if(winner==0||((winner==-1)&&host==0))       //如果上一个赢家是人，或者玩家是庄家，在第一轮
        {
            setHandle(trump,cardPlayerSelected[0]);
        }


        int turn=1;     //玩家出完牌之后就到1号玩家出
        int end=(winner==-1)?host:winner;       //看winner是否为-1,就是是否为第一轮，
        while(turn!=end)        //当还有机器人没出完牌的时候，继续出，直到下一个为第一个出牌的人为止。
        {
            cardPlayerSelected[turn]=players.get(turn).select(handle,cardPlayerSelected);
            turn=(turn+1)%4;
        }
        settlement();
        if(numberOfRound==7)
        {
            gameState=GameState.END;    //如果round==7了，就变为游戏结束状态
            numberOfRound=1;        //顺便把round初始化为1
        }
        else
            ++numberOfRound;

    }

    public void settlement()        //结算
    {
        int max=0;
        Card maxCard=cardPlayerSelected[0];
        for(int i=1;i<4;i++)
        {
            Card tmp=cardPlayerSelected[i];
            maxCard=tmp.comp(handle,trump,maxCard)?tmp:maxCard;
            //判断tmp是否大于maxCard，如果大于，就把tmp作为最大的
            //否则，按照原本不变
            max=i;
        }
        winner=max;
        int score=1;
        for(int i=0;i<4;i++)
        {
            int bigger=cardPlayerSelected[i].getBigger();
            int smaller=cardPlayerSelected[i].getSmaller();
            if((bigger+smaller)%5==0)
                score+=(bigger+smaller);
        }
        if(winner%2==0)
        {
            myTeamScore+=score;
        }
        else
        {
            eneTeamScore+=score;
        }

    }
}
