/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import calculate.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bart
 */
public class ClientSession {
    
    JSF32_W5_client app;
    List<Edge> edges;
    
    private InputStream inStream = null;
    private OutputStream outStream = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    Socket socket = null;
    private String hostName = "localhost";
    private int portNumber = 1090;
    
    public ClientSession(JSF32_W5_client app) {
        try {
            this.app = app;
            socket = new Socket(hostName, portNumber);
            
            this.inStream = socket.getInputStream();
            this.outStream = socket.getOutputStream();
            
            this.in = new ObjectInputStream(inStream);
            this.out = new ObjectOutputStream(outStream);
            
            while(true) {
                Object inObject = in.readObject();
                System.out.println(inObject);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
