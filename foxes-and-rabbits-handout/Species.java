import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Species
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    private Clock clock;
    private Weather weather;
    private double foodLevel;
    private boolean isSick;
    private Random rand;
    private boolean isMale;
    private boolean suitable;
    /**
     * Create a new animal at location in field.
     * ;
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Species(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        clock = new Clock();
        weather  = new Weather();
        weather.setWeather();
        rand = new Random();
        isSick = rand.nextBoolean();
        isMale = rand.nextBoolean();
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Species> newSpecies);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    protected boolean isNight()
    {
        return clock.status().equals("Night");
    }
    
    protected String getWeather()
    {
        return weather.getWeather();
    }
            
    protected boolean isSick()
    {
        return isSick;
    }
    
    protected String getGender() {
        String gender; //= "female";
        if(isMale) {
            return gender = "male";
        }
        else {
            return gender = "female";
       }
    }
    
    private boolean findSuitableMate()
    {        
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);
            
            if(s) {
               Possum sexPossum = (Possum) species;
               if(sexPossum.isAlive() && !sexPossum.getGender().equals(getGender()))  { 
                    //System.out.println("We're fucking possums");
                    suitable = true;                    
                }
            }            
        }
        return suitable;
    }        
}
