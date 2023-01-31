package com.pj.chess;

import javax.swing.*;
import java.io.*;

public class Config {
    public static String configinfo=null;
    public static void exportconfigevent() {
        String sb=configinfo;
        java.io.BufferedOutputStream buff=null;
        ObjectOutputStream out =null;
        try {
            buff=new  java.io.BufferedOutputStream(new java.io.FileOutputStream("chinesechess.config"));
            buff.write(sb.toString().getBytes());
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
    public static String readconfig() {
        String configtext = null;
        FileInputStream fileInput = null;
        try {
            File chessFile = new File("chinesechess.config");
            fileInput = new java.io.FileInputStream(chessFile);
            BufferedReader bufferedReader = new BufferedReader(
                    new java.io.InputStreamReader(fileInput));
            while (bufferedReader.ready()) {
                configtext = bufferedReader.readLine();
            }
            if (configtext != null) {
                ObjectInputStream objInput = null;
            }
        } catch (Exception e) {
            configinfo = "false";
            exportconfigevent();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(configtext);
        return configtext;
    }
}
