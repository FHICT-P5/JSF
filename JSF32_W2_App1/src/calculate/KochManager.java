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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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
    public boolean read;
    private boolean outputType;
    private boolean useBuffer;
    private boolean useMappedFile;
    private int level;
    
    public KochManager(JSF32_W2_App1 application)
    {
        this.application = application;
        textPath = "C:\\Users\\Julius\\Test";
        binaryPath = "C:\\Users\\Julius\\Test";
        edges = new ArrayList<>();
        kochFractal = new KochFractal();
        kochFractal.addObserver(this);
        outputType = true;
        useBuffer = true;
        useMappedFile = true;
        read = true;
    }
    
    public void start()
    {       
        level = 2;
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
        
//        System.out.print("[T]ext or [B]inary: ");
//        String outputTypeString = input.nextLine();
//        if (outputTypeString.toLowerCase().equals("text") || outputTypeString.toLowerCase().equals("t"))
//        {
//            outputType = true;
//        }
//        else if (outputTypeString.toLowerCase().equals("binary") || outputTypeString.toLowerCase().equals("b"))
//        {
//            outputType = false;
//        }
//        else
//        {
//            return;
//        }
        outputType = false;
        System.out.println("Use mapped file? y/n");
        String mappedFileString = input.nextLine();
        if (mappedFileString.toLowerCase().equals("y"))
        {
            useMappedFile = true;
        }
        else if (mappedFileString.toLowerCase().equals("n"))
        {
            useMappedFile = false;
        }
        else
        {
            return;
        }
            
            
//        System.out.print("Use Buffer? [y/n]");
//        String useBufferString = input.nextLine();
//        if (useBufferString.toLowerCase().equals("y"))
//        {
//            useBuffer = true;
//        }
//        else if (useBufferString.toLowerCase().equals("n"))
//        {
//            useBuffer = false;
//        }
//        else
//        {
//            return;
//        }
//        
//        if (outputType)
//        {
//            //Clear file
//            clearFile();
//        }
        
        
        System.out.print("Level: ");
        int levelInput = input.nextInt();
        level = levelInput;
        
        if (read == false)
        {
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
            
                file = new File(textPath + "\\Level_" + level + ".txt");
                System.out.println(file);
                
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
                if (useMappedFile == false)
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
                            if (e != null)
                            {
                                //e.readObject(reader);
                                //System.out.println("Color: " + e.color.toString());
                                System.out.println("TEST");

                                e.X1 *= 500;
                                e.Y1 *= 500;
                                e.X2 *= 500;
                                e.Y2 *= 500;
                                application.drawEdge(e);
                            }
                            else
                            {
                                System.out.println("Edge is null");
                            }
                        }

                    } catch (Exception ex) {
                        System.out.println("Binary Read Exception: " + ex.getMessage());
                    }
                }
                else
                {
                    //TODO Mapped file read
                    
                    System.out.println("Mapped file reading start");
                    
                    RandomAccessFile raf = new RandomAccessFile(binaryPath + "\\" + level + "MappedBinary", "rw");
                   
                    
                    //MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 10000);

                    
                    String chars = "";
                    
                    chars = raf.readLine();
                    
                    
//                    for (int i = 0; i < 10000; i++)
//                    {
//                        try
//                        {
//                            char c = (char)mbb.getChar(i);
//                            chars += c;
//                        }
//                        catch(Exception ex)
//                        {
//                            System.out.println("MappedFileReading Exception: " + ex.getMessage());
//                            break;
//                        }
//                    }
                    
                    
                    
                    raf.close();
                    
                    chars = chars.replaceAll("\\.", "Z");
                    chars = chars.replaceAll("\\W","");
                    chars = chars.replaceAll("Z", "\\.");
      
                    System.out.println("Mapped file reading done");
                    
                    System.out.println("READ: " + chars);
                    
                    createEdges(chars);
                    System.out.println("Edge creation done");
                    
                    
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
        if (true)
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

                            System.out.println("X1: " + X1);
                            System.out.println("Y1: " + Y1);
                            System.out.println("X2: " + X2);
                            System.out.println("Y2: " + Y2);
                            
                            if (useMappedFile)
                            {
                                //X1 *= 500;
                                //Y1 *= 500;
                                //X2 *= 500;
                                //Y2 *= 500;
                            }

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
        
        System.out.println("HERE");
    }
       
    private void writeToFile(boolean append)
    {
        System.out.println("AAA");
        
        try
        {
            if (outputType == true)
            {
                System.out.println("BBB");
                
                PrintWriter output;
                if (useBuffer == true)
                {
                    output = new PrintWriter(new BufferedWriter(new FileWriter(textPath + "\\Level_" + level + ".txt", append)));
                }
                else
                {
                    output = new PrintWriter(new FileWriter(textPath + "\\Level_" + level + ".txt", append));
                }
                
                for (Edge e : edges)
                {
                    output.println(e.toString());;
                }
                
                output.close();
            }
            else
            {
                System.out.println("CCC");
                
                if (useMappedFile == false)
                {
                    System.out.println("DDD");
                    
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
                else
                {
                    System.out.println("EEE");
                    
                    //Mapped file write
                    
                    System.out.println("Mapped file writing start");
                    
                    List<char[]> charList = new ArrayList();
                    
                    for (Edge e : edges)
                    {
                        char[] chars = e.toString().toCharArray();
                            
                        charList.add(chars);
                    }
                    
                    
                    RandomAccessFile raf = new RandomAccessFile(binaryPath + "\\" + level + "MappedBinary", "rw");
                    int count = 10000;
                    //MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);
                    
                    for (char[] chars : charList)
                    {
                        for (char c : chars)
                        {
                            //mbb.putChar(c);
                            System.out.println(c);
                            raf.writeChar(c);
                        }
                    }
                    //raf.writeChars("ABC");
                    
                    raf.close();
                    
                    System.out.println("Mapped file writing done.");
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
