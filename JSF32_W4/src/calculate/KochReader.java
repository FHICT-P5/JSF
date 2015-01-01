/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juliusername
 */
public class KochReader implements Runnable {
    
    private KochManager manager;
    private boolean useBuffer;
    private String path;
    private int level;
    
    public KochReader(KochManager manager, String path, int level, boolean useBuffer)
    {
        this.manager = manager;
        this.path = path;
        this.level = level;
        this.useBuffer = useBuffer;
    }
    
    public void readFile()
    {
        //Binary to object
        List<Edge> output = new ArrayList<>();
        String file;
        ObjectInputStream reader = null;

        try {
            if (useBuffer) {
                file = path + File.separator + String.valueOf(level) + "Binary";
                System.out.println(file);
                InputStream is = new FileInputStream(file);
                reader = new ObjectInputStream(new BufferedInputStream(is));
            } else {
                file = path + File.separator + String.valueOf(level) + "Binary";
                System.out.println(file);
                InputStream is = new FileInputStream(file);
                reader = new ObjectInputStream(is);
            }
            output = (List<Edge>)reader.readObject();

            System.out.println("Output size: " + output.size());

            for (Edge e : output)
            {
                if (e != null)
                {
                    //e.readObject(reader);
                    //System.out.println("Color: " + e.color.toString());
                    System.out.println("TEST");

                    e.X1 *= 500;
                    e.Y1 *= 500;
                    e.X2 *= 500;
                    e.Y2 *= 500;
                    manager.drawEdge(e);
                }
                else
                {
                    System.out.println("Edge is null");
                }
            }

        } catch (Exception ex) {
            System.out.println("Binary Read Exception: " + ex.getMessage());
        }
        
        System.out.println("Reading done");
    }

    @Override
    public void run() {
        readFile();
    }
}
