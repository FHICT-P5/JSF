/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import calculate.Edge;
import calculate.KochManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Juliusername
 */
public class JSF32_W5_server {
    
    public static ExecutorService pool = Executors.newCachedThreadPool();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(1090);
            System.out.println("Server started");
            
            int id = 1;
            while(true) {
                Socket clientSocket = ss.accept();
                pool.execute(new ServerSession(clientSocket, id));
                id++;
            }
        } catch (IOException ex) {
            Logger.getLogger(JSF32_W5_server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IOException at Server Main: " + ex.getMessage());
        }
    }
    

}
