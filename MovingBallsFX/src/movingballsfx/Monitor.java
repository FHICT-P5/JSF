/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package movingballsfx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 *
 * @author Bart
 */
public class Monitor
{
    //private final String[] buffer;
    private int count;
    private final Lock lock = new ReentrantLock();
    private int readerCount;
    private int writerCount;
    
    private List<BallRunnable> readers;
    private List<BallRunnable> writers;
    
    public Monitor() {
        readerCount = 0;
        writerCount = 0;
        
        readers = new ArrayList();
        writers = new ArrayList();
    }
    
    public void enterReader() throws InterruptedException 
    {
        lock.lock();
        try
        {
            if(readers.size() > 0 && writerCount == 0)
            {
                for (BallRunnable b : readers)
                {
                    if (b != null)
                    {
                        b.waiting = false;
                        readerCount++;
                        System.out.println("Entering: READER");
                    }
                }
                readers.clear();
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void enterWriter() throws InterruptedException
    {
        lock.lock();
        try
        {
            if(writers.size() > 0 && writerCount == 0 && readerCount == 0)
            {
                int index = 0;
                boolean done = false;
                while (index < writers.size() && done == false)
                {
                    BallRunnable b = writers.get(index);
                    
                    if (b != null)
                    {
                        b.waiting = false;
                        writerCount++;
                        System.out.println("Entering: WRITER");
                        done = true;
                    }
                    writers.remove(b);
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public void AtBeginOfCs(BallRunnable run) throws InterruptedException 
    {
        lock.lock();
        try
        {
            run.waiting = true;
            if(run.getBall().getColor() == Color.RED)
            {
                readers.add(run);
            }
            else
            {
                writers.add(run);
            }
            AllowBall();
        }
        finally
        {
            
            lock.unlock();
        }
    }
    
    private void AllowBall()
    {
        lock.lock();
        
        try
        {
            enterWriter();
            enterReader();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            lock.unlock();
        }
    }
          
    public void leave(Color color)
    {
        lock.lock();
        try
        {
            if(color == Color.RED)
            {
                readerCount--;
                if(readerCount < 0)
                {
                    readerCount = 0;
                }
                System.out.println("Leaving: WRITER");
            }
            else if(color == Color.BLUE)
            {
                writerCount--;
                if(writerCount < 0)
                {
                    writerCount = 0;
                }
                System.out.println("Leaving: READER");
            }

            AllowBall();
        }
        finally
        {
            lock.unlock();
        }
    }
}
