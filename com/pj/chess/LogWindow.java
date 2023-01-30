package com.pj.chess;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LogWindow {
    public LogWindow() {
        try {
            jtextArea.append("[System] AI��־����\n");
            addlog("��־���Ϳ���");
            addlogerror("�������",0);
        }
        catch (Exception ev) {

        }
    }
    public static void addlog(String info){
        try {
            jtextArea.append("[INFO] "+info+"\n");
        }
        catch (Exception ev) {

        }
    }
    public static void addlogerror(String error,Integer level){
        try {
            jtextArea.append("[ERROR] "+level+"�� "+error+"\n");
            if(level > 0){
                JOptionPane.showMessageDialog(null, "����","�������"+level+"������",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ev) {

        }
    }
    public static JTextArea jtextArea= new JTextArea(1000,300);
    public static void logwindow() {
        JFrame jframe=new JFrame("��־��¼");
        jframe.setBounds(100,100,600,1200);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JTextArea jtextArea=new JTextArea();
        jtextArea.setEditable(false);
        DefaultCaret caret=(DefaultCaret)jtextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jtextArea.setLineWrap(true);
        jtextArea.setWrapStyleWord(true);
        jframe.add(jtextArea);
        jframe.setVisible(true);

        new LogWindow();
    }

}
