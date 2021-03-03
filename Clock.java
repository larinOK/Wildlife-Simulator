import java.util.Random;
/**
 * A simple clock to record time in our program.
 *
 * @author (Larin Okunoye & Mahmood Ndanusa)
 * @version (3/3/21)
 */
public class Clock
{
    
    private double time;
    private int days;
    private Random rand;
    /**
     * Constructor for objects of class Clock
     * Creates a new clock to record the current time/time of day in the 
     * simulation.
     */
    public Clock()
    {
        
        time = 0;
        rand = new Random();
    }

    /**
     * Gets the current time
     * @return the modulus of time when didvided by 24
     */
    public double getTime()
    {
        return time%24;
    }
    
    /**
     * Resets the time
     */
    public void timeReset()
    {
        time = rand.nextInt();        
    }
    
    /**
     * increases time by adding 0.25 to the current time
     */
    public void increaseTime()
    {
        time = time + 0.25;
    }
        
    /**
     * Uses current time to determine if its day or night
     */
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
