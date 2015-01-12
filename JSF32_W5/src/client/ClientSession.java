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
import java.util.Scanner;
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
            
            this.outStream = socket.getOutputStream();
            this.inStream = socket.getInputStream();            
            
            this.out = new ObjectOutputStream(outStream);
            out.flush();
            this.in = new ObjectInputStream(inStream);
            
            
            Object inObject = in.readObject();
            System.out.println(inObject);

            Scanner scanner = new Scanner(System.in);
            String readWriteString = scanner.nextLine();
            int level;

            try {
                level = Integer.parseInt(readWriteString);
                if(level < 1)
                    level = 1;
                else if(level > 9)
                    level = 9;
            }
            catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                level = 1;
            }

            out.writeObject(level);
            out.flush();

            edges = (List<Edge>) in.readObject();
            System.out.println("Edges received");

            for(Edge e : edges) {
                app.drawEdge(e);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
