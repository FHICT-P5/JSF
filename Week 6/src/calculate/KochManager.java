/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;
import jsf31kochfractalfx.KochObserver;
import timeutil.*;

/**
 *
 * @author jsf3
 */
public class KochManager implements Observer{
    
    private JSF31KochFractalFX application;
    
    private ArrayList<Edge> edges;
    private CyclicBarrier barrier;
    
    //Thread thread1;
    //Thread thread2;
    //Thread thread3;
    
    public int threadCount;
    
    public KochManager(JSF31KochFractalFX application){
        this.application = application;
        
        threadCount = 0;
        
        edges = new ArrayList();

    }
    
    @Override
    public void update (Observable o, Object arg) {
        //application.drawEdge((Edge)arg);
        
        edges.add((Edge)arg);
    }
    
    public void changeLevel(int nxt) {
        TimeStamp tsTotal = new TimeStamp();
        tsTotal.setBegin("Start total");
        
        edges.clear();
        
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start changeLevel");
        
        KochRunnable koch1 = new KochRunnable(1, nxt, this);
        KochRunnable koch2 = new KochRunnable(2, nxt, this);
        KochRunnable koch3 = new KochRunnable(3, nxt, this);
        
        Thread thread1 = new Thread(koch1);
        Thread thread2 = new Thread(koch2);
        Thread thread3 = new Thread(koch3);
        
        thread1.setName("T1");
        thread2.setName("T2");
        thread3.setName("T3");
        
        thread1.start();
        thread2.start();
        thread3.start();

        application.setTextNrEdges(String.valueOf(koch1.getKochFractal().getNrOfEdges()));
        
        ts.setEnd("Einde changeLevel");     
        application.setTextCalc(ts.toString());
            
        tsTotal.setEnd("Einde total");
        application.setTextCalc(tsTotal.toString());

        
    }
    
    public synchronized void increaseCount(){
        threadCount++;
        if(threadCount == 3)
        {
            application.requestDrawEdges();
            threadCount = 0;
        }
    }
    
    public synchronized void addEdge(Edge e){
        edges.add(e);
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
