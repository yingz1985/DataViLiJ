/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runningEvents;

import dataprocessors.DataSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;

/**
 *
 * @author Bagel
 */
public class RandomClusterer extends Clusterer
{
    private Random              RAND;
    private DataSet             dataset;
    private final int           maxIterations;
    private final int           updateInterval;
    private final AtomicBoolean tocontinue;
    private final int numberOfClusters;
    private boolean proceed;
    private boolean done;

    private ApplicationTemplate app;


    public RandomClusterer(DataSet dataset, int maxIterations, int updateInterval, boolean toContinue,int numberOfClusters,ApplicationTemplate app) {
        super(numberOfClusters);
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(toContinue);
        this.app = app;
        this.numberOfClusters = numberOfClusters;
        done = true;
        proceed = true;
        RAND = new Random();
    }

    @Override
    public int getMaxIterations() { return maxIterations; }

    @Override
    public int getUpdateInterval() { return updateInterval; }

    @Override
    public boolean tocontinue() { return tocontinue.get(); }
    @Override
    public boolean done()
    {
        return done;
    }
    
    @Override
    public void stop()
    {
        proceed = false;
        done = true;
    }
    @Override
    public synchronized void run() {
        done = false;
        ((AppUI) app.getUIComponent()).running(true);
        for (int i = 0; i <= maxIterations&&proceed; i++){//& tocontinue.get()) {
             AtomicInteger k = new AtomicInteger(i);
            ((AppUI) app.getUIComponent()).setRun(true);
            ((AppUI) app.getUIComponent()).running(true);
            ((AppUI) app.getUIComponent()).setScreenshot(true);
            
            assignLabels();
             if(!proceed)break;
            try
            {
                Platform.runLater(()->
                    {
                        ((AppUI) app.getUIComponent()).setIteration(k.intValue());
                        
                    });
                Thread.sleep(20);
            }
            catch (InterruptedException ex)
            {
                           // Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (i % updateInterval == 0) {
                     if(!proceed)break;
                    Platform.runLater(()->
                    {
                        //((AppUI) app.getUIComponent()).setIteration(k.intValue());
                        dataset.toChartData();
                    });
                    if(!tocontinue.get())
                    {
                        try
                        {
                            ((AppUI) app.getUIComponent()).setRun(false);
                            ((AppUI) app.getUIComponent()).setScreenshot(false);
                            this.wait();
                        }
                        catch (InterruptedException ex)
                        {
                           // Logger.getLogger(KMeansClusterer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

            }
            if (i > maxIterations * .6 && RAND.nextDouble() < 0.05) {
                //System.out.printf("Iteration number %d: ", i);
                Platform.runLater(()->  dataset.toChartData());
                if(!proceed)
                    break;
                //flush();
            }
        }
        
        ((AppUI) app.getUIComponent()).running(false);
        ((AppUI) app.getUIComponent()).setRun(false);
        done = true;
        if(proceed)
        {
            ((AppUI) app.getUIComponent()).setScreenshot(false);
        }
    }


    private synchronized void assignLabels() {
        dataset.getLabels().forEach((instanceName, label) -> {
            Random k = new Random();
            int i = k.nextInt(numberOfClusters);
            dataset.getLabels().put(instanceName, Integer.toString(i));
        });
    }

    
    @Override
    public synchronized void notifyThread()
    {
        this.notifyAll();
    }

    
    
    
}
