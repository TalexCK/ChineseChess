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
        if ("red".equals(rorb)) {
            com.pj.chess.ChessBoardMain.play=1-com.pj.chess.ChessBoardMain.play;
            JOptionPane.showMessageDialog(null,"Red side start","",JOptionPane.PLAIN_MESSAGE);
            ADialogue(ip, port, com.pj.chess.ChessBoardMain.startFen);
            move(com.pj.chess.ChessBoardMain.startFen,rorb);
            astep();
        } else if ("black".equals(rorb)) {
            String received = ListenerLaunch(mport);
            JOptionPane.showMessageDialog(null,"Black side start","",JOptionPane.PLAIN_MESSAGE);
            move(received,rorb);
            astep();
        } else {
            JOptionPane.showMessageDialog(null,"No Such RorB message","",JOptionPane.PLAIN_MESSAGE);
        }
    }
    public static String received2=null;
    public static void astep() throws IOException, InterruptedException {
        new Thread(){
            public void run(){
                while (moved!=1){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                String seed = seedexport();
                addlog("SeedExport: "+seed);
                try {
                    ADialogue(theip, theport, seed);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    received2 = ListenerLaunch(themport);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                move(received2, therorb);
                try {
                    astep();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
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
        }
    }


    public static void handshaking(){
        Integer mport = Integer.valueOf(JOptionPane.showInputDialog(null,"请设置您的监听端口:\n","联网",JOptionPane.PLAIN_MESSAGE));
        String ip = JOptionPane.showInputDialog(null,"请输入对方ip:\n","联网",JOptionPane.PLAIN_MESSAGE);
        if(ip.equals("lh")){
            ip = "127.0.0.1";
        }
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
