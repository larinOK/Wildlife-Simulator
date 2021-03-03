import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Lion here.
 *
 * @author (your name)
 * @version (a version number or a date)
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
    // The fox's age.
    private int age;
    // The fox's food level, which is increased by eating Possums.
    private double foodLevel;
    
    private boolean suitable;

    /**
     * Constructor for objects of class Lion
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

    public void act(List<Species> newLions)
    {
        incrementAge();
        incrementHunger();
        sickEffect();
        if(isAlive()) {
            Location newLocation = getLocation();
            //if(getWeather().equals("Hot") && isNight()) {           
               newLocation = findFood();                
            //}  
            
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
        
    
    /*private  void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }*/
    
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
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Species> newLions)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc);
            newLions.add(young);
            //System.out.println("lion babies");
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    /*private int breed()
    {
        int births = 0;
        if(findSuitableMate() && canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
         // && rand.nextDouble() <= BREEDING_PROBABILITY && findSuitableMate()) {
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
    
    /*private void sickEffect() {
        if(isSick()) {
            System.out.println("lion is sick");
            setFoodLevel(getFoodLevel()/2);
        }
        
    }*/
    
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

