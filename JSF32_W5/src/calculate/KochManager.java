/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import server.JSF32_W5_server;
import server.ServerSession;

/**
 *
 * @author Juliusername
 */
public class KochManager implements Observer {
    
    private List<Edge> edges;
    private KochFractal kochFractal;
    
    public KochManager()
    {
        edges = new ArrayList<>();
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
    }
    
    public List<Edge> generateEdges(int level)
    {
        System.out.println("Generating edges");
        kochFractal.setLevel(level);
        kochFractal.generateLeftEdge();
        kochFractal.generateBottomEdge();
        kochFractal.generateRightEdge();
        
        return edges;
    }
    
    public synchronized void drawEdge(Edge e)
    {
        //application.drawEdge(e);
    }

    @Override
    public void update(Observable o, Object o1) {
        this.edges.add((Edge)o1);
    }
}
