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
     * This is what the Possums does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newPossumss A list to return newly born Possumss.
     */
    public void act(List<Species> newGrass)
    {
        incrementAge();
        sickEffect();
        if(isAlive()) {            
            giveBirth(newGrass); 
            
        }
    }
    
    private void giveBirth(List<Species> newGrass)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(false, field, loc);
            newGrass.add(young);
            //System.out.println("grass babies");
        }
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
