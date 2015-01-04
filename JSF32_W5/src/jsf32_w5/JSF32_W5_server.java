/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_w5;

import calculate.Edge;
import calculate.KochManager;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author Juliusername
 */
public class JSF32_W5_server extends Application {

    public JSF32_W5_server()
    {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
       launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
    }
    
    public synchronized List<Edge> generateKochFractal(int level)
    {
        KochManager kochManager = new KochManager(this);
        kochManager.generateEdges(level);
        
        //TODO: add generated edges to this list
        List<Edge> edges = new ArrayList();
        
        return edges;
    }
}
