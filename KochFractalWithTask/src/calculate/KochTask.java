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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import jsf31kochfractalfx.JSF31KochFractalFX;

/**
 *
 * @author Julius
 */
public class KochTask extends Task<List<Edge>> implements Observer {

    private int id;
    private int level;
    
    private List<Edge> edges;
    private KochFractal koch;
    private KochManager manager;
    private JSF31KochFractalFX application;
    private boolean allowDrawWhiteLine;
    
    public KochTask(int id, int level, KochManager manager, JSF31KochFractalFX fx) 
    {
        this.id = id;
        this.level = level;
        this.koch = new KochFractal();
        this.manager = manager;
        
        edges = new ArrayList<>();
        koch.addObserver(this);
        koch.setLevel(level);
        application = fx;
        allowDrawWhiteLine = true;
        
        bindKochTaskProperties();
    }
    
    @Override
    protected List<Edge> call() throws Exception {
        
        final int MAX = (int)Math.pow(4, (level - 1));
        for (int i = 1; i <= MAX; i++) {
            if (isCancelled()) {
                break;
            }

            updateProgress(i, MAX);
            updateMessage(String.valueOf(i));
        }
        
        switch(id)
        {
            case 1:
                koch.generateLeftEdge();
                break;
            case 2:
                koch.generateRightEdge();
                break;
            case 3:
                koch.generateBottomEdge();
                break;
        }
        
        
               
        return this.edges;
    }
    
    private void drawWhiteLine(final Edge input){
        
        if (allowDrawWhiteLine)
        try {
            Thread.sleep(100);
        
            Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() {
                        application.drawEdge(input, Color.WHITE);
                    }

                }
            );
        } catch (InterruptedException ex) {
            System.out.println("Interupted");
            allowDrawWhiteLine = false;
        }
    }
    
    @Override
    public void update(Observable o, Object o1)
    {
        this.edges.add((Edge)o1);
        this.drawWhiteLine((Edge)o1);
    }
    
    private void bindKochTaskProperties()
    {
        final ProgressBar p1 = application.getProgressBar1();
        final ProgressBar p2 = application.getProgressBar2();
        final ProgressBar p3 = application.getProgressBar3();
        
        final Label l1 = application.getLabel1();
        final Label l2 = application.getLabel2();
        final Label l3 = application.getLabel3();
        
        final ReadOnlyDoubleProperty pp = this.progressProperty();
        final ReadOnlyStringProperty mp = this.messageProperty();
        
        Platform.runLater(new Runnable()
            {
                @Override
                public void run() {
                    switch(id)
                    {
                        case 1:
                            //p1.setProgress(0);
                            p1.progressProperty().bind(pp);
                            l1.textProperty().bind(mp);
                            System.out.println("BINDING");
                            break;
                        case 2:
                            //p2.setProgress(0);
                            p2.progressProperty().bind(pp);
                            l2.textProperty().bind(mp);

                            break;
                        case 3:
                            //p3.setProgress(0);
                            p3.progressProperty().bind(pp);
                            l3.textProperty().bind(mp);

                            break;
                    }
                }
            }
        );
        
    }
}
