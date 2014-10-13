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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;
import jsf31kochfractalfx.KochObserver;
import timeutil.*;

/**
 *
 * @author jsf3
 */
public class KochManager{
    
    private JSF31KochFractalFX application;
    
    private ArrayList<Edge> edges;
    private ExecutorService pool;
    private CyclicBarrier barrier;
    
    
    public KochManager(JSF31KochFractalFX application){
        this.application = application;
        
        edges = new ArrayList();
        pool = Executors.newFixedThreadPool(3);
        barrier = new CyclicBarrier(3);
    }
    
    public void changeLevel(int nxt) {
        TimeStamp tsTotal = new TimeStamp();
        tsTotal.setBegin("Start total");
        
        edges.clear();
        
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start changeLevel");
        
        KochCallable koch1 = new KochCallable(1, this.barrier);
        KochCallable koch2 = new KochCallable(2, this.barrier);
        KochCallable koch3 = new KochCallable(3, this.barrier);
        
        Future<ArrayList<Edge>> fut1 = pool.submit(koch1);
        Future<ArrayList<Edge>> fut2 = pool.submit(koch2);
        Future<ArrayList<Edge>> fut3 = pool.submit(koch3);

        try
        {
            edges.addAll(fut1.get());
        }
        catch(InterruptedException e1)
        {
            System.out.println("I was interrupted");
        }
        catch(ExecutionException e2)
        {
            System.out.println("Excecution went wrong");
        }

        //application.setTextNrEdges(String.valueOf(koch1.getKochFractal().getNrOfEdges()));
        
        ts.setEnd("Einde changeLevel");     
        application.setTextCalc(ts.toString());
            
        tsTotal.setEnd("Einde total");
        application.setTextCalc(tsTotal.toString());

        
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
