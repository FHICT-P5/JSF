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
            if(readers.size() > 0)
            {
                readers.get(0).clearInterruptFlag();
                readers.remove(0);
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
            if(writers.size() > 0)
            {
                writers.get(0).clearInterruptFlag();
                writers.remove(0);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public boolean canIMove(BallRunnable run) throws InterruptedException 
    {
        lock.lock();
        try
        {
            if(run.getBall().getColor() == Color.RED && writerCount == 0)
            {
                readerCount++;
                return true;
            }
            else if(run.getBall().getColor() == Color.BLUE && writerCount == 0 && readerCount == 0)
            {
                writerCount++;
                return true;
            }
            else
            {
                if(run.getBall().getColor() == Color.RED)
                {
                    readers.add(run);
                }
                else
                {
                    //writers.add(run);
                }
                throw new InterruptedException();
            }
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
            }
            else if(color == Color.BLUE)
            {
                writerCount--;
                if(writerCount < 0)
                {
                    writerCount = 0;
                }
            }

            if(writerCount == 0 && readerCount == 0 && writers.size() > 0)
            {   
                try
                {
                    enterWriter();
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(writerCount == 0 && readerCount == 0)
            {

                try
                {
                    enterReader();
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }
}
