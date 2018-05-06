/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocessors;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

/**
 * This class specifies how an algorithm will expect the dataset to be. It is
 * provided as a rudimentary structure only, and does not include many of the
 * sanity checks and other requirements of the use cases. As such, you can
 * completely write your own class to represent a set of data instances as long
 * as the algorithm can read from and write into two {@link java.util.Map}
 * objects representing the name-to-label map and the name-to-location (i.e.,
 * the x,y values) map. These two are the {@link DataSet#labels} and
 * {@link DataSet#locations} maps in this class.
 *
 * @author Ritwik Banerjee
 */
public class DataSet {



    private static Point2D locationOf(String locationString) {
        String[] coordinateStrings = locationString.trim().split(",");
        return new Point2D(Double.parseDouble(coordinateStrings[0]), Double.parseDouble(coordinateStrings[1]));
    }

    private Map<String, String>  labels;
    private Map<String, Point2D> locations;
    private XYChart<Number, Number> chart;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private AtomicInteger counter;

    /** Creates an empty dataset. */
    public DataSet() {
        labels = new HashMap<>();
        locations = new HashMap<>();
        counter= new AtomicInteger(0);
    }

    public Map<String, String> getLabels()     { return labels; }

    public Map<String, Point2D> getLocations() { return locations; }

    public void updateLabel(String instanceName, String newlabel) {
        if (labels.get(instanceName) == null)
            throw new NoSuchElementException();
        labels.put(instanceName, newlabel);
    }

    private void addInstance(String tsdLine)  {
        String[] arr = tsdLine.split("\t");
        labels.put(arr[0], arr[1]);
        locations.put(arr[0], locationOf(arr[2]));
    }

