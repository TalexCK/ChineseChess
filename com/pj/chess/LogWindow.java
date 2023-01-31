package com.pj.chess;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class LogWindow {
    public LogWindow() {
        try {
            jtextArea.append("[System] AI日志开启\n");
            addlog("日志传送开启");
            addlogerror("报错测试",0);
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
            jtextArea.append("[ERROR] "+level+"级 "+error+"\n");
            if(level > 0){
                JOptionPane.showMessageDialog(null, "警报","程序出现"+level+"级错误",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ev) {

        }
    }
    public static JTextArea jtextArea= new JTextArea(1000,300);
    public static void logwindow() {
        JFrame jframe=new JFrame("日志记录");
        jframe.setBounds(100,100,600,1200);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
