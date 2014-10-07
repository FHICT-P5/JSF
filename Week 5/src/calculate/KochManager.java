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

/**
 *
 * @author jsf3
 */
public class KochManager implements Observer{
    
    private JSF31KochFractalFX application;
    private KochFractal koch;
    
    private CopyOnWriteArrayList<Edge> edges;
    private ArrayList<KochRunnable> threads;
    
    public int threadCount;
    
    public KochManager(JSF31KochFractalFX application){
        this.application = application;
        
        koch = new KochFractal();
        koch.addObserver(new KochObserver());
        koch.addObserver(this);
        
        threads = new ArrayList();
        threadCount = 0;
        
        for(int i = 1; i <= 3; i++)
        {
            threads.add(new KochRunnable(i, koch, this));
        }
        
        edges = new CopyOnWriteArrayList();
             
    }
    
    @Override
    public void update (Observable o, Object arg) {
        //application.drawEdge((Edge)arg);
        
        edges.add((Edge)arg);
    }
    
    public void changeLevel(int nxt) {
        koch.setLevel(nxt);
        
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start changeLevel");
        
        
        //koch.generateLeftEdge();
        //koch.generateBottomEdge();
        //koch.generateRightEdge();
        
        synchronized(this)
        {
            for(int i = 0; i < threads.size(); i++)
            {
                //k.run();
                while (threadCount < i)
                {
                    
                }
                new Thread(threads.get(i)).start();
                
            }
            while(threadCount != 3)
            {
                System.out.println(threadCount);
            }
            
            application.requestDrawEdges();
            threadCount = 0;
        }
        
        ts.setEnd("Einde changeLevel");
        
        application.setTextNrEdges(String.valueOf(koch.getNrOfEdges()));
        application.setTextCalc(ts.toString());
        
        drawEdges();
        edges = new CopyOnWriteArrayList();
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
