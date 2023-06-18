package com.pj.chess.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;//无法确定主机IP地址时抛出异常
import java.util.Scanner;

import static com.pj.chess.LogWindow.addlog;


public class ClientDia {
    Socket socket;
    public ClientDia(String ips, Integer ports) throws UnknownHostException, IOException{
        addlog("正在与服务器建立连接...");
        socket = new Socket(ips,ports);
        addlog("连接成功");
    }
}
