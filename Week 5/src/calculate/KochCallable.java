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

/**
 *
 * @author Bart
 */
public class KochCallable implements Callable, Observer
{
    private ArrayList<Edge> edges;
    private KochFractal koch;
    
    public KochCallable()
    {
        this.koch = new KochFractal();
        
        edges = new ArrayList();
        koch.addObserver(this);
    }

    @Override
    public Object call() throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Observable o, Object o1)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
