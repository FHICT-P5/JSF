/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsf32_w2_app1.JSF32_W2_App1;
import timeutil.TimeStamp;

/**
 *
 * @author Juliusername
 */
public class KochManager implements Observer {
    
    private JSF32_W2_App1 application;
    private List<Edge> edges;
    private KochFractal kochFractal;
    private String textPath;
    private String binaryPath;
    private boolean read;
    private boolean outputType;
    private boolean useBuffer;
    private int level;
    
    public KochManager(JSF32_W2_App1 application)
    {
        this.application = application;
        textPath = "C:\\Users\\Julius\\Test\\Test.txt";
        binaryPath = "C:\\Users\\Julius\\Test";
        edges = new ArrayList<>();
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
        outputType = true;
        useBuffer = true;
        read = true;
    }
    
    public void start()
    {       
        level = 1;
        Scanner input = new Scanner(System.in);
        
        System.out.print("[R]ead or [W]rite?");
        String readWriteString = input.nextLine();
        if (readWriteString.toLowerCase().equals("r"))
        {
            read = true;
        }
        else if (readWriteString.toLowerCase().equals("w"))
        {
            read = false;
        }
        else
        {
            return;
        }
        
        System.out.print("[T]ext or [B]inary: ");
        String outputTypeString = input.nextLine();
        if (outputTypeString.toLowerCase().equals("text") || outputTypeString.toLowerCase().equals("t"))
        {
            outputType = true;
        }
        else if (outputTypeString.toLowerCase().equals("binary") || outputTypeString.toLowerCase().equals("b"))
        {
            outputType = false;
        }
        else
        {
            return;
        }
        
        System.out.print("Use Buffer? [y/n]");
        String useBufferString = input.nextLine();
        if (useBufferString.toLowerCase().equals("y"))
        {
            useBuffer = true;
        }
        else if (useBufferString.toLowerCase().equals("n"))
        {
            useBuffer = false;
        }
        else
        {
            return;
        }
        
        if (read == false)
        {
            //Clear file
            clearFile();
            
            //Write
            System.out.print("Level: ");
            int levelInput = input.nextInt();
            level = levelInput;

            if (levelInput > 0 || levelInput <= 10)
            {
                kochFractal.setLevel(levelInput);
                kochFractal.generateLeftEdge();
                kochFractal.generateBottomEdge();
                kochFractal.generateRightEdge();
            }
            
            TimeStamp tsWrite = new TimeStamp();
            tsWrite.setBegin("Start writing");
            
            writeToFile(true);
            
            tsWrite.setEnd("end writing");
            
            System.out.println("Writing time:" + tsWrite.toString());
        }
        else
        {
            TimeStamp tsRead = new TimeStamp();
            tsRead.setBegin("Start Reading");
            
            //Read
            this.edges.clear();
            readFromFile();
            
            tsRead.setEnd("End Reading");
            
            System.out.println("Writing time:" + tsRead.toString());
        }
    }
    
    @Override
    public void update(Observable o, Object o1)
    {
        this.edges.add((Edge)o1);
    }
    
    private void readFromFile()
    {
        
        
        try
        {
            
            if (outputType == true)
            {
                String content = null;
                File file; 
            
                file = new File(textPath);
                
                if (useBuffer == true)
                {
                    BufferedReader fileInput = new BufferedReader(new FileReader(file));

                    char[] chars = new char[(int) file.length()];

                    fileInput.read(chars);
                    content = new String(chars);
                    fileInput.close();
                }
                else
                {
                    FileReader fileInput = new FileReader(file);
                    char[] chars = new char[(int) file.length()];

                    fileInput.read(chars);
                    content = new String(chars);
                    fileInput.close();
                }
                
                //System.out.println("Content:" + content);
                createEdges(content);
            }
            else
            {
                //Binary to object
                List<Edge> output = new ArrayList<>();
                String file;
                ObjectInputStream reader = null;
                try {
                    if (useBuffer) {
                        file = binaryPath + File.separator + String.valueOf(level) + "BufferedBinary";
                        System.out.println(file);
                        InputStream is = new FileInputStream(file);
                        reader = new ObjectInputStream(new BufferedInputStream(is));
                    } else {
                        file = binaryPath + File.separator + String.valueOf(level) + "UnbufferedBinary";
                        System.out.println(file);
                        InputStream is = new FileInputStream(file);
                        reader = new ObjectInputStream(is);
                    }
                 output = (List<Edge>)reader.readObject();
                 
                 edges = output;
                 
                    System.out.println("Output size: " + output.size());
                 
                 for (Edge e : edges)
                 {
                     //e.readObject(reader);
                     System.out.println("Color: " + e.color.toString());
                     application.drawEdge(e);
                 }
                 
            } catch (Exception ex) {
                System.out.println("Binary Read Exception: " + ex.getMessage());
            }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Read Exception: " + ex.getMessage());
        }
    }
    
    private void createEdges(String content)
    {
        if (outputType == true)
        {
            String[] lines = content.split(System.getProperty("line.separator"));

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


                            Edge e = new Edge(X1, Y1, X2, Y2, c);

                            application.drawEdge(e);
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
       
    private void writeToFile(boolean append)
    {
        try
        {
            if (outputType == true)
            {
                PrintWriter output;
                if (useBuffer == true)
                {
                    output = new PrintWriter(new BufferedWriter(new FileWriter(textPath, append)));
                }
                else
                {
                    output = new PrintWriter(new FileWriter(textPath, append));
                }
                
                for (Edge e : edges)
                {
                    output.println(e.toString());;
                }
                
                output.close();
            }
            else
            {
                String file = binaryPath + File.separator + level + "BufferedBinary";
                ObjectOutputStream os;
                try {
                    os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                    os.writeObject(edges);
                    os.flush();
                    os.close();
                } catch (IOException ex) {

                }
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
        }
        finally
        {
            
        }
    }
    
    private void clearFile()
    {
        if (outputType == true)
        {
            try
            {
            PrintWriter writer = new PrintWriter(textPath);
            writer.print("");
            writer.close();
            }
            catch (Exception ex)
            {
                System.out.println("Clearfile Exception: " + ex.getMessage());
            }
        }
        else
        {
            try
            {
            PrintWriter writer = new PrintWriter(binaryPath);
            writer.print("");
            writer.close();
            }
            catch (Exception ex)
            {
                System.out.println("Clearfile Exception: " + ex.getMessage());
            }
        }
    }
}
