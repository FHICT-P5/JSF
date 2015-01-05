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
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

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
        System.out.println("Mapped file reading start");
                
        FileChannel fc;
        FileLock fl;
        
        try
        {
            RandomAccessFile raf = new RandomAccessFile(path + "\\" + level + "MappedBinary", "rw");

            //MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 10000);
            fc = raf.getChannel();
            fl = fc.lock(0, raf.length(), false);

            String chars = "";

            chars = raf.readLine();

            raf.close();

            chars = chars.replaceAll("\\.", "Z");
            chars = chars.replaceAll(System.getProperty("line.separator"), "Q");
            chars = chars.replaceAll("\\W","");
            chars = chars.replaceAll("Z", "\\.");
            chars = chars.replaceAll("Q", System.getProperty("line.separator"));

            System.out.println("Mapped file reading done");

            System.out.println("READ: " + chars);

            if (fl != null)
            {
                try
                {
                    fl.release();
                }
                catch (Exception ex)
                {
                    System.out.println("Exception @fl.release" + ex.getMessage());
                }
            }
            
            createEdges(chars);
            System.out.println("Edge creation done");
        }
        catch (Exception ex)
        {
            System.out.println("Exception @readFile: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        readFile();
    }
    
    private void createEdges(String content)
    {
        if (true)
        {
            String[] lines = content.split("P"); //content.split(System.getProperty("line.separator"));

            for (String line : lines)
            {
                if (!line.contains("Level"))
                {
                    String[] attributes = line.split("_");
                    if (attributes.length == 5)
                    {
                        try
                        {
                            double X1 = Double.parseDouble(attributes[0]) * 500;
                            double Y1 = Double.parseDouble(attributes[1]) * 500;
                            double X2 = Double.parseDouble(attributes[2]) * 500;
                            double Y2 = Double.parseDouble(attributes[3]) * 500;
                            Color c = Color.web(attributes[4]);

//                            System.out.println("X1: " + X1);
//                            System.out.println("Y1: " + Y1);
//                            System.out.println("X2: " + X2);
//                            System.out.println("Y2: " + Y2);

                            Edge e = new Edge(X1, Y1, X2, Y2, c);

                            manager.drawEdge(e);
                        }
                        catch (Exception ex)
                        {
                            System.out.println("Edge Exception: " + ex.getMessage());
                        }
                    }
                }
            }
        }
        else
        {
            
        }
        
    }
}
