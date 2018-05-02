/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runningEvents;


import dataprocessors.DataSet;
import dataprocessors.TSDProcessor;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;

/**
 * @author Ritwik Banerjee
 */
public class RandomClassifier extends Classifier {

    private static final Random RAND = new Random();

    @SuppressWarnings("FieldCanBeLocal")
    // this mock classifier doesn't actually use the data, but a real classifier will
    private DataSet dataset;
    private final int maxIterations;
    private final int updateInterval;
    private ApplicationTemplate app;
    private boolean proceed;
    private boolean started;
    private boolean done;

    // currently, this value does not change after instantiation
    private final AtomicBoolean tocontinue;

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }
    public void stop()
    {
        proceed = false;
        done = true;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public boolean tocontinue() {
        return tocontinue.get();
    }
    public void setToContinue(boolean cont)
    {
        this.tocontinue.set(cont);
    }
    public boolean alreadyStarted()
    {
        return this.started;
    }
   /* public RandomClassifier(DataSet dataset,AlgorithmContainer container,ApplicationTemplate app)
    {
        this.dataset = dataset;
        this.maxIterations = container.getMaxIterations();
        this.updateInterval = container.getUpdateInterval();
        this.tocontinue = new AtomicBoolean(container.tocontinue());
        this.app = app;
        this.proceed = true;
        done = true;
    }*/
    public RandomClassifier(DataSet dataset,
                            int maxIterations,
                            int updateInterval,
                            boolean tocontinue,int rand,ApplicationTemplate app) {
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(tocontinue);
        done = true;
        this.app = app;
        this.proceed = true;
        done = true;
        
    }
    public boolean done()
    {
        return done;
    }

    @Override
    public void run() {
        done = false;
        ((AppUI) app.getUIComponent()).running(true);
        Platform.runLater(()->dataset.toChartData());
        if(tocontinue.get())
        {
            continuous();
        }
        else
        {
            notContinuous();
        }
        done = true;
        ((AppUI) app.getUIComponent()).running(false);
        ((AppUI) app.getUIComponent()).setRun(false);
        if(proceed)
        {
            ((AppUI) app.getUIComponent()).setScreenshot(false);
        }
        ((AppUI) app.getUIComponent()).isolateChoice(false);
        
    }
    public synchronized void notContinuous()
    {
       
                for (int i = 1; i <= maxIterations&&proceed; i++) {
                    int xCoefficient =  new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
                    int yCoefficient = 10;
                    int constant     = RAND.nextInt(11);
                    
                    ((AppUI) app.getUIComponent()).setRun(true);
                    ((AppUI) app.getUIComponent()).running(true);
                    ((AppUI) app.getUIComponent()).setScreenshot(true);
                     AtomicInteger k = new AtomicInteger(i);
                    Platform.runLater(()->((AppUI) app.getUIComponent()).setIteration(k.intValue()));
            // this is the real output of the classifier
                    output = Arrays.asList(xCoefficient, yCoefficient, constant);
                    try
                        {
                            Thread.sleep(20);
                        }
                        catch (InterruptedException ex)
                        {
                           // Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
            // everything below is just for internal viewing of how the output is changing
            // in the final project, such changes will be dynamically visible in the UI
                    if (i % updateInterval == 0) {
                    //System.out.printf("Iteration number %d: ", i); //
               try{
                    Platform.runLater(()->dataset.updateChart(output));
                    // flush();
                    ((AppUI) app.getUIComponent()).setRun(false);
                    ((AppUI) app.getUIComponent()).setScreenshot(false);
                    this.wait();
                   
               }
               catch (InterruptedException ex)
                {
                  //Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
                }
              
                       
                    
            }
            if (i > maxIterations * .6 && RAND.nextDouble() < 0.05) {
                //System.out.printf("Iteration number %d: ", i);
                Platform.runLater(()->dataset.updateChart(output));
                if(!proceed)
                    break;
                //flush();
                break;
            }
        }
        
            
        
    }
    public synchronized void continuous()
    {
        ((AppUI) app.getUIComponent()).setRun(true);
        ((AppUI) app.getUIComponent()).setScreenshot(true);
        for (int i = 1; i <= maxIterations && tocontinue()&&proceed; i++) {
            int xCoefficient =  new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
            int yCoefficient = 10;
            int constant     = RAND.nextInt(11);
            AtomicInteger k = new AtomicInteger(i);
            // this is the real output of the classifier
            Platform.runLater(()->((AppUI) app.getUIComponent()).setIteration(k.intValue()));

            output = Arrays.asList(xCoefficient, yCoefficient, constant);
            try
            {
                            Thread.sleep(20);
            }
            catch (InterruptedException ex)
            {
                           // Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // everything below is just for internal viewing of how the output is changing
            // in the final project, such changes will be dynamically visible in the UI
            if (i % updateInterval == 0) {
               // System.out.printf("Iteration number %d: ", i); //
                 try
            {
                Platform.runLater(()->dataset.updateChart(output));
                Thread.sleep(800);
                if(!proceed)
                   break;
                           
            }
            
            catch (InterruptedException ex)
            {
                Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                   // flush();
            }
            if (i > maxIterations * .6 && RAND.nextDouble() < 0.05) {
                //System.out.printf("Iteration number %d: ", i);
                   try
            {
                Platform.runLater(()->dataset.updateChart(output));
                Thread.sleep(800);
                if(!proceed)
                    break;
                
            }
            
            catch (InterruptedException ex)
            {
                //Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }
               // flush();
                break;
            }
        }
        
    }
    public synchronized void notifyThread()
    {
        this.notifyAll();
    }

    // for internal viewing only
    protected void flush() {
        System.out.printf("%d\t%d\t%d%n", output.get(0), output.get(1), output.get(2));
    }

}