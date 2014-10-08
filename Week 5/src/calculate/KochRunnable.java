/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observable;
import java.util.Observer;


/**
 *
 * @author jsf3
 */
public class KochRunnable implements Runnable, Observer{
    
    private int id;
    private KochFractal koch;
    private KochManager man;
    
    public KochRunnable(int id, int level, KochManager man)
    {
        this.id = id;
        this.koch = new KochFractal();
        this.man = man;
        
        koch.addObserver(this);
        koch.setLevel(level);
    }
    
    public int getId()
    {
        return id;
    }
    
    @Override
    public synchronized void run()
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
        man.increaseCount();
        

    }

    @Override
    public void update(Observable o, Object arg) {
        man.addEdge((Edge)arg);
    }
    
    public KochFractal getKochFractal()
    {
        return koch;
    }    
}


