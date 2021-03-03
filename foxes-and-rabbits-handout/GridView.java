import java.awt.*;
import java.awt.event.*;
//import javafx.event.ActionEvent;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location representing its contents.
 * Colors for each type of species can be defined using the setColor method.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2016.03.18
 */
public class GridView extends JFrame implements SimulatorView
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String TIME_PREFIX = "Time: ";
    private final String WEATHER_PREFIX = "Weather: ";
    private JLabel stepLabel, population, timeLabel, weatherLabel;
    private FieldView fieldView;
    
    // A map for storing colors for participants in the simulation
    private Map<Class<?>, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;
    
    private Scanner userStep;
    
    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public GridView(int height, int width)
    {
        stats = new FieldStats();
        colors = new HashMap<>();        
        userStep = new Scanner(System.in);
        setTitle("Wildlife Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        timeLabel = new JLabel(TIME_PREFIX, JLabel.LEFT);
        weatherLabel = new JLabel(TIME_PREFIX, JLabel.CENTER);
        setLocation(20, 50);
       
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        contents.add(weatherLabel, BorderLayout.EAST);
        contents.add(timeLabel, BorderLayout.WEST);
        pack();
        setVisible(true);
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class<?> speciesClass, Color color)
    {
        colors.put(speciesClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class<?> speciesClass)
    {
        Color col = colors.get(speciesClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    public void showStatus(int step, Field field) 
    {
        
    }
    
    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field, String time, String weather)
    {
        if(!isVisible()) {
            setVisible(true);
        }
            
        stepLabel.setText(STEP_PREFIX + step);
        timeLabel.setText(TIME_PREFIX + time);
        weatherLabel.setText(WEATHER_PREFIX + weather);
        stats.reset();
        
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object species = field.getObjectAt(row, col);
                if(species != null) {
                    Class<?> cls = species.getClass();
                    stats.incrementCount(cls);
                    fieldView.drawMark(col, row, getColor(cls));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
    /**
     * Prepare for a new run.
     */
    public void reset()
    {
        stats.reset();
    }
    
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
    
    private int stepButton()
    {
        System.out.println("How many steps");
        int wantedSteps = userStep.nextInt();
        return wantedSteps;
    }
}