    public static DataSet fromTSDFile(Path tsdFilePath) throws IOException {
        DataSet dataset = new DataSet();
        Files.lines(tsdFilePath).forEach(line -> {

                dataset.addInstance(line);
         
        });
        return dataset;
    }
    public void fromTSDFile(String tsdString) //throws IOException {
    {
        DataSet dataset = new DataSet();
        counter = new AtomicInteger(0);
        Stream.of(tsdString.split("\n"))
              .map(line -> Arrays.asList(line.split("\t")))
              .forEach((List<String> list) -> {
                  
                      counter.getAndIncrement();
                      
                      String name  = list.get(0);

                      String   label = list.get(1);

                      String[] pair  = list.get(2).split(",");
                      Double x = Double.parseDouble(pair[0]);
                      Double y = Double.parseDouble(pair[1]);
                      //Double y = Double.parseDouble(pair[1]);
                      if(counter.intValue()==1)
                      {
                          dataset.setMinX(x);
                          dataset.setMaxX(x);
                          dataset.setMinY(y);
                          dataset.setMaxY(y);
                          minX = x;
                          maxX = x;
                          minY = y;
                          maxY = y;
     
                      }
                      if(x<minX)
                             
                      {
                          minX = x;
                         dataset.setMinX(x);
                      }
                      if(x>maxX)
                      {
                           dataset.setMaxX(x);
                           maxX = x;
                      }
                             
                      if(y<minY)
                      {
                              minY = y;
                              dataset.setMinY(y);
                      }
                      if(y>maxY)
                      {
                          dataset.setMaxY(y);
                          maxY = y;
                      }
                              
                      //System.out.println("x="+x);
                      //System.out.println("maxX="+dataset.getMaxX());
                      
                      Point2D  point = locationOf(list.get(2));

                      
                      labels.put(name, label);
                      locations.put(name, point);

                  
              });

    }
    public void setChart(XYChart<Number, Number> chart)
    {
        this.chart = chart;
        
    }
    public XYChart<Number, Number> getChart()
    {
        return chart;
    }
    public void setMinX(double x)
    {
        this.minX=x;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
        /**
     * Exports the data to the specified 2-D chart.
     *
     * @param chart the specified chart
     */
    public void toChartData() {
        //this.chart = chart;
        chart.getData().clear();
        Set<String> labels = new HashSet<>(getLabels().values());
        Object[] names =  getLocations().keySet().toArray();
        
        for (String label : labels) 
        {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(label);
            getLabels().entrySet().stream().filter(entry -> entry.getValue().equals(label)).forEach(entry -> {
                Point2D point = getLocations().get(entry.getKey());
                
                XYChart.Data data = new XYChart.Data<>(point.getX(), point.getY());      
                series.getData().add(data);
               
                
            });

            getChart().getData().add(series);
            for (XYChart.Series<Number, Number> s : getChart().getData()) {
            s.getNode().setStyle("-fx-stroke-width: 0; ");
            s.getNode().setStyle("-fx-stroke: transparent; ");

            } 


        }
            getChart().setAnimated(false);
            getChart().getData().add(new XYChart.Series<Number,Number>());
           
           for (XYChart.Series<Number, Number> s : getChart().getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {

                for(Object name: names)
                {
                    if(getLocations().get((String)name).getX()==d.getXValue().doubleValue()
                       && getLocations().get((String)name).getY()==d.getYValue().doubleValue())
                    {
                        Tooltip.install(d.getNode(),new Tooltip((String)name));
                    }
                    
                    
                }
                d.getNode().setCursor(Cursor.CROSSHAIR);
                
                
            }
            
           }
           

            
           /* getChart().getXAxis().setAutoRanging(false);
            getChart().getYAxis().setAutoRanging(false);
        //System.out.print(minX+" "+maxX+" "+minY+" "+maxY);
            double deltaX = maxX-minX;
            double deltaY = maxY-minY;
            double lowerY = deltaY>1?minY-deltaY*0.05:minY*0.1;
            double upperY = deltaY>1?maxY+deltaY*0.05:maxY*0.1;
            if(deltaY==0)
            {
                lowerY = -0.5;
                upperY = 0.5;
            }
            double lowerX = deltaX>1?minX-deltaX*0.05:minX*0.1;
            double upperX = deltaX>1?minX+deltaX*0.05:maxX*0.1;
            if(deltaX == 0)
            {
                lowerX = -0.5;
                upperX = 0.5;
            }
            
            ((NumberAxis)getChart().getXAxis()).setLowerBound(lowerX);
            ((NumberAxis)getChart().getXAxis()).setUpperBound(upperX);
            ((NumberAxis)getChart().getYAxis()).setLowerBound(lowerY);
            ((NumberAxis)getChart().getYAxis()).setUpperBound(upperY);
           */
            getChart().getXAxis().setAutoRanging(false);
            getChart().getYAxis().setAutoRanging(false);
            
           double deltaY = getMaxY()-getMinY();
            double deltaX = getMaxX()-getMinX();
            if(deltaX==0) 
            {
                deltaX = getMinX()*0.1;
                setMinX(getMinX()*0.1-1);
                setMaxX(getMaxX()*0.1+1);
            }
            if(deltaY==0)
            {
                deltaY = getMinY()*0.1;
                setMinY(getMinY()*0.1-1);
                setMaxY(getMaxY()*0.1+1);
            }

            getChart().getXAxis().setAutoRanging(false);
            getChart().getYAxis().setAutoRanging(false);
            ((NumberAxis)getChart().getXAxis()).setLowerBound(getMinX()-Math.abs(deltaX/10));
            ((NumberAxis)getChart().getXAxis()).setUpperBound(getMaxX()+Math.abs(deltaX/10));
            ((NumberAxis)getChart().getYAxis()).setLowerBound(getMinY()-Math.abs(deltaY/10));
            ((NumberAxis)getChart().getYAxis()).setUpperBound(getMaxY()+Math.abs(deltaY/10));

             
    }
    public double getMinX()
    {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }
    
    
    public synchronized void updateChart(List<Integer> data)
    {
            if(chart.getData().isEmpty())
                return;
        //synchronized
        //chart.getData().get(chart.getData().size()-1);//get the last series and replace it 
        //chart.getData().clear();
        //toChartData(chart);

            
            Double miY = ((-minX*data.get(0))-data.get(2))/data.get(1);
            Double maY = ((-maxX*data.get(0))-data.get(2))/data.get(1);
           // System.out.println("("+minX+","+minY+")"+"  ("+maxX+","+maxY+")");
            XYChart.Data dataMin = new XYChart.Data<>(minX,miY);
            XYChart.Data dataMax = new XYChart.Data<>(maxX,maY);
            XYChart.Series<Number,Number> serie = new XYChart.Series<>();

            //dataMin.getNode().setStyle("-fx-background-color: transparent;");
            //dataMax.getNode().setStyle("-fx-background-color: transparent;");
            
            //serie.getNode().setStyle("-fx-stroke-width: 1px; ");
            //serie.setName("average:"+averageY*100/100);
            serie.getData().add(dataMin);
            serie.getData().add(dataMax);
            //serie.setName("average:"+averageY*100/100);
            
            //chart.getData().add(serie);
            //Platform.runLater(()->{
  
                 //chart.getData().set(2, serie);
             
                 chart.getData().get(chart.getData().size()-1).setData(
                FXCollections.observableArrayList(dataMin,dataMax));
         

            
            dataMin.getNode().setStyle("-fx-background-color: transparent;");
            dataMax.getNode().setStyle("-fx-background-color: transparent;");
            
            chart.getData().get(chart.getData().size()-1).getNode().setStyle("-fx-stroke-width: 1px; ");
            //serie.getNode().setStyle("-fx-stroke-width: 1px; ");


            
            double miny = getMinY()>miY?miY:getMinY();
            double maxy = getMaxY()>maY?getMaxY():maY;
            double deltaY = maxy-miny;
            double deltaX = getMaxX()-getMinX();
                        if(deltaX==0) 
            {
                deltaX = getMinX()*0.1;
                setMinX(getMinX()*0.1-1);
                setMaxX(getMaxX()*0.1+1);
            }
            if(deltaY==0)
            {
                deltaY = miny*0.1;
                setMinY(miny*0.1-1);
                setMaxY(miny*0.1+1);
            }


            getChart().getXAxis().setAutoRanging(false);
            getChart().getYAxis().setAutoRanging(false);
            ((NumberAxis)getChart().getXAxis()).setLowerBound(getMinX()-Math.abs(deltaX/10));
            ((NumberAxis)getChart().getXAxis()).setUpperBound(getMaxX()+Math.abs(deltaX/10));
            ((NumberAxis)getChart().getYAxis()).setLowerBound(miny-Math.abs(deltaY/10));
            ((NumberAxis)getChart().getYAxis()).setUpperBound(maxy+Math.abs(deltaY/10));
    }
    
}