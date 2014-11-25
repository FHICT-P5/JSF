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
import javafx.concurrent.Task;
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
        
        
        //bindKochTaskProperties();
    }
    
    @Override
    protected List<Edge> call() throws Exception {
        
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
        
        final int MAX = level;
        for (int i = 1; i <= MAX; i++) {
            if (isCancelled()) {
                break;
            }

            updateProgress(i, MAX);
            updateMessage(String.valueOf(i));
        }
               
        return this.edges;
    }
    
    private void drawWhiteLine(final Edge input){
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(KochTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Platform.runLater(new Runnable()
            {
                @Override
                public void run() {
                    application.drawEdge(input, Color.WHITE);
                }
            }
        );
        
    }
    
    @Override
    public void update(Observable o, Object o1)
    {
        this.edges.add((Edge)o1);
        this.drawWhiteLine((Edge)o1);
    }
    
//    private void bindKochTaskProperties()
//    {
//        Platform.runLater(new Runnable()
//            {
//                @Override
//                public void run() {
//                    application.drawEdge(input, Color.WHITE);
//                }
//            }
//        );
//        switch(id)
//        {
//            case 1:
//                application.getProgressBar1().progressProperty().bind(this.progressProperty());
//                application.getLabel1().textProperty().bind(this.messageProperty());
//
//                break;
//            case 2:
//                application.getProgressBar2().progressProperty().bind(this.progressProperty());
//                application.getLabel2().textProperty().bind(this.messageProperty());
//
//                break;
//            case 3:
//                application.getProgressBar3().progressProperty().bind(this.progressProperty());
//                application.getLabel3().textProperty().bind(this.messageProperty());
//
//                break;
//        }
//    }
}
