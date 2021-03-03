import java.util.List;
import java.util.Random;
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Grass extends Species
{
    // instance variables - replace the example below with your own
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 10;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.7;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    
    /**
     * Constructor for objects of class Grass
     * Creates grass. Grass may be created with age
     * zero (a new plant) or with a random age
      
     * @param randomAge If true, the Grass will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grass(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables
        super(field, location);
        if(randomAge) {
           setAge(rand.nextInt(MAX_AGE)); 
        }
    } 

    /**
     * This is what the Grass does most of the time - it is immobile 
     * Sometimes its numbers will increase or die of old age.
     * @param newGrass A list to return newly grown Grass.
     */
    public void act(List<Species> newGrass)
    {
        incrementAge();
        sickEffect();
        if(isAlive()) {            
            giveBirth(newGrass); 
            
        }
    }
    
    /**
     * Check whether or not grass is able to grow at this step.
     * New grass will be grown in free adjacent locations.
     * @param newGrass A list to return newly grown Grass.
     */
    private void giveBirth(List<Species> newGrass)
    {
        // New grass grows in adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(false, field, loc);
            newGrass.add(young);
            
        }
    }    
    

    /**
     * Gets the age of grass
     * @return age of grass
     */
    public int breedingAge()
     {
        return BREEDING_AGE;
    }
    
    /**
     * Gets the probability of grass being able to reproduce
     * @return probabilty of breeding
     */
    public double breedingProbablilty()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Gets the maximum number of grass grown
     * @return maximum number
     */
    public int maxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Gets the maximum age grass is able to live up to
     * @return maximum age
     */
    public int maxAge()
    {
        return MAX_AGE;
    }
}
