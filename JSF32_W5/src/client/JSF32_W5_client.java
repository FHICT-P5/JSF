/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import calculate.Edge;
import calculate.KochManager;
import calculate.ZoomObject;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Juliusername
 */
public class JSF32_W5_client extends Application {

    ClientSession client = null;

    // Koch manager
    // TO DO: Create class KochManager in package calculate
    private KochManager kochManager;
    
    // Current level of Koch fractal
    private int level;
    
    // Koch panel and its size
    private Canvas kochPanel;
    private final int kpWidth = 500;
    private final int kpHeight = 500;
    
    // Zoom and drag
    private double zoomTranslateX = 0.0;
    private double zoomTranslateY = 0.0;
    private double zoom = 1.0;
    private double startPressedX = 0.0;
    private double startPressedY = 0.0;

       public JSF32_W5_client()
    {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
                
        
        
        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);
        
        // Drawing panel for Koch fractal
        kochPanel = new Canvas(kpWidth,kpHeight);
        grid.add(kochPanel, 0, 3, 25, 1);
        
        
        
        // Add mouse clicked event to Koch panel
        kochPanel.addEventHandler(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    kochPanelMouseClicked(event);
                }
            });
        
        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root, kpWidth+50, kpHeight+300);
        root.getChildren().add(grid);
        
        // Define title and assign the scene for main window
        primaryStage.setTitle("Koch Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        clearKochPanel();
        
        client = new ClientSession(this);
    }
    
    public void clearKochPanel() {
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
        gc.clearRect(0.0,0.0,kpWidth,kpHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0.0,0.0,kpWidth,kpHeight);
    }
    
    public void drawEdge(Edge e)
    {
        e.X1 *= 500;
        e.Y1 *= 500;
        e.X2 *= 500;
        e.Y2 *= 500;
        
        try
        {
        // Graphics
        GraphicsContext gc = kochPanel.getGraphicsContext2D();
               
        if (e.color == null)
        {
            gc.setStroke(Color.valueOf(e.colorString));
        }
        else
        {
            gc.setStroke(e.color);
        }
        
        //Set line width depending on level
        if (level <= 3) {
            gc.setLineWidth(2.0);
        }
        else if (level <=5 ) {
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
 
//    private void fitFractalButtonActionPerformed(ActionEvent event) {
//        resetZoom();
//        kochManager.drawEdges();
//    }
    
    private void kochPanelMouseClicked(MouseEvent event) {
        
        System.out.println("MouseEvent");
        
        double x = event.getX();
        double y = event.getY();
        boolean primary;
        
        if (event.getButton() == MouseButton.PRIMARY) {
                primary = true;
            } else if (event.getButton() == MouseButton.SECONDARY) {
                primary = false;
            }
            else 
            {
                return;
            }
        
        ZoomObject zo = new ZoomObject(x, y, primary);
        
        client.Zoom(zo);
    }
    
        private void kochPanelMousePressed(MouseEvent event)
        {
            startPressedX = event.getX();
            startPressedY = event.getY();
        }
}
