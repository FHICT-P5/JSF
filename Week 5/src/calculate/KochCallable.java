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
    
    public KochCallable(int id, CyclicBarrier cb)
    {
        this.id = id;
        this.koch = new KochFractal();
        this.barrier = cb;
        
        edges = new ArrayList();
        koch.addObserver(this);
    }

    @Override
    public Object call() throws Exception
    {
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
        barrier.await();
        return edges;
    }

    @Override
    public void update(Observable o, Object o1)
    {
        edges.add((Edge)o1);
    }
    
}
