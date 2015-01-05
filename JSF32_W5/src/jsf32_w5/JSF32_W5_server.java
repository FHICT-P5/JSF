/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf32_w5;

import calculate.Edge;
import calculate.KnockKnockProtocol;
import calculate.KochManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author Juliusername
 */
public class JSF32_W5_server extends Application {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
       launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerSocket ss = new ServerSocket(1090);
        
            try
            {
                Socket clientSocket = ss.accept();

                String inputLine, outputLine;
            
                int level;
                boolean useBuffer;

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                KnockKnockProtocol kkp = new KnockKnockProtocol();
                outputLine = kkp.processInput(null);
                out.println(outputLine);
                
                 while ((inputLine = in.readLine()) != null) {
                    outputLine = kkp.processInput(inputLine);
                    out.println(outputLine);
                    if (outputLine.equals("Bye."))
                    break;
                 }
                
                Scanner input = new Scanner(clientSocket.getChannel());
                
                System.out.print("Level: ");
                String readWriteString = input.nextLine();
                try
                {
                    level = Integer.parseInt(readWriteString);
                }
                catch (Exception ex)
                {
                    System.out.println("Exception: " + ex.getMessage());
                    return;
                }
                
                List<Edge> edges = generateKochFractal(level);
                
                ObjectOutputStream oos = (ObjectOutputStream)clientSocket.getOutputStream();
                oos.writeObject(edges);
                oos.flush();
                oos.close();
            }
            catch (Exception ex)
            {
                
            }
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
