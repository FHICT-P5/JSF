/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CyclicBarrier;
import javafx.concurrent.Task;

/**
 *
 * @author Julius
 */
public class KochTask extends Task<Void> implements Observer {

    private int id;
    private int level;
    
    private ArrayList<Edge> edges;
    private KochFractal koch;
    private CyclicBarrier barrier;
    private KochManager manager;
    
    public KochTask(int id, int level, CyclicBarrier cb, KochManager manager)
    {
        this.id = id;
        this.level = level;
        this.koch = new KochFractal();
        this.barrier = cb;
        this.manager = manager;
        
        edges = new ArrayList();
        koch.addObserver(this);
        koch.setLevel(level);
        
        switch(id)
        {
            case 1:
                koch.generateLeftEdge();
                break;
            case 2:
                koch.generateRightEdge();
                break;
            case 3:
                koch.generateBottomEdge();
                break;
        }
    }
    
    @Override
    protected Void call() throws Exception {
        final int MAX = level;
        for (int i = 1; i <= MAX; i++) {
            if (isCancelled()) {
                break;
            }
            updateProgress(i, MAX);
            updateMessage(String.valueOf(i));
            System.out.println("AAAAAAAAAAAAA");

            Thread.sleep(100);
        }
               
        return null;
    }
    
    @Override
    public void update(Observable o, Object o1)
    {
        manager.addEdge((Edge)o1);
    }
}
