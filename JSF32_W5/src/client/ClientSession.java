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
import java.util.ArrayList;
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
    
    private boolean allEdges;
    
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
            
            System.out.println("[A]ll Edges or [S]ingle Edge? ");
            readWriteString = scanner.nextLine();
            
            if (readWriteString.trim().toLowerCase().equals("a"))
            {
                allEdges = true;
            }
            else
            {
                allEdges = false;
            }

            out.writeObject(level);
            out.flush();
            
            out.writeObject(allEdges);
            out.flush();
           
            if (allEdges)
            {
                edges = (List<Edge>) in.readObject();
                System.out.println("All Edges received");
                
                for(Edge e : edges) {
                app.drawEdge(e);
                }
            }
            else
            {
                edges = new ArrayList<>();
                for (int i = 0; i < 3 * Math.pow(4, level - 1); i++)
                {
                    Edge e = (Edge) in.readObject();
                    edges.add(e);
                    app.drawEdge(e);
                }
                System.out.println("Single Edges received");
            }

            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Zoom(ZoomObject zo)
    {
        try {
            this.out.writeObject(zo);
            this.out.flush();
            
            edges = (List<Edge>) in.readObject();
            System.out.println("Edges received");

            app.clearKochPanel();
            
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
