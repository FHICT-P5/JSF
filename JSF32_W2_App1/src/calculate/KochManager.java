/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
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
    
    public KochManager(JSF32_W2_App1 application)
    {
        this.application = application;
        textPath = "C:\\Users\\Julius\\Test\\Test.txt";
        binaryPath = "C:\\Users\\Julius\\Test\\Binary.bin";
        edges = new ArrayList<>();
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
        outputType = true;
        useBuffer = true;
        read = true;
    }
    
    public void start()
    {       
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

            if (levelInput > 0 || levelInput <= 10)
            {
                writeToFile("Level: " + levelInput, false);

                kochFractal.setLevel(levelInput);
                kochFractal.generateLeftEdge();
                kochFractal.generateBottomEdge();
                kochFractal.generateRightEdge();
            }
            
            TimeStamp tsWrite = new TimeStamp();
            tsWrite.setBegin("Start writing");
            
            for(Edge e : edges)
            {
                writeToFile(e.toString(), true);
            }
            
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
        String content = null;
        
        try
        {
            File file; 
            if (outputType == true)
            {
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
            }
            else
            {
                file = new File(binaryPath);
                
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
                    content = "";
                    DataInputStream dataInput = new DataInputStream(new FileInputStream(file));
                    int data;
                    while ((data = dataInput.read()) != -1) { content += data; }
                    dataInput.close();
                    
                    System.out.println(content);
                }
            }
            
            
            
            //System.out.println("Content:" + content);
            createEdges(content);
        }
        catch (Exception ex)
        {
            System.out.println("Read Exception: " + ex.getMessage());
        }
    }
    
    private void createEdges(String content)
    {
        String[] lines = content.split(System.getProperty("line.separator"));
        
        for (String line : lines)
        {
            if (!line.contains("Level"))
            {
                String[] attributes = line.split("-");
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
       
    private void writeToFile(String s, boolean append)
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
                
                output.println(s);
                output.close();
            }
            else
            {
//                DataOutputStream os = new DataOutputStream(new FileOutputStream(binaryPath, true));
//                os.write(((Edge)o).toString().getBytes());
//                os.close();
                
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(binaryPath, true));
                os.write(s.getBytes());
                os.close();
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
