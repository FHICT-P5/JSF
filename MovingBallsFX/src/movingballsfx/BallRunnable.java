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
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        while(true)
        {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if(ball.isEnteringCs())
                    {
                        if(monitor.canIMove(this))
                            ball.move();
                    }
                    else if(ball.isLeavingCs())
                    {
                        monitor.leave(ball.getColor());
                        ball.move();
                    }
                    else
                        ball.move();
                    Thread.sleep(ball.getSpeed());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
