import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing Grass,Possums,Zebra,Lions and Wolves.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a Wolf will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.35;
    // The probability that a Possums will be created in any given grid position.
    private static final double POSSUM_CREATION_PROBABILITY = 0.12;
    private static final double ZEBRA_CREATION_PROBABILITY = 0.15;
    private static final double WOLF_CREATION_PROBABILITY = 0.07;
    private static final double LION_CREATION_PROBABILITY = 0.11;
    
    // List of Speciess in the field.
    private List<Species> Species;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    private Clock clock;
    private Weather weather;
    private  List<SimulatorView> views;
    /**
     * Constructs a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Creates a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        Species = new ArrayList<>();
        field = new Field(depth, width);
        clock = new Clock();
        weather = new Weather();
        weather.setWeather();
        views = new ArrayList<>();
         
        // Create a view of the state of each location in the field.
        SimulatorView view = new GridView(depth, width);        
        view.setColor(Possum.class, Color.RED);
        view.setColor(Wolf.class, Color.BLACK);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Zebra.class, Color.YELLOW);
        view.setColor(Lion.class, Color.PINK);
        views.add(view);
        
        view = new GraphView(1000,500,1000);
        view.setColor(Possum.class, Color.RED);
        view.setColor(Wolf.class, Color.BLACK);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Zebra.class, Color.YELLOW);
        view.setColor(Lion.class, Color.PINK);
        views.add(view);
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Runs the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(500);
    }
    
    /**
     * Runs the simulation from its current state for the given number of steps.
     * Stops before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && views.get(0).isViable(field); step++) {
             simulateOneStep();
             //delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Runs the simulation from its current state for a single step.
     * Iterates over the whole field updating the state of each
     * Wolf and Possums.
     */
    public void simulateOneStep()
    {
        step++;
        clock.increaseTime();
        if(step%10==0) {
            weather.setWeather();
        }
        
        // Provide space for newborn Species.
        List<Species> newSpecies = new ArrayList<>();        
        // Let all Possumss act.
        for(Iterator<Species> it = Species.iterator(); it.hasNext(); ) {
            Species Species = it.next();
            
              Species.act(newSpecies);
            
            
            if(! Species.isAlive()) {
                it.remove();
            }
        }
               
        
        
        // Add the newly born Wolfes and Possumss to the main lists.
        Species.addAll(newSpecies);
        

        updateViews();//,clock.getTime(), clock.getDays());
    }
        
    /**
     * Resets the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        clock.timeReset();
        weather.setWeather();
        Species.clear();        
        for (SimulatorView view : views) {
            
            view.reset();
        }
        
        populate();
        
        updateViews();
        
        // Show the starting state in the view.
        //view.showStatus(step, field);//, clock.getTime(), clock.getDays());
    }
    
    /**
     * Updates all existing views.
     */
    private void updateViews()
    {
        for (SimulatorView view : views) {
            SimulatorView viewed;
            if(view instanceof GridView) {
              view.showStatus(step, field, clock.status(), weather.getWeather());
            }
            view.showStatus(step, field);
        }
    }
    
    /**
     * Randomly populates the field with Grass,Possums,Zebra,Wolves, and Lions.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(true, field, location);
                    Species.add(wolf);
                }
                else if(rand.nextDouble() <= POSSUM_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Possum Possums = new Possum(true, field, location);
                    Species.add(Possums);
                }
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Grass Grass = new Grass(true, field, location);
                    Species.add(Grass);
                }
                else if(rand.nextDouble() <= ZEBRA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Zebra Zebra = new Zebra(true, field, location);
                    Species.add(Zebra);
                }
                else if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Lion Lion = new Lion(true, field, location);
                    Species.add(Lion);
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
      * Pauses for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
