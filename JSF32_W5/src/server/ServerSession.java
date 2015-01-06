/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bart
 */
public class ServerSession implements Runnable{

    private InputStream inStream = null;
    private OutputStream outStream = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private Socket client;
    private int id;
    
    public ServerSession(Socket client, int clientId) {
        try {
            this.client = client;
            this.id = clientId;
            
            this.inStream = client.getInputStream();
            this.outStream = client.getOutputStream();
            
            this.in = new ObjectInputStream(inStream);
            this.out = new ObjectOutputStream(outStream);
            
            out.writeObject("Level: ");
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
            
    }
    
}
