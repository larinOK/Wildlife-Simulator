import java.util.Random;
import java.util.List;
import java.util.Iterator;
/**
 * A simple model of a Zebra.
 * Zebra age, move, breed, eat,contact diseases and die.
 *
 * @author (Larin Okunoye & Mahmood Ndanusa)
 * @version (3/3/21)
 */

public class Zebra extends Species
{
    // The age at which a Possums can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which a Possums can live.
    private static final int MAX_AGE = 30;
    // The likelihood of a Possums breeding.
    private static final double BREEDING_PROBABILITY = 0.45;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    private static final int GRASS_FOOD_VALUE = 30;
    // Individual characteristics (instance fields).
    
    
    
    private boolean suitable;
    
    

    /**
     * Constructor for objects of class Zebra
     * Creates a new Zebra. A Zebra may be created with age
     * zero (a new Zebra) or with a random age
      
     * @param randomAge If true, the Zebra will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Zebra(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setAge(0);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            setFoodLevel(rand.nextInt(GRASS_FOOD_VALUE));
        }
        else {
            setAge(0);
            setFoodLevel(GRASS_FOOD_VALUE);                   
        }
        
    }

    /**
     * This is what the Zebra does most of the time - it runs 
     * around. Sometimes it will breed, get a disease or die of old age.
     * @param newZebras A list to return newly born Zebra.
     */
     public void act(List<Species> newZebras)
      {
        incrementAge();
        incrementHunger();
        sickEffect();
        if(isAlive()) {
            Location newLocation = getLocation();
            if(getWeather().equals("Snowing")&& isNight()){                
                newLocation = findFood();
            }
            
            if(findSuitableMate()) {
              giveBirth(newZebras);
            }
            // Try to move into a free location.
            //Location newLocation = getField().freeAdjacentLocation(getLocation());
            
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
                
            }
            
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
        
    
    /**
     * Check whether or not this Zebras is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newZebras A list to return newly born Zebras.
     */
    private void giveBirth(List<Species> newZebras)
     {
        // New Zebra are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Zebra young = new Zebra(false, field, loc);
            newZebras.add(young);
            
        }
    }
        
    /**
     * Look for Grass adjacent to the current location.
     * Only the first live Grass is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);
            if(species instanceof Grass) {
                Grass grass = (Grass) species;
                if(grass.isAlive()) { 
                    grass.setDead();
                    setFoodLevel(getFoodLevel()+GRASS_FOOD_VALUE);
                    
                    
                    return where;
                }
            }
        }
        return null;
    }
    
    
    /**
     * Looks for a Zebra of the opposite gender to enable breeding
     */
    private boolean findSuitableMate()
    {        
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);
            if(species instanceof Zebra) {
               Zebra sexZebra = (Zebra) species;
               if(sexZebra.isAlive() && !sexZebra.getGender().equals(getGender()))  { 
                    
                    if(sexZebra.isSick()){
                        getSick(true);
                    }
                    suitable = true;                    
                }
            }            
        }
        return suitable;
    }
    
    /**
     * Gets the age of a Zebra about to breed
     * @return age of the Zebra
     */
    public int breedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Gets the probability of a Zebra being able to breed
     * @return probability of breeding
     */
    public double breedingProbablilty()
    {
        return BREEDING_PROBABILITY;
    }
    
    
    /**
     * Gets the maximum number of Zebra born
     * @return maximum number
     */
    public int maxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Gets the maximum age a Zebra is able to live up to
     * @return maximum age
     */
    public int maxAge()
    {
        return MAX_AGE;
    }
 }
