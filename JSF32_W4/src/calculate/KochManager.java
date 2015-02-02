/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javafx.application.Platform;
import jsf32_w4.JSF32_W4;

/**
 *
 * @author Juliusername
 */
public class KochManager implements Observer {
    
    private JSF32_W4 application;
    private List<Edge> edges;
    private KochFractal kochFractal;
    private String path;
    private boolean useBuffer;
    private int level;
    private int writerCount;
    private int readerCount;
    
    public KochManager(JSF32_W4 application)
    {
        this.application = application;
        path = "C:\\Users\\Julius\\Test";
        edges = new ArrayList<>();
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
        useBuffer = true;
    }
    
    public void start()
    {
        Scanner input = new Scanner(System.in);
        
        System.out.print("How many writers?");
        String readWriteString = input.nextLine();
        try
        {
            writerCount = Integer.parseInt(readWriteString);
            if (writerCount < 0)
            {
                writerCount = 0;
            }
            else if (writerCount > 5)
            {
                writerCount = 5;
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
            return;
        }
        
        System.out.print("How many readers?");
        readWriteString = input.nextLine();
        try
        {
            readerCount = Integer.parseInt(readWriteString);
            if (readerCount < 0)
            {
                readerCount = 0;
            }
            else if (readerCount > 5)
            {
                readerCount = 5;
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
            return;
        }
        
        System.out.print("Level: ");
        readWriteString = input.nextLine();
        try
        {
            level = Integer.parseInt(readWriteString);
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
            return;
        }
        
        System.out.print("Buffer? [y/n]");
        readWriteString = input.nextLine();
        try
        {
            if (readWriteString.toLowerCase().equals("y"))
            {
                useBuffer = true;
            }
            else if (readWriteString.toLowerCase().equals("n"))
            {
                useBuffer = false;
            }
            else
            {
                System.out.println("Foute input");
                return;
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
            return;
        }
        
        if (writerCount > 0)
        {
            System.out.println("Generating edges");
            kochFractal.setLevel(level);
            kochFractal.generateLeftEdge();
            kochFractal.generateBottomEdge();
            kochFractal.generateRightEdge();
        }
        
        
        
        for (int i = 0; i < writerCount; i++)
        {
            KochWriter writer = new KochWriter(this, path, level, useBuffer, edges);
            System.out.println("Running writer " + i);
            //Platform.runLater(writer);
            Thread thread = new Thread(writer);
            thread.start();
        }
        
        for (int i = 0; i < readerCount; i++)
        {
            KochReader reader = new KochReader(this, path, level, useBuffer);
            System.out.println("Running reader " + i);
            //Platform.runLater(reader);
            Thread thread = new Thread(reader);
            thread.start();
        }
    }
    
    public synchronized void drawEdge(Edge e)
    {
        application.drawEdge(e);
    }

    @Override
    public void update(Observable o, Object o1) {
        this.edges.add((Edge)o1);
    }
}
