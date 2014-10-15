/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Bart
 */
public class KochCallable implements Callable, Observer
{
    private int id;
    
    private ArrayList<Edge> edges;
    private KochFractal koch;
    private CyclicBarrier barrier;
    private KochManager manager;
    
    public KochCallable(int id, int level, CyclicBarrier cb, KochManager manager)
    {
        this.id = id;
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
    public Object call() throws Exception
    {
        if (barrier.await() == 0);
        {
            //De laatste thread returns 0
            manager.readCallables();
        }
        return edges;
    }

    @Override
    public void update(Observable o, Object o1)
    {
        edges.add((Edge)o1);
    }
    
    public KochFractal getKochFractal()
    {
        return this.koch;
    }
    
}
