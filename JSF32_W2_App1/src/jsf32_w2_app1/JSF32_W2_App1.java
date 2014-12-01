/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_w2_app1;

import calculate.KochManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Juliusername
 */
public class JSF32_W2_App1 {

    
    private KochManager kochManager;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
       JSF32_W2_App1 app = new JSF32_W2_App1();
       app.start();
    }

    public void start()
    {
        kochManager = new KochManager();
        kochManager.start();
    }

    
}
