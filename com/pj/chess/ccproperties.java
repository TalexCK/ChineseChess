package com.pj.chess;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ccproperties {
    static Properties cc = new Properties();
    public static String readProperties(String key){
        String info = "";
        try {
            cc.load(Files.newInputStream(Paths.get("chinesechess.properties")));

            info = cc.getProperty(key);

        } catch (IOException e) {
            if(e.toString().contains("NoSuchFileException")){
                firstsavefile();
            }
        }
        return info;
    }
    public static void writeProperties(String key, String info){
        try {
            cc.load(Files.newInputStream(Paths.get("chinesechess.properties")));
            cc.setProperty(key, info);
            cc.store(new FileWriter("chinesechess.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void firstsavefile(){
        java.io.BufferedOutputStream buff=null;
        ObjectOutputStream out =null;
        try {
            buff=new  java.io.BufferedOutputStream(new java.io.FileOutputStream("chinesechess.properties"));
            buff.write("".getBytes());
            buff.flush();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
