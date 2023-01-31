package com.pj.chess;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class RecordWindow {
    public RecordWindow() {
        try {
            jtextArea2.append("[System] AI运算开启\n");
            addrlog("运算日志开启");
        }
        catch (Exception ev) {

        }
    }
    public static void addrlog(String info){
        try {
            jtextArea2.append("[INFO] "+info+"\n");
        }
        catch (Exception ev) {

        }
    }
    public static JTextArea jtextArea2= new JTextArea(1000,300);
    public static void recordwindow() {
        JFrame jframe=new JFrame("AI运算");
        jframe.setBounds(780,1070,1000,375);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jtextArea2.setEditable(false);
        DefaultCaret caret=(DefaultCaret)jtextArea2.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jtextArea2.setLineWrap(true);
        jtextArea2.setWrapStyleWord(true);
        jframe.add(jtextArea2);
        jframe.setVisible(true);

        new RecordWindow();

    }

}
