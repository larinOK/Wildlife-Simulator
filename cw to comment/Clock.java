import java.util.Random;
/**
 * Write a description of class Clock here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Clock
{
    // instance variables - replace the example below with your own
    private double time;
    private int days;
    private Random rand;
    /**
     * Constructor for objects of class Clock
     */
    public Clock()
    {
        // initialise instance variables
        time = 0;
        rand = new Random();
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public double getTime()
    {
        return time%24;
    }
    
    public void timeReset()
    {
        time = rand.nextInt();        
    }
    
    public void increaseTime()
    {
        time = time + 0.25;
    }
        
    public String status()
    {
        if(getTime()>=18 || getTime()<=3 ) {
            return "Night";
        }
        else {
            return "Day";
        }
    }
}
