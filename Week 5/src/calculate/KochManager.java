/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observer;
import java.util.Observable;
import jsf31kochfractalfx.JSF31KochFractalFX;
import jsf31kochfractalfx.KochObserver;
import timeutil.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.application.Platform;

/**
 *
 * @author jsf3
 */
public class KochManager implements Observer{
    
    private JSF31KochFractalFX application;
    private KochFractal koch;
    
    private ArrayList<Edge> edges;
    
    Thread thread1;
    Thread thread2;
    Thread thread3;
    
    public int threadCount;
    
    public KochManager(JSF31KochFractalFX application){
        this.application = application;
        
        koch = new KochFractal();
        koch.addObserver(new KochObserver());
        koch.addObserver(this);
        
        edges = new ArrayList();
        
        threadCount = 3;
        
        thread1 = new Thread(new KochRunnable(1, koch, this));
        thread2 = new Thread(new KochRunnable(2, koch, this));
        thread3 = new Thread(new KochRunnable(3, koch, this));

    }
    
    @Override
    public void update (Observable o, Object arg) {
        //application.drawEdge((Edge)arg);
        
        edges.add((Edge)arg);
    }
    
    public void changeLevel(int nxt) {
        koch.setLevel(nxt);
        edges.clear();
        
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start changeLevel");
        
        Platform.runLater(thread1);
        Platform.runLater(thread2);
        Platform.runLater(thread3);
        
        //koch.generateLeftEdge();
        //koch.generateBottomEdge();
        //koch.generateRightEdge();
        
        synchronized(this)
        {
            System.out.println(threadCount);
            if(threadCount == 3)
            {
                application.requestDrawEdges();
                threadCount = 0;
            }            
        
        
            ts.setEnd("Einde changeLevel");
        
            application.setTextNrEdges(String.valueOf(koch.getNrOfEdges()));
            application.setTextCalc(ts.toString());

            drawEdges();
        }
    }
    
    public void drawEdges() {
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start drawEdges");
        
        application.clearKochPanel();
        
        
        for(Edge e : edges){
            application.drawEdge(e);
        }
        
        ts.setEnd("Einde drawEdges");
        application.setTextDraw(ts.toString());
        
    }
}
