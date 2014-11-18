/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import jsf31kochfractalfx.JSF31KochFractalFX;
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
    
    //private KochCallable koch1;
    //private KochCallable koch2;
    //private KochCallable koch3;
    
    private KochTask ktLeft;
    private KochTask ktBottom;
    private KochTask ktRight;
    
    private ArrayList<KochTask> KochTasks;
    
    Future<ArrayList<Edge>> fut1;
    Future<ArrayList<Edge>> fut2;
    Future<ArrayList<Edge>> fut3;   
    
    public KochManager(JSF31KochFractalFX application){
        this.application = application;
        
        edges = new ArrayList();
        pool = Executors.newFixedThreadPool(3);
        barrier = new CyclicBarrier(3);
        
        KochTasks = new ArrayList();
        
    }
    
    public void changeLevel(int nxt) {
        TimeStamp tsTotal = new TimeStamp();
        tsTotal.setBegin("Start total");
        
        edges.clear();
        
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start changeLevel");
        
        //koch1 = new KochCallable(1, nxt, this.barrier, this);
        //koch2 = new KochCallable(2, nxt, this.barrier, this);
        //koch3 = new KochCallable(3, nxt, this.barrier, this);
        
        //fut1 = pool.submit(koch1);
        //fut2 = pool.submit(koch2);
        //fut3 = pool.submit(koch3);

        //application.setTextNrEdges(String.valueOf(koch1.getKochFractal().getNrOfEdges()));
        
        ktLeft = new KochTask(1, nxt, this.barrier, this);
        ktBottom = new KochTask(2, nxt, this.barrier, this);
        ktRight = new KochTask(3, nxt, this.barrier, this);
        
        application.bindKochTaskProperties(ktLeft, ktBottom, ktRight);
        
        ts.setEnd("Einde changeLevel");     
        application.setTextCalc(ts.toString());
            
        tsTotal.setEnd("Einde total");
        application.setTextCalc(tsTotal.toString());
    }
    
    public synchronized void addEdge(Edge e){
        edges.add(e);
        drawSingleEdge();
    }
    
    public synchronized void drawSingleEdge()
    {
        application.drawEdge(edges.get(edges.size() - 1));
    }
       
    public void drawEdges() {
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start drawEdges");
        
        application.clearKochPanel();
        
        try
        {
            edges.addAll(fut1.get());
            edges.addAll(fut2.get());
            edges.addAll(fut3.get());
        }
        catch(InterruptedException e1)
        {
            System.out.println("I was interrupted");
        }
        catch(ExecutionException e2)
        {
            System.out.println("Excecution went wrong");
        }
        
        for(Edge e : edges){
            application.drawEdge(e);
        }
        
        ts.setEnd("Einde drawEdges");
        application.setTextDraw(ts.toString());
    }
    
    public void readCallables()
    {
        //application.requestDrawEdges();
    }
}
