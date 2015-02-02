/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package calculate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class Edge implements Serializable {
    public double X1, Y1, X2, Y2;
    public transient Color color;
    public String colorString;
    
    public Edge(double X1, double Y1, double X2, double Y2, Color color) {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
        this.color = color;
        this.colorString = color.toString();
    }

    public void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        this.color = Color.valueOf(colorString);
    }
    
    public double getColorValue()
    {
        if (color == null)
        {
            return Color.WHITE.getHue();
        }
        else
        {
            return color.getHue();
        }
    }
    
    public Color getColor()
    {
        return color;
    }
    
    @Override
    public String toString()
    {
        char colorChar;
        
        if (this.color == Color.RED)
        {
            colorChar = 0;
        }
        else if (this.color == Color.BLUE)
        {
            colorChar = 1;
        }
        else
        {
            colorChar = 2;
        }
        
        return X1 + "_" + Y1 + "_" + X2 + "_" + Y2 + "_" + colorChar;
    }
}
