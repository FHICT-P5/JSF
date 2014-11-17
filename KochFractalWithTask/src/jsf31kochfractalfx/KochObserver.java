/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import calculate.Edge;
import java.util.Observer;
import java.util.Observable;

/**
 *
 * @author jsf3
 */
public class KochObserver implements Observer{
    
    @Override
    public void update(Observable o, Object obj){
        Edge e = (Edge)obj;
        
        //System.out.println("X1: " + String.valueOf(e.X1) + " - Y1: " + String.valueOf(e.Y1));
        //System.out.println("X2: " + String.valueOf(e.X2) + " - Y2: " + String.valueOf(e.Y2));
    }
}
