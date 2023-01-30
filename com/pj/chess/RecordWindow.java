package com.pj.chess;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.Window;

public class RecordWindow {
    public RecordWindow() {
        try {
            jtextArea2.append("[System] AI���㿪��\n");
            addrlog("������־����");
            addrlogerror("�������",0);
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
    public static void addrlogerror(String error,Integer level){
        try {
            jtextArea2.append("[ERROR] "+level+"�� "+error+"\n");
            if(level > 0){
                JOptionPane.showMessageDialog(null, "����","�������"+level+"������",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ev) {

        }
    }
    public static JTextArea jtextArea2= new JTextArea(1000,300);
    public static void recordwindow() {
        JFrame jframe=new JFrame("AI����");
        jframe.setBounds(780,1070,1000,375);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JTextArea jtextArea=new JTextArea();
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
