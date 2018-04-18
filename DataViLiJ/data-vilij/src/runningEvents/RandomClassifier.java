/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runningEvents;


import dataprocessors.TSDProcessor;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * @author Ritwik Banerjee
 */
public class RandomClassifier extends Classifier {

    private static final Random RAND = new Random();

    @SuppressWarnings("FieldCanBeLocal")
    // this mock classifier doesn't actually use the data, but a real classifier will
    private TSDProcessor dataset;
    private final int maxIterations;
    private final int updateInterval;

    // currently, this value does not change after instantiation
    private final AtomicBoolean tocontinue;

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public boolean tocontinue() {
        return tocontinue.get();
    }
    public RandomClassifier(TSDProcessor dataset,AlgorithmContainer container)
    {
        this.dataset = dataset;
        this.maxIterations = container.getMaxIterations();
        this.updateInterval = container.getUpdateInterval();
        this.tocontinue = new AtomicBoolean(container.tocontinue());
    }
    public RandomClassifier(TSDProcessor dataset,
                            int maxIterations,
                            int updateInterval,
                            boolean tocontinue) {
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(tocontinue);
    }

    @Override
    public void run() {
        
        for (int i = 1; i <= maxIterations && tocontinue(); i++) {
            int xCoefficient = new Double(RAND.nextDouble() * 100).intValue();
            int yCoefficient = new Double(RAND.nextDouble() * 100).intValue();
            int constant     = new Double(RAND.nextDouble() * 100).intValue();

            // this is the real output of the classifier
            output = Arrays.asList(xCoefficient, yCoefficient, constant);
            
            
           
            
            // everything below is just for internal viewing of how the output is changing
            // in the final project, such changes will be dynamically visible in the UI
            if (i % updateInterval == 0) {
                System.out.printf("Iteration number %d: ", i); //
                 try
            {
                Platform.runLater(()->dataset.updateChart(output));
                Thread.sleep(800);
                
            }
            
            catch (InterruptedException ex)
            {
                Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                    flush();
            }
            if (i > maxIterations * .6 && RAND.nextDouble() < 0.05) {
                System.out.printf("Iteration number %d: ", i);
                   try
            {
                Platform.runLater(()->dataset.updateChart(output));
                Thread.sleep(800);
                
            }
            
            catch (InterruptedException ex)
            {
                Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
                flush();
                break;
            }
        }
        

    }

    // for internal viewing only
    protected void flush() {
        System.out.printf("%d\t%d\t%d%n", output.get(0), output.get(1), output.get(2));
    }

    /** A placeholder main method to just make sure this code runs smoothly */
    public static void main(String... args) throws IOException {
        //TSDProcessor dataset    = DataSet.fromTSDFile(Paths.get("/path/to/some-data.tsd"));
       // RandomClassifier classifier = new RandomClassifier(dataset, 100, 5, true);
       // classifier.run(); // no multithreading yet
    }
}