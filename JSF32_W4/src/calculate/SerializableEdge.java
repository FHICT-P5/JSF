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
public class SerializableEdge implements Serializable {
    
    public double X1, Y1, X2, Y2;
    public String colorString;
    
    public SerializableEdge(Edge e)
    {
        X1 = e.X1;
        X2 = e.X2;
        Y1 = e.Y1;
        Y2 = e.Y2;
        colorString = e.color.toString();
    }
    
    @Override
    public String toString()
    {
        String edgeString = "";
        edgeString = X1 + "-" + Y1 + "-" + X2 + "-" + Y2 + "-" + colorString;
        return edgeString;
    }
}
