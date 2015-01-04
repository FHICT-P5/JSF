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
import jsf32_w5.JSF32_W5_server;

/**
 *
 * @author Juliusername
 */
public class KochManager implements Observer {
    
    private JSF32_W5_server application;
    private List<Edge> edges;
    private KochFractal kochFractal;
    
    public KochManager(JSF32_W5_server application)
    {
        this.application = application;
        edges = new ArrayList<>();
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
    }
    
    public void generateEdges(int level)
    {
        System.out.println("Generating edges");
        kochFractal.setLevel(level);
        kochFractal.generateLeftEdge();
        kochFractal.generateBottomEdge();
        kochFractal.generateRightEdge();
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
