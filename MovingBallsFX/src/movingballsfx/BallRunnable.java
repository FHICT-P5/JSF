/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movingballsfx;

/**
 *
 * @author Peter Boots
 */
public class BallRunnable implements Runnable {

    private Monitor monitor;
    private Ball ball;
    public boolean waiting;

    public BallRunnable(Ball ball, Monitor monitor) {
        this.ball = ball;
        this.monitor = monitor;
    }
    
    public Ball getBall()
    {
        return ball;
    }
    
    public void clearInterruptFlag()
    {
        Thread.currentThread().interrupted();
        System.out.println("Clearing interupt");
    }

    @Override
    public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (waiting == false)
                    {
                        ball.move();

                        if(ball.isEnteringCs())
                        {
                            monitor.AtBeginOfCs(this);
                        }
                        else if(ball.isLeavingCs())
                        {
                            monitor.leave(ball.getColor());
                        }

                        Thread.sleep(ball.getSpeed());
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
        }
            System.out.println("Stopping run");
    }
}
