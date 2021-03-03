import java.util.Arrays;
import java.util.Random;
/**
 * Generates the weather for our simulation
 *
 * @author (Larin Okunoye & Mahmood Ndanusa)
 * @version (3/3/21)
 */
public class Weather
{
    private String currentWeather;
    private String[] weatherArray;
    private Random rand;
    
    /**
     * Array containing different weathers that are available in our 
     * simulation.
     */
    public Weather() {
        rand = new Random();
        weatherArray = new String [] {"Hot", "Snowing", "Raining"};
    }         
    
    /**
     * Selects the weather randomly from an Array containing different 
     * climates.
     */
    public void setWeather() {
       currentWeather = weatherArray[rand.nextInt(10)%weatherArray.length];        
    }
    
    /**
     * Gets the current weather generated
     * @return gets weather
     */
    public String getWeather() {
        return currentWeather;
    }
}


