package com.pj.chess.multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.pj.chess.LogWindow.addlog;

public class Listener {
    public static String ListenerLaunch(Integer port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        Socket socket = server.accept();
        InputStream ainputstream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int received = ainputstream.read(bytes);
        socket.close();
        server.close();
        addlog("Listened: "+new String(bytes, 0, received));
        return new String(bytes, 0, received);
    }
}
