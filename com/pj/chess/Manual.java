package com.pj.chess;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import static com.pj.chess.ChessBoardMain.aitoai;
import static com.pj.chess.LogWindow.addlog;


public class Manual {
    public static ArrayList manuallist = new ArrayList();
    public static ArrayList manuallistread = new ArrayList();
    public static void clearmanuallist(){
        manuallist.clear();
    }
    public static void importmanualevent(String filename) {
        if(filename.substring(0)=="/"){
            String filenames = filename.substring(1,filename.length());
            manuallistread.clear();
            String manualinfo = readmanual(filenames);

            String seedpart = "";
            String letterx;
            Integer i = 0;
            Integer endwhile = 0;
            while (endwhile < 1) {
                letterx = String.valueOf(manualinfo.charAt(i));
                if (letterx.equals(",")) {
                    i++;
                    manuallistread.add(seedpart);
                    seedpart = "";
                } else if (letterx.equals("[")) {
                    i = i + 1;
                    i = i - 1;
                } else if (letterx.equals("]")) {
                    manuallistread.add(seedpart);
                    endwhile = 1;
                } else {
                    seedpart = seedpart + letterx;
                }
                i++;
            }

            addlog("棋谱数据: " + manualinfo);
            System.out.println("测试数据: " + manuallistread);
            ChessBoardMain.manualload();
        }
        else {
            manuallistread.clear();
            String manualinfo = readmanual(filename);

            String seedpart = "";
            String letterx;
            Integer i = 0;
            Integer endwhile = 0;
            while (endwhile < 1) {
                letterx = String.valueOf(manualinfo.charAt(i));
                if (letterx.equals(",")) {
                    i++;
                    manuallistread.add(seedpart);
                    seedpart = "";
                } else if (letterx.equals("[")) {
                    i = i + 1;
                    i = i - 1;
                } else if (letterx.equals("]")) {
                    manuallistread.add(seedpart);
                    endwhile = 1;
                } else {
                    seedpart = seedpart + letterx;
                }
                i++;
            }

            addlog("棋谱数据: " + manualinfo);
            System.out.println("测试数据: " + manuallistread);
            ChessBoardMain.manualload();
        }
    }


    public static void exportmanualevent(String filename) {
        java.io.BufferedOutputStream buff=null;
        ObjectOutputStream out =null;
        try {
            buff=new  java.io.BufferedOutputStream(new java.io.FileOutputStream(filename + ".manual"));
            buff.write(manuallist.toString().getBytes());
            buff.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                if(buff!=null){
                    buff.close();
                }
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String readmanual(String filename) {
        String manualtext = null;
        FileInputStream fileInput = null;
        try {
            File chessFile = new File(filename + ".manual");
            fileInput = new java.io.FileInputStream(chessFile);
            BufferedReader bufferedReader = new BufferedReader(
                    new java.io.InputStreamReader(fileInput));
            while (bufferedReader.ready()) {
                manualtext = bufferedReader.readLine();
            }
            if (manualtext != null) {
                if(aitoai==1){
                    ObjectInputStream objInput = null;
                    System.out.println(manualtext);
                } else if (JOptionPane.showConfirmDialog(null, "检测该棋谱，确认导入?\n该操作将会永久删除当前对局记录", "棋谱",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    ObjectInputStream objInput = null;
                    System.out.println(manualtext);
                } else {
                    chessFile.deleteOnExit();
                    JOptionPane.showMessageDialog(null,"找不到该棋谱","提示",JOptionPane.PLAIN_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"找不到该棋谱","提示",JOptionPane.PLAIN_MESSAGE);
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(manualtext);
        return manualtext;
    }
    public static String readmanualin(String filename) {
        String manualtext = null;
        FileInputStream fileInput = null;
        try {
            File chessFile = new File("/Manuals/" + filename + ".manual");
            fileInput = new java.io.FileInputStream(chessFile);
            BufferedReader bufferedReader = new BufferedReader(
                    new java.io.InputStreamReader(fileInput));
            while (bufferedReader.ready()) {
                manualtext = bufferedReader.readLine();
            }
            if (manualtext != null) {
                if (JOptionPane.showConfirmDialog(null, "检测该棋谱，确认导入?\n该操作将会永久删除当前对局记录", "棋谱",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    ObjectInputStream objInput = null;
                    System.out.println(manualtext);
                } else {
                    chessFile.deleteOnExit();
                    JOptionPane.showMessageDialog(null,"找不到该棋谱","提示",JOptionPane.PLAIN_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"找不到该棋谱","提示",JOptionPane.PLAIN_MESSAGE);
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(manualtext);
        return manualtext;
    }
}
/*
public static  final String[] chessName=new String[]{
            "   ",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
            "黑将","黑车","黑车","黑马","黑马","黑炮","黑炮","黑象","黑象","黑士","黑士","黑卒","黑卒","黑卒","黑卒","黑卒",
            "红将","红车","红车","红马","红马","红炮","红炮","红象","红象","红士","红士","红卒","红卒","红卒","红卒","红卒",
    };
    public static  final String[] chessIcon=new String[]{
            null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
            "BK","BR","BR","BN","BN","BC","BC","BB","BB","BA","BA","BP","BP","BP","BP","BP",
            "RK","RR","RR","RN","RN","RC","RC","RB","RB","RA","RA","RP","RP","RP","RP","RP",
    };
 */
