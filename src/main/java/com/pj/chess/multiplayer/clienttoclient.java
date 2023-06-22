package com.pj.chess.multiplayer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

import static com.pj.chess.ChessBoardMain.*;
import static com.pj.chess.LogWindow.addlog;
import static com.pj.chess.Tools.seedexporttime;
import static com.pj.chess.multiplayer.DialogueMoudle.ADialogue;
import static com.pj.chess.multiplayer.Listener.ListenerLaunch;

public class clienttoclient {
    public static Integer multiplayer = 0;
    public static Integer moved = 0;
    public static String theip = null;
    public static Integer theport= 0;
    public static Integer themport= 0;
    public static String therorb= null;
    public static void clienttoclient(String rorb,String ip,Integer port,Integer mport) throws IOException, InterruptedException {
        doubles();
        multiplayer=1;
        therorb=rorb;
        if(rorb.equals("red")){
            com.pj.chess.ChessBoardMain.play=1-com.pj.chess.ChessBoardMain.play;
            JOptionPane.showMessageDialog(null,"您是红方，对局开始","联网",JOptionPane.PLAIN_MESSAGE);
            //lastinfo-->走棋-->message
            String message = null;
            move("c6c5  rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR",rorb);
        }else if(rorb.equals("black")){
            String received = ListenerLaunch(mport);
            JOptionPane.showMessageDialog(null,"您是黑方，对局开始","联网",JOptionPane.PLAIN_MESSAGE);
            move(received,rorb);
        }else{
            JOptionPane.showMessageDialog(null,"错误:\nNo Such RorB message(01).","联网",JOptionPane.PLAIN_MESSAGE);
        }
    }
    public static void astep(String seed) throws IOException, InterruptedException {
        if (multiplayer==1){
                    addlog("SeedExport: "+seedexport());

                    try {
                        ADialogue(theip, theport, seedexport());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String received = null;
                    try {
                        received = ListenerLaunch(themport);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    move(received, therorb);
        }
    }
    public static String red(){
        Random random = new Random();
        Integer randomnumber = random.nextInt(2);
        if(randomnumber==1){
            return "black";
        }else{
            return "red";
        }
    }

    public static void move(String seed, String rorb){
        moved=0;
        seedtoloadatime(seed);
        if (therorb.equals("red")){
            com.pj.chess.ChessBoardMain.play=1-com.pj.chess.ChessBoardMain.play;
        }else {
            com.pj.chess.ChessBoardMain.play=1-com.pj.chess.ChessBoardMain.play;
            System.out.println("play=0");
        }
    }


    public static void handshaking(){
        Integer mport = Integer.valueOf(JOptionPane.showInputDialog(null,"请设置您的监听端口:\n","联网",JOptionPane.PLAIN_MESSAGE));
        String ip = JOptionPane.showInputDialog(null,"请输入对方ip:\n","联网",JOptionPane.PLAIN_MESSAGE);
        Integer port = Integer.valueOf(JOptionPane.showInputDialog(null,"请输入对方的监听端口:\n","联网",JOptionPane.PLAIN_MESSAGE));
        theport = port;
        theip = ip;
        themport = mport;
        try {
            ADialogue(ip,port,"request-Game");
            String received = null;
            received = ListenerLaunch(mport);
            if (received.equals("request-Game")){
                ADialogue(ip,port,"received-Game");
            }else if(received.equals("received-Game")){
                Thread.sleep(100);
                if(red()=="red"){
                    ADialogue(ip,port,"red");
                    clienttoclient("black",ip,port,mport);
                }else{
                    ADialogue(ip,port,"black");
                    clienttoclient("red",ip,port,mport);
                }
            }
        } catch (IOException | InterruptedException ex) {
            String received = null;
            try {
                received = ListenerLaunch(mport);
                if (received.equals("request-Game")){
                    Thread.sleep(100);
                    ADialogue(ip,port,"received-Game");
                    received = ListenerLaunch(mport);
                    clienttoclient(received,ip,port,mport);
                }
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}
