import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a Possums.
 * Possumss age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Possum extends Species
{
    // Characteristics shared by all Possumss (class variables).

    // The age at which a Possums can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which a Possums can live.
    private static final int MAX_AGE = 7;
    // The likelihood of a Possums breeding.
    private static final double BREEDING_PROBABILITY = 0.4;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 6;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    private static final int GRASS_FOOD_VALUE = 30;
    // Individual characteristics (instance fields).
    
    // The Possums's age.
    private int age;
    
    private boolean suitable;
    private int foodLevel;
    
    /**
     * Create a new Possums. A Possums may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the Possums will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Possum(boolean randomAge, Field field, Location location)
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
     * This is what the Possums does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newPossumss A list to return newly born Possumss.
     */
     public void act(List<Species> newPossums)
      {
        incrementAge();
        incrementHunger();
        sickEffect();
        if(isAlive()) {
            Location newLocation = getLocation();
            //if(getWeather().equals("Snowing")&& isNight()){                
                newLocation = findFood();
            //}
            
            if(findSuitableMate()) {
               giveBirth(newPossums);
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
                //System.out.println("too many possum");
            }
        }
    } 

    /**
     * Increase the age.
     * This could result in the Possums's death.
     */
    /*private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
            //System.out.println("old possums die");
        }
    }*/
    
    /*private  void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }*/
    
    /**
     * Check whether or not this Possums is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPossumss A list to return newly born Possumss.
     */
    private void giveBirth(List<Species> newPossums)
     {
        // New Possumss are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Possum young = new Possum(false, field, loc);
            newPossums.add(young);
            //System.out.println("possums babies");
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
                    //System.out.println("possums are eating");
                    
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    /*private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY && findSuitableMate()) {
             births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }*/

    
    private boolean findSuitableMate()
    {        
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);            
            if(species instanceof Possum) {
               Possum sexPossum = (Possum) species;
               if(sexPossum.isAlive() && !sexPossum.getGender().equals(getGender()))  { 
                    //System.out.println("We're fucking possums");
                    suitable = true;                    
                    if(sexPossum.isSick()){
                        getSick(true);
                    }
                }                               
            }            
        }
        return suitable;
    }        
    
    public int breedingAge()
    {
        return BREEDING_AGE;
    }
    
    public double breedingProbablilty()
    {
        return BREEDING_PROBABILITY;
    }
    
    public int maxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    public int maxAge()
    {
        return MAX_AGE;
    }
}
