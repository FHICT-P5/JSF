/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 *
 * @author Juliusername
 */
public class KochManager implements Observer {
    
    private List<Edge> edges;
    private KochFractal kochFractal;
    private String path;
    private FileOutputStream file;
    
    public KochManager()
    {
        path = "C:\\MyFiles\\Test\\Test.txt";
        edges = new ArrayList<>();
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
    }
    
    public void start()
    {
        try {
            file = new FileOutputStream(path, true);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        
        Scanner input = new Scanner(System.in);
        System.out.print("Level: ");
        int levelInput = input.nextInt();
        
        if (levelInput > 0 || levelInput <= 10)
        {
            writeToFile("Level: " + levelInput);
            
            kochFractal.setLevel(levelInput);
            kochFractal.generateLeftEdge();
            kochFractal.generateBottomEdge();
            kochFractal.generateRightEdge();
        }
        
        for(Edge e : edges)
        {
            writeToFile(e.toString());
        }
    }
    
    @Override
    public void update(Observable o, Object o1)
    {
        //this.edges.add((Edge)o1);
        //writeToFile(((Edge)o1).toString());
        this.edges.add((Edge)o1);
        System.out.println("Edge added");
    }
    
    private void clearFile()
    {
        
    }
    
    private void writeToFile(String s)
    {
        try
        {
            OutputStream buffer = new BufferedOutputStream(file);
            //ObjectOutput output = new ObjectOutputStream(buffer);
            //output.writeObject(s);
            System.out.println(s);
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
            output.println(s);
            output.close();
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }
        finally
        {
            
        }
    }
}
