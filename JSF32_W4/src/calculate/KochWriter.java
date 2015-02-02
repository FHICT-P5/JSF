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
                    
//        List<char[]> charList = new ArrayList();

//        for (Edge e : edges)
//        {
//            char[] chars = e.toString().toCharArray();
//
//            charList.add(chars);
//        }

        try
        {
            RandomAccessFile raf = new RandomAccessFile(path + "\\" + level + "MappedBinary", "rw");
            int buffersize = 8 + 128 * edges.size();
            MappedByteBuffer mbb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, buffersize);

            fc = raf.getChannel();
            fl = null;
            
            int writtenEdges = 0;
            
            //Write info
            boolean done = false;
            while (!done)
            {
                try
                {
                    fl = fc.lock(0, 8, false);
                    mbb.position(0);
                    mbb.putInt(writtenEdges);
                    mbb.putInt(level);
                    fl.release();
                    
                    done = true;
                }
                catch (Exception ex)
                {
                    
                }
            }
            
            //Write edges
            long index = 8;
            
//            long bytes = 1;
            
//            for (char[] chars : charList)
//            {
//                mbb.position((int)index);
//                
//                for (char c : chars)
//                {
//                    fl = fc.lock(index, bytes, false);
//                    mbb.putChar(c);
//                    //raf.writeChar(c);
//                    fl.release();
//                    
//                    index++;
//                    
//                    System.out.println("Write Char: " + c);
//                }
//                
//                fl = fc.lock(0, 4, false);
//                mbb.position(0);
//                writtenEdges++;
//                mbb.putInt(writtenEdges);
//                fl.release();
//                //raf.writeChar('P');
//            }
//            //raf.writeChars("ABC");
//
//            raf.close();
            
            //mbb.position((int)index);
            done = false;
            
            for (Edge e : edges)
            {
                while (!done)
                {
                    try
                    {
                        fl = fc.lock(index, 128, false);

                        //System.out.println("File length: " + raf.length());

                        mbb.position((int)index);
                        mbb.putDouble(e.X1);
                        mbb.putDouble(e.Y1);
                        mbb.putDouble(e.X2);
                        mbb.putDouble(e.Y2);

        //                double hue = e.getColorValue();
        //                mbb.putDouble(hue);
                        mbb.putDouble(e.getColor().getRed());
                        mbb.putDouble(e.getColor().getBlue());
                        mbb.putDouble(e.getColor().getGreen());
                        mbb.putDouble(e.getColor().getOpacity());

                        fl.release();

                        fl = fc.lock(0, 4, false);
                        mbb.position(0);
                        writtenEdges++;
                        mbb.putInt(writtenEdges);
                        fl.release();

                        index += 128;
                        
                        done = true;
                    }
                    catch (Exception ex)
                    {
                        
                    }
                }
                
                done = false;
            }

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
