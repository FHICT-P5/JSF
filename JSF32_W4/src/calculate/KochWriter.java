/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juliusername
 */
public class KochWriter implements Runnable {
    
    private KochManager manager;
    private boolean useBuffer;
    private String path;
    private int level;
    private List<Edge> edges;
    
    public KochWriter(KochManager manager, String path, int level, boolean useBuffer, List<Edge> edges)
    {
        this.manager = manager;
        this.path = path;
        this.level = level;
        this.useBuffer = useBuffer;
        this.edges = edges;
    }
    
    public void writeFile()
    {
        String file = path + File.separator + level + "Binary";
        ObjectOutputStream os;

        if (useBuffer == true)
        {
            try {
            os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            os.writeObject(edges);
            os.flush();
            os.close();
            } catch (IOException ex) {
                System.out.println("IOException ex: " + ex.getMessage());
            }
        }
        else
        {
            try {
                os = new ObjectOutputStream(new FileOutputStream(file));
                os.writeObject(edges);
                os.flush();
                os.close();
            } catch (IOException ex) {
                System.out.println("IOException ex: " + ex.getMessage());
            }
        }
        
        System.out.println("Writing done");
    }

    @Override
    public void run() {
        writeFile();
    }
}
