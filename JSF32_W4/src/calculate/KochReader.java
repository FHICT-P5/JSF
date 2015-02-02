/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.paint.Color;

/**
 *
 * @author Juliusername
 */
public class KochReader implements Runnable {
    
    private KochManager manager;
    private boolean useBuffer;
    private String path;
    private int givenLevel;
    private boolean started;
    
    public KochReader(KochManager manager, String path, int level, boolean useBuffer)
    {
        this.manager = manager;
        this.path = path;
        this.givenLevel = level;
        this.useBuffer = useBuffer;
        this.started = false;
    }
    
    public void readFile()
    {
        System.out.println("Mapped file reading start");
                
        FileChannel fc = null;
        FileLock fl = null;
        
        RandomAccessFile raf = null;
        MappedByteBuffer mbb = null;
        
        int readEdges = 0;
        int index = 0;
        int level = givenLevel;
        
        try
        {
            int writtenEdges = 0;
            int totalEdges = 0;
            
            while (!started)
            {
                try
                {
                    raf = new RandomAccessFile(path + "\\" + givenLevel + "MappedBinary", "rw");

                    readEdges = 0;
                    writtenEdges = 0;
                    totalEdges = 0;

                    mbb = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, raf.length());

                    fc = raf.getChannel();
                    
                    fl = fc.lock(0, 8, false);
                    mbb.position(0);
                    writtenEdges = mbb.getInt();
                    level = mbb.getInt();
                    fl.release();
                    
                    started = true;
                }
                catch (Exception ex)
                {
                    //System.out.println("Exception: " + ex.getMessage());
                    
                    if (fl != null)
                    {
                        fl.release();
                    }
                    
                    
                }
                
                if (level == 0)
                {
                    started = false;
                }
            }
            
            totalEdges = (int) (3 * Math.pow(4, level - 1));
            
            //mbb.limit(totalEdges * 128 + 8);
            
            index = 8;
            
//            String chars = "";

//            while (readEdges < totalEdges)
//            {
//                if (index < (writtenEdges * 74) + 8)
//                {
//                    fl = fc.lock(index, 74, false);
//                    mbb.position(index);
//                    
//                    for (int i = index; i < 74; i++)
//                    {
//                        
//                        chars += mbb.getChar(i);
//                    }
//
//                    fl.release();
//
//                    index += 74;
//                    
//                    readEdges++;
//                    
//                    System.out.println("ReadEdges: " + readEdges);
//                    System.out.println("Read Chars: " + chars);
//                }
//            }
            
//            chars = chars.replaceAll("\\.", "Z");
//            chars = chars.replaceAll(System.getProperty("line.separator"), "Q");
//            chars = chars.replaceAll("\\W","");
//            chars = chars.replaceAll("Z", "\\.");
//            chars = chars.replaceAll("Q", System.getProperty("line.separator"));
            
            List<Edge> edges = new ArrayList();
           
            while (readEdges < totalEdges)
            {
                try
                {
                    if (index < (writtenEdges * 128 + 8))
                    {
                        mbb.position((int)index);
                        fl = fc.lock(index, 128, false);
                        double x1 = mbb.getDouble();
                        double y1 = mbb.getDouble();
                        double x2 = mbb.getDouble();
                        double y2 = mbb.getDouble();
                        double red = mbb.getDouble();
                        double blue = mbb.getDouble();
                        double green = mbb.getDouble();
                        double opacity = mbb.getDouble();

                        fl.release();

                        Edge e = new Edge(x1 * 500, y1 * 500, x2 * 500, y2 * 500, new Color(red, green, blue, opacity));
                        edges.add(e);
                        readEdges++;

                        index += 128;
                    }
                    else
                    {
                        mbb.position(0);
                        writtenEdges = mbb.getInt();
                        
                        try
                        {
                            fl.release();
                        }
                        catch(Exception ex)
                        {
                            
                        }
                    }
                }
                catch(Exception ex)
                {
                    
                }
            }
            
            raf.close();

            System.out.println("Mapped file reading done");

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
            
            //createEdges(chars);
            
            for (Edge e : edges)
            {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        manager.drawEdge(e);
                    }
                } );
            }
            
            System.out.println("Edge creation done");
        }
        catch (Exception ex)
        {
            System.out.println("Exception @readFile: " + ex.getMessage());
            System.out.println("ReadEdges: " + readEdges);
            System.out.println("Index: " + index);
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
