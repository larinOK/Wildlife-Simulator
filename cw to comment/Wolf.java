import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat Possums, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Wolf extends Species
{
     // Characteristics shared by all foxes (class variables).
    
    // The age at which a wolf can start to breed.
    private static final int BREEDING_AGE = 4;
    // The age to which a wolf can live.
    private static final int MAX_AGE = 14;
    // The likelihood of a wolf breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single wolf. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int POSSUM_FOOD_VALUE = 8;
    private static final int ZEBRA_FOOD_VALUE = 12;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    
    private boolean suitable;
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setAge(0);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            setFoodLevel(rand.nextInt(POSSUM_FOOD_VALUE));
        }
        else {
            setAge(0);
            setFoodLevel(POSSUM_FOOD_VALUE);
        }
        
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * Possums. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Species> newWolves)
    {
        incrementAge();
        incrementHunger();
        sickEffect();
        if(isAlive()) {
            Location newLocation = getLocation();
              newLocation = findFood();                       
            if( findSuitableMate()) {
               giveBirth(newWolves);
            }            
             
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
                
            }
            // See if it was possible to move.
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
     * Look for Possums adjacent to the current location.
     * Only the first live Possum is eaten.
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
            if(species instanceof Zebra) {
                Zebra zebra = (Zebra) species;                
                if(zebra.isAlive()) { 
                    zebra.setDead();                    
                    setFoodLevel(getFoodLevel()+ZEBRA_FOOD_VALUE);
                    //System.out.println("Wolves eat");
                    
                    return where;
                }
            }
            else if(species instanceof Possum) {
                Possum possum = (Possum) species;
                if(possum.isAlive()) {
                    possum.setDead();                    
                    setFoodLevel(getFoodLevel()+POSSUM_FOOD_VALUE);
                    
                   // System.out.println("wolves eat");
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Species> newWolves)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Wolf young = new Wolf(false, field, loc);
            newWolves.add(young);
            //System.out.println("wolf babies");
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
            if(species instanceof Wolf) {
               Wolf sexWolf = (Wolf) species;
               if(sexWolf.isAlive() && !sexWolf.getGender().equals(getGender()))  { 
                   //System.out.println("We're fucking wolves");
                   if(sexWolf.isSick()){
                        getSick(true);
                    }
                   suitable = true;                                       
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
