/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import calculate.Edge;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Juliusername
 */
public class JSF32_W5_client extends Application {

    private Canvas canvas;
    private int currentLevel;
    ClientSession client = null;

    
    public JSF32_W5_client()
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
        
        canvas = new Canvas(500, 500);
        GraphicsContext gc= canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
          
        currentLevel = 1;
        
        client = new ClientSession(this);
                  
        StackPane root = new StackPane();

        root.getChildren().add(canvas);

        Scene scene;
        scene = new Scene(root, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Kochfractals apps");
        //primaryStage.addEventFilter(EventType.ROOT, null);
        primaryStage.show();
        
        
    }

    public void drawEdge(Edge e)
    {
//        e.X1 *= 500;
//        e.Y1 *= 500;
//        e.X2 *= 500;
//        e.Y2 *= 500;
        
        try
        {
        // Graphics
        GraphicsContext gc = canvas.getGraphicsContext2D();
               
        if (e.color == null)
        {
            gc.setStroke(Color.valueOf(e.colorString));
        }
        else
        {
            gc.setStroke(e.color);
        }
        
        //Set line width depending on level
        if (currentLevel <= 3) {
            gc.setLineWidth(2.0);
        }
        else if (currentLevel <=5 ) {
            gc.setLineWidth(1.5);
        }
        else {
            gc.setLineWidth(1.0);
        }
        
        //Draw line
        gc.strokeLine(e.X1,e.Y1,e.X2,e.Y2);
               
        }
        catch (Exception ex)
        {
            System.out.println("Draw Edge Exception: " + ex);
        }
    }
    
    
    private List<Edge> getEdgesFromServer(int level)
    {
        List<Edge> edges = new ArrayList<>();
        
        //TODO: Get edges from server (sockets)
        //edges = server.generateEdge();
        
        return edges;
    }
}
