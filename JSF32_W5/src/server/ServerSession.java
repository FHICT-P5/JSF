/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import calculate.Edge;
import calculate.KochManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
    private int level;
    private List<Edge> edges;
    
    public ServerSession(Socket client, int clientId) {
        try {
            this.client = client;
            this.id = clientId;
            
            this.inStream = client.getInputStream();
            this.outStream = client.getOutputStream();
            
            this.in = new ObjectInputStream(inStream);
            this.out = new ObjectOutputStream(outStream);                        
            
            out.writeObject("Level: ");
            
            Object inObject = in.readObject();
            System.out.println(inObject);
            
            level = (int) inObject;
            edges = generateKochFractal(level);
            System.out.println("Edges generated");
            
            out.writeObject(edges);
            System.out.println("Edges sent");
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    public synchronized List<Edge> generateKochFractal(int level)
    {
        KochManager kochManager = new KochManager();
        return kochManager.generateEdges(level);
    }
    
    @Override
    public void run() {
            
    }
    
}
