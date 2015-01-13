/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import calculate.Edge;
import calculate.KochManager;
import calculate.ZoomObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    
    //Canvas
    private final int kpWidth = 500;
    private final int kpHeight = 500;
    
    // Zoom and drag
    private double zoomTranslateX = 0.0;
    private double zoomTranslateY = 0.0;
    private double zoom = 1.0;
    private double startPressedX = 0.0;
    private double startPressedY = 0.0;
    
    private boolean allEdges;
    
    public ServerSession(Socket client, int clientId) {
        this.client = client;
        this.id = clientId;
    }
    
    public synchronized List<Edge> generateKochFractal(int level)
    {
        KochManager kochManager = new KochManager();
        return kochManager.generateEdges(level);
    }
    
    @Override
    public void run() {
            try {
            
            this.inStream = client.getInputStream();
            this.outStream = client.getOutputStream();
            
            this.in = new ObjectInputStream(inStream);
            this.out = new ObjectOutputStream(outStream);                        
            
            out.writeObject("Level: ");
            
            Object inObject = in.readObject();
            System.out.println(inObject);
            
            level = (int) inObject;
            
            inObject = in.readObject();
            allEdges = (boolean)inObject;
            
            resetZoom();
            
            edges = generateKochFractal(level);
            System.out.println("Edges generated");
            
            if (allEdges)
            {
                out.writeObject(edges);
                System.out.println("All Edges sent");
            }
            else
            {
                for (int i = 0; i < this.edges.size(); i++)
                {
                    out.writeObject(edges.get(i));
                    out.flush();
                }
                System.out.println("Single Edges sent");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception: " + ex.getMessage());
        }
        
        while(true)
        {
            try {
                Object inObject = in.readObject();

                System.out.println("Object received");
                ZoomObject zo = (ZoomObject)inObject;
                System.out.println(zo.getPrimary());

                Zoom(zo);

                this.edges = generateKochFractal(level);

                List<Edge> edgesAfterZoom = new ArrayList<>();
                for (Edge e : this.edges)
                {
                    edgesAfterZoom.add(edgeAfterZoomAndDrag(e));
                }

                edges = edgesAfterZoom;

                out.writeObject(edges);
                System.out.println("Edges sent (after zoom)");

            } catch (IOException ex) {
                Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    private void resetZoom() {
        int kpSize = Math.min(kpWidth, kpHeight);
        zoom = kpSize;
        zoom = 1;
        zoomTranslateX = (kpWidth - kpSize) / 2.0;
        zoomTranslateY = (kpHeight - kpSize) / 2.0;
    }
    
    private Edge edgeAfterZoomAndDrag(Edge e) {
        return new Edge(
                e.X1 * zoom,
                e.Y1 * zoom,
                e.X2 * zoom,
                e.Y2 * zoom,
                e.color);
    }
    
    private void Zoom(ZoomObject zo)
    {
        if (true) {//Math.abs(zo.getX() - startPressedX) < 1.0 && Math.abs(zo.getY() - startPressedY) < 1.0) {
            double originalPointClickedX = (zo.getX() - zoomTranslateX) / zoom;
            double originalPointClickedY = (zo.getY() - zoomTranslateY) / zoom;
            if (zo.getPrimary() == true) {
                zoom *= 2.0;
            } else {
                zoom /= 2.0;
            }
            zoomTranslateX = (int) (zo.getX() - originalPointClickedX * zoom);
            zoomTranslateY = (int) (zo.getY() - originalPointClickedY * zoom);
            
            System.out.println("KOM JE HIER?");
        }
    }
}
