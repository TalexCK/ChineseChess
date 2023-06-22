package com.pj.chess.multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.pj.chess.LogWindow.addlog;

public class DialogueMoudle {

    public static void ADialogue(String ip, Integer port,String message) throws IOException {
        addlog("Send: "+message);
        Socket socket = new Socket(ip, port);
        OutputStream aoutputstream = socket.getOutputStream();
        aoutputstream.write(message.getBytes());
        socket.close();
    }
}
