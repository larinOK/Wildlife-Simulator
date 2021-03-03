import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * A simple model of a Lion.
 * Lions age, move, breed, eat,contact diseases and die.
 *
 * @author (Larin Okunoye & Mahmood Ndanusa)
 * @version (3/3/21)
 */
public class Lion extends Species
{
    // instance variables - replace the example below with your own
    private static final int BREEDING_AGE = 8;
    // The age to which a fox can live.
    private static final int MAX_AGE = 30;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single Possum. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int POSSUM_FOOD_VALUE = 8;
    private static final int ZEBRA_FOOD_VALUE = 12;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    private boolean suitable;

    /**
     * Constructor for objects of class Lion
     * Creates a new lion. A Wolf may be created with age
     * zero (a new lion) or with a random age
      
     * @param randomAge If true, the lion will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Lion(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            setFoodLevel(rand.nextInt(POSSUM_FOOD_VALUE));
        }
        else {
            setAge(0);
            setFoodLevel(ZEBRA_FOOD_VALUE);
        }       
    }

    /**
     * This is what the lion does most of the time: it hunts for
     * Possums or Zebra. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newLion A list to return newly born lions.
     */
    public void act(List<Species> newLions)
    {
        incrementAge();
        incrementHunger();
        sickEffect();
        if(isAlive()) {
            Location newLocation = getLocation();
                  
               newLocation = findFood();                
            
            
            if(findSuitableMate()) {
               giveBirth(newLions);
            }
            // Move towards a source of food if found.
            
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
     * Look for Possums/Zebra adjacent to the current location.
     * Only the first live Possum/Zebra is eaten.
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
            if(species instanceof Possum) {
                Possum possum = (Possum) species;                
                if(possum.isAlive()) { 
                    possum.setDead();                    
                    setFoodLevel(getFoodLevel()+POSSUM_FOOD_VALUE);
                    
                    //System.out.println("lion eat");
                    return where;
                }
            }            
            else if(species instanceof Zebra) {
                Zebra zebra = (Zebra) species;
                if(zebra.isAlive()) {
                    zebra.setDead();                    
                    setFoodLevel(getFoodLevel()+ZEBRA_FOOD_VALUE);
                    
                    //System.out.println("lions eat");
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this lion is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newLion A list to return newly born lions.
     */
    private void giveBirth(List<Species> newLions)
    {
        // New lions are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc);
            newLions.add(young);
            
        }
    }
        

    /**
     * Looks for a lion of the opposite gender to enable breeding
     */
    private boolean findSuitableMate()
    {
          
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);
            if(species instanceof Lion) {
               Lion sexLion = (Lion) species;
               if(!isSick()&&sexLion.isAlive() && !sexLion.getGender().equals(getGender()))  { 
                   //System.out.println("We're fucking lions");
                   if(sexLion.isSick()){
                        getSick(true);
                    }
                   suitable = true;                                       
                }                                     
           }
        }
        return suitable;
    }
    
  
    /**
     * Gets the age of a lion about to breed
     * @return age of the lion
     */ 
    public int breedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Gets the probability of a lion being able to breed
     * @return probability of breeding
     */
    public double breedingProbablilty()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Gets the maximum number of lions born
     * @return maximum number
     */
    public int maxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Gets the maximum age a lion is able to live up to
     * @return maximum age
     */
    public int maxAge()
    {
        return MAX_AGE;
    }
 }

