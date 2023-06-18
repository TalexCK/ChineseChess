package com.pj.chess;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Config {
    public static String configinfo=readconfig();
    public static ArrayList configinfos;
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
            configinfo = "[]";
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
    public static void infotolist(){
        configinfo=readconfig();
        configinfos.clear();
        String infopart = "";
        String letterx;
        Integer i = 0;
        Integer endwhile = 0;
        while(endwhile < 1){
            letterx = String.valueOf(configinfo.charAt(i));
            if (letterx.equals(",") ){
                i++;
                configinfos.add(infopart);
                infopart="";
            }
            else if (letterx.equals("[")){
                i=i+1;
                i=i-1;
            }
            else if (letterx.equals("]")){
                configinfos.add(infopart);
                endwhile=1;
            }
            else{
                infopart=infopart+letterx;
            }
            i++;
        }
        System.out.println(configinfos);
        if(configinfos.size()!=VersionFile.configsnum){
            configinfos.clear();
            Integer ii = 0;
            while(ii<=VersionFile.configsnum){
                ii++;
                configinfos.add("");
            }
        }
    }
}
