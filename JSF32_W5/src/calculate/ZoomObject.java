/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.io.Serializable;

/**
 *
 * @author Juliusername
 */
public class ZoomObject implements Serializable {
    
    private double x;
    private double y;
    private boolean primary;
    
    public ZoomObject(double x, double y, boolean primary)
    {
        this.x = x;
        this.y = y;
        this.primary = primary;
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public boolean getPrimary()
    {
        return primary;
    }
}
