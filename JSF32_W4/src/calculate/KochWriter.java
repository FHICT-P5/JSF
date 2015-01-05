/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
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
        FileChannel fc;
        FileLock fl;
        
        System.out.println("Mapped file writing start");
                    
        List<char[]> charList = new ArrayList();

        for (Edge e : edges)
        {
            char[] chars = e.toString().toCharArray();

            charList.add(chars);
        }

        try
        {
        RandomAccessFile raf = new RandomAccessFile(path + "\\" + level + "MappedBinary", "rw");
        //int count = 10000;
        //MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);

        fc = raf.getChannel();
        fl = fc.lock(0, raf.length(), false);
        
        for (char[] chars : charList)
        {
            for (char c : chars)
            {
                //mbb.putChar(c);
                raf.writeChar(c);
            }
            raf.writeChar('P');
        }
        //raf.writeChars("ABC");

        raf.close();
        
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
        
        System.out.println("Mapped file writing done.");
        
        System.out.println("Writing done");
        }
        catch (Exception ex)
        {
            System.out.println("Exception @writeFile: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        writeFile();
    }
}
