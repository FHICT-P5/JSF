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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.*;

/**
 *
 * @author jsf3
 */
public class KochManager {

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

    /*
     Future<ArrayList<Edge>> fut1;
     Future<ArrayList<Edge>> fut2;
     Future<ArrayList<Edge>> fut3;   
     */
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;

        edges = new ArrayList();
        pool = Executors.newFixedThreadPool(3);
        barrier = new CyclicBarrier(3);

        KochTasks = new ArrayList();

    }

    public void changeLevel(int nxt) {
        interuptKochTasks();
        application.clearKochPanel();
        TimeStamp tsTotal = new TimeStamp();
        tsTotal.setBegin("Start total");

        edges.clear();

        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start changeLevel");

        //application.setTextNrEdges(String.valueOf(koch1.getKochFractal().getNrOfEdges()));
        ktLeft = new KochTask(1, nxt, this, application);
        ktBottom = new KochTask(2, nxt, this, application);
        ktRight = new KochTask(3, nxt, this, application);

        EventHandler doneHandler = new EventHandler() {

            @Override
            public void handle(Event event) {
                if (ktLeft.isDone() && ktRight.isDone() && ktBottom.isDone()) {

                    ktLeft.setOnSucceeded(null);
                    ktBottom.setOnSucceeded(null);
                    ktRight.setOnSucceeded(null);

                    try {
                        edges.addAll(ktLeft.get());
                        edges.addAll(ktBottom.get());
                        edges.addAll(ktRight.get());
                        // draw stuff

                        application.requestDrawEdges();
                    } catch (InterruptedException | ExecutionException ex) {
                        Logger.getLogger(KochManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        ktLeft.setOnSucceeded(doneHandler);
        ktBottom.setOnSucceeded(doneHandler);
        ktRight.setOnSucceeded(doneHandler);

        try {
            pool.submit(ktLeft);
            pool.submit(ktBottom);
            pool.submit(ktRight);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        //application.bindKochTaskProperties(ktLeft, ktBottom, ktRight);
        ts.setEnd("Einde changeLevel");
        application.setTextCalc(ts.toString());

        tsTotal.setEnd("Einde total");
        application.setTextCalc(tsTotal.toString());

    }

    public synchronized void addEdge(Edge e) {
        edges.add(e);
        drawSingleEdge();
    }

    public synchronized void drawSingleEdge() {
        application.drawEdge(edges.get(edges.size() - 1), null);
        System.out.println("Drawing edge");
    }

    public void drawEdges() {
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Start drawEdges");

        application.clearKochPanel();

        for (Edge e : edges) {
            application.drawEdge(e, null);
        }

        ts.setEnd("Einde drawEdges");
        application.setTextDraw(ts.toString());
    }

    public void readCallables() {
        //application.requestDrawEdges();
    }

    private void interuptKochTasks() {
        try {
            ktLeft.cancel();
            ktBottom.cancel();
            ktRight.cancel();
            ktLeft = null;
            ktBottom = null;
            ktRight = null;
        } catch (Exception ex) {

        }
//        if (ktLeft != null)
//        {
//            if (ktLeft.isRunning())
//            {
//                ktLeft.cancel();
//                System.out.println("CANCELED");
//            }
//
//            if (ktBottom.isRunning())
//            {
//                ktBottom.cancel();
//            }
//
//            if (ktRight.isRunning())
//            {
//                ktRight.cancel();
//            }
//        }
    }
}
