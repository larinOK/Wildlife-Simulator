import java.util.Arrays;
import java.util.Random;
/**
 * Write a description of class Weather here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Weather
{
    private String currentWeather;
    private String[] weatherArray;
    private Random rand;
    public Weather() {
        rand = new Random();
        weatherArray = new String [] {"Hot", "Snowing", "Raining"};
    }         
    
    public void setWeather() {
       currentWeather = weatherArray[rand.nextInt(10)%weatherArray.length];        
    }
    
    public String getWeather() {
        return currentWeather;
    }
}


