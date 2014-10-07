/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;


/**
 *
 * @author jsf3
 */
public class KochRunnable implements Runnable{
    
    private int id;
    private KochFractal koch;
    private KochManager man;
    
    public KochRunnable(int id, KochFractal koch, KochManager man)
    {
        this.id = id;
        this.koch = koch;
        this.man = man;
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
        
        man.threadCount++;

    }
    
}


