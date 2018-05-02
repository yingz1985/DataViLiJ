/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runningEvents;

/**
 *
 * @author Bagel
 */
import dataprocessors.DataSet;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Platform;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;

/**
 * @author Ritwik Banerjee
 */
public class KMeansClusterer extends Clusterer {

    private DataSet       dataset;
    private List<Point2D> centroids;

    private final int           maxIterations;
    private final int           updateInterval;
    private final AtomicBoolean tocontinue;
    private final int numberOfClusters;
    private boolean proceed;
    private boolean done;
    private boolean interrupt;

    private ApplicationTemplate app;


    public KMeansClusterer(DataSet dataset, int maxIterations, int updateInterval, boolean toContinue,int numberOfClusters,ApplicationTemplate app) {
        super(numberOfClusters);
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(false);
        this.app = app;
        this.numberOfClusters = numberOfClusters;
        done = true;
        proceed = toContinue;
        interrupt = false;
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
        interrupt = true;
        done = true;
    }
    @Override
    public synchronized void run() {
        done = false;
        ((AppUI) app.getUIComponent()).running(true);
        initializeCentroids();
        for (int i = 0; i <= maxIterations&&tocontinue.get()&&!interrupt; i++){//& tocontinue.get()) {
             AtomicInteger k = new AtomicInteger(i);
            try
            {
                Platform.runLater(()->
                    {
                        ((AppUI) app.getUIComponent()).setIteration(k.intValue());
                        
                    });
                Thread.sleep(50);
            }
            catch (InterruptedException ex)
            {
                           // Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            assignLabels();
            recomputeCentroids();
            
            if (i % updateInterval == 0) {
            
                      //  try
               // {
                    if(interrupt)break;
                    Platform.runLater(()->
                    {
                        //((AppUI) app.getUIComponent()).setIteration(k.intValue());
                        dataset.toChartData();
                    });
                    if(!proceed)
                    {
                        try
                        {
                            ((AppUI) app.getUIComponent()).setRun(false);
                            ((AppUI) app.getUIComponent()).setScreenshot(false);
                            this.wait();
                            
                        }
                        catch (InterruptedException ex)
                        {
                            Logger.getLogger(KMeansClusterer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                   // Thread.sleep(500);
                //}
                //catch (InterruptedException ex)
                //{
                //}
            }
            if(!tocontinue.get())
            {
                Platform.runLater(()->
                    {
                        //((AppUI) app.getUIComponent()).setIteration(k.intValue());
                        dataset.toChartData();
                    });
            }
        }
        
        ((AppUI) app.getUIComponent()).running(false);
        ((AppUI) app.getUIComponent()).setRun(false);
        done = true;
        if(proceed)
        {
            ((AppUI) app.getUIComponent()).setScreenshot(false);
        }
        ((AppUI) app.getUIComponent()).isolateChoice(false);
    }

    private synchronized void initializeCentroids() {
        Set<String>  chosen        = new HashSet<>();
        List<String> instanceNames = new ArrayList<>(dataset.getLabels().keySet());
        Random       r             = new Random();
        while (chosen.size() < numberOfClusters) {
            int i = r.nextInt(instanceNames.size());
            while (chosen.contains(instanceNames.get(i)))
                ++i;
            chosen.add(instanceNames.get(i));
        }
        centroids = chosen.stream().map(name -> dataset.getLocations().get(name)).collect(Collectors.toList());
        tocontinue.set(true);
    }

    private synchronized void assignLabels() {
        dataset.getLocations().forEach((instanceName, location) -> {
            double minDistance      = Double.MAX_VALUE;
            int    minDistanceIndex = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = computeDistance(centroids.get(i), location);
                if (distance < minDistance) {
                    minDistance = distance;
                    minDistanceIndex = i;
                }
            }
            dataset.getLabels().put(instanceName, Integer.toString(minDistanceIndex));
        });
    }

    private synchronized void recomputeCentroids() {
        tocontinue.set(false);
        IntStream.range(0, numberOfClusters).forEach(i -> {
            AtomicInteger clusterSize = new AtomicInteger();
            Point2D sum = dataset.getLabels()
                                 .entrySet()
                                 .stream()
                                 .filter(entry -> i == Integer.parseInt(entry.getValue()))
                                 .map(entry -> dataset.getLocations().get(entry.getKey()))
                                 .reduce(new Point2D(0, 0), (p, q) -> {
                                     clusterSize.incrementAndGet();
                                     return new Point2D(p.getX() + q.getX(), p.getY() + q.getY());
                                 });
            Point2D newCentroid = new Point2D(sum.getX() / clusterSize.get(), sum.getY() / clusterSize.get());
            if (!newCentroid.equals(centroids.get(i))) {
                centroids.set(i, newCentroid);
                tocontinue.set(true);
            }
        });
    }
    @Override
    public synchronized void notifyThread()
    {
        this.notifyAll();
    }

    private static double computeDistance(Point2D p, Point2D q) {
        return Math.sqrt(Math.pow(p.getX() - q.getX(), 2) + Math.pow(p.getY() - q.getY(), 2));
    }
    
}