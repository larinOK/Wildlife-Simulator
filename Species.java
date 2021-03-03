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
    private int age;
    private static final double SICK_PROBABILITY = 0.2;
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
        setSick();
        isMale = rand.nextBoolean();
        age = 0;
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
    
    /**
     * Indicates night time
     * @return current time and nigt
     */
    protected boolean isNight()
    {
        return clock.status().equals("Night");
    }
    
    /**
     * Returns the current weather
     * @return current weather
     */
    protected String getWeather()
    {
        return weather.getWeather();
    }
            
    /**
     * Checks if an animal has been infected with a disease
     */
    protected void setSick()
    {
        if(rand.nextDouble() <= SICK_PROBABILITY) {
            isSick = true;
        }
        else {
            isSick = false;
        }
        
    }
    
    /**
     * Returns if an animal is sick or not
     * @return health of an animal
     */
    protected boolean isSick()
    {
        return isSick;
    }
    
    /**
     * Returns the gender of an organism
     * @return gender of an organism
     */
    protected String getGender() {
        String gender; //= "female";
        if(isMale) {
            return gender = "male";
        }
        else {
            return gender = "female";
       }
    }
    
    /*protected boolean findSuitableMate()
    {        
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();        
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);
            //Species sexSpecies; //= new Species(field, where);
            if(species.getClass().equals(this.getClass())) {
               //Possum sexPossum = (Possum) species;
               if(this.isAlive() && !this.getGender().equals(getGender())){ 
                   System.out.println("We're fucking ");
                   suitable = true;                    
               }
            }            
        }
        return suitable;
    }*/       
    
    abstract protected int breedingAge();    
    
    abstract protected double breedingProbablilty();
    
    abstract protected int maxLitterSize();
    
    abstract protected int maxAge();    
    
    /**
     * Sets the age of an organism
     */
    protected void setAge(int initialAge) 
    {
        age = initialAge;
    }
    
    /**
     * Returns the age of an organism
     * @return age of an organism
     */
    protected int getAge() {
        return age;
    }
    
    protected void incrementAge() 
    {
        age++;
        if(age > maxAge()) {
            setDead();
            //System.out.println("zebra old age");
        }
    }
    
    protected boolean canBreed()
    {
       return age >= breedingAge();
    }
    
    protected void setFoodLevel(double initialLevel) 
    {
        foodLevel = initialLevel;
    }
    
    protected double getFoodLevel() 
    {
        return foodLevel;
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= breedingProbablilty() ) {
            births = rand.nextInt(maxLitterSize()) + 1;
        }
        return births;
    }    
    
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    protected void sickEffect() {
        if(isSick()) {
            //System.out.println("animal is sick");
            setFoodLevel(getFoodLevel()/2);            
        }        
    }
    
    protected void getSick(boolean sick)
    {
        isSick = true;
    }
}
