package dataprocessors;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import settings.AppPropertyTypes;
import vilij.components.ErrorDialog;
import vilij.templates.ApplicationTemplate;

/**
 * The data files used by this data visualization applications follow a tab-separated format, where each data point is
 * named, labeled, and has a specific location in the 2-dimensional X-Y plane. This class handles the parsing and
 * processing of such data. It also handles exporting the data to a 2-D plot.
 * <p>
 * A sample file in this format has been provided in the application's <code>resources/data</code> folder.
 *
 * @author Ritwik Banerjee
 * @see XYChart
 */
public final class TSDProcessor {

    public static class InvalidDataNameException extends Exception {

        private static final String NAME_ERROR_MSG = "All data instance names must start with the @ character.";

        public InvalidDataNameException(String name) {
            super(String.format("Invalid name '%s'." + NAME_ERROR_MSG, name));
        }
    }

    private Map<String, String>  dataLabels;
    private Map<String, Point2D> dataPoints;
    private String name;
    private AtomicInteger counter;
    private double averageY = 0;
    private double minX=0;
    private double maxX=0;
    
    public TSDProcessor() {
        dataLabels = new HashMap<>();
        dataPoints = new HashMap<>();
        counter = new AtomicInteger();
    }
    
 
    public int getLineNum()
    {
        return counter.intValue();
    }
    /**
     * Processes the data and populated two {@link Map} objects with the data.
     *
     * @param tsdString the input data provided as a single {@link String}
     * @throws Exception if the input string does not follow the <code>.tsd</code> data format
     */
    public void processString(String tsdString) throws Exception {
        averageY = 0;
        AtomicBoolean hadAnError   = new AtomicBoolean(false);
        StringBuilder errorMessage = new StringBuilder();
        ApplicationTemplate template = new ApplicationTemplate();
        
        counter = new AtomicInteger();
        //counter.getAndIncrement();  //initialized at 1 
        Stream.of(tsdString.split("\n"))
              .map(line -> Arrays.asList(line.split("\t")))
              .forEach((List<String> list) -> {
                  try
                  {
                      counter.incrementAndGet();
                      name  = checkedname(list.get(0));
                      String   label = list.get(1);
                      String[] pair  = list.get(2).split(",");
                      Double x = Double.parseDouble(pair[0]);
                      Double y = Double.parseDouble(pair[1]);
                      Point2D  point = new Point2D(x,y);
                      if(dataLabels.containsKey(name))    //if name already exists in hash, throw new exception
                          throw new IllegalArgumentException();
                      if(counter.get()==1)
                      {
                          minX = x;
                      }
                      if(x<minX)
                              minX = x;
                      if(x>maxX)
                              maxX = x;
                      averageY+=Double.parseDouble(pair[1]);
                      dataLabels.put(name, label);
                      dataPoints.put(name, point);
                      
                  }
                  catch(InvalidDataNameException x)
                  {
                          errorMessage.setLength(0);
                          errorMessage.append(x.getClass().getSimpleName()).append(": ").append(x.getMessage());
                    
                          ErrorDialog dialog = ErrorDialog.getDialog();
                          dialog.show(template.manager.getPropertyValue(
                            AppPropertyTypes.INVALID_DATA_EXCEPTION.name()),
                            InvalidDataNameException.NAME_ERROR_MSG+"\n"+
                            template.manager.getPropertyValue(
                            AppPropertyTypes.ERROR_LINE.name())+counter);
                            
                          
                         
                  }
                  catch(NumberFormatException e)
                 {
                        errorMessage.setLength(0);
                        errorMessage.append(e.getClass().getSimpleName()).append(": ").append(e.getMessage());
                    
                        ErrorDialog dialog = ErrorDialog.getDialog();
                          dialog.show(AppPropertyTypes.NUMBER_FORMAT_EXCEPTION.toString(),
                                  template.manager.getPropertyValue(AppPropertyTypes.NUMBER_FORMAT_EXCEPTION.name())+
                                  "\n"+template.manager.getPropertyValue(
                        AppPropertyTypes.ERROR_LINE.name())+counter);
                         
                   }
                  
                  catch(IllegalArgumentException k)
                  {
                        errorMessage.setLength(0);
                        errorMessage.append(k.getClass().getSimpleName()).append(": ").append(k.getMessage());
                        hadAnError.set(true);
                        ErrorDialog dialog = ErrorDialog.getDialog();
                          dialog.show(AppPropertyTypes.IDENTICAL_NAME_EXCEPTION.toString(),
                                  template.manager.getPropertyValue(AppPropertyTypes.IDENTICAL_NAME_EXCEPTION.name())+
                                  name+"\n"+template.manager.getPropertyValue(
                                AppPropertyTypes.ERROR_LINE.name())+counter+
                                          "\n");
                  }
                  catch (Exception e)
                  {
                      errorMessage.setLength(0);
                      errorMessage.append(e.getClass().getSimpleName()).append(": ").append(e.getMessage());
                    
                    
                      ErrorDialog dialog = ErrorDialog.getDialog();
                      dialog.show(AppPropertyTypes.INVALID_SPACING_ERRORS.toString()
                      ,template.manager.getPropertyValue(AppPropertyTypes.INVALID_SPACING_ERRORS.name())
                      +"\n"+template.manager.getPropertyValue(
                            AppPropertyTypes.ERROR_LINE.name())+counter);
                       
                   }
                 
                   
        
              }
                      
                      
        );
                


        if (errorMessage.length() > 0)
        {
           clear();
           throw new Exception();
        }
        
    }
 
    /**
     * Exports the data to the specified 2-D chart.
     *
     * @param chart the specified chart
     */
    public void toChartData(XYChart<Number, Number> chart) {
        Set<String> labels = new HashSet<>(dataLabels.values());
        Object[] names =  dataPoints.keySet().toArray();
        
        for (String label : labels) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(label);
            dataLabels.entrySet().stream().filter(entry -> entry.getValue().equals(label)).forEach(entry -> {
                Point2D point = dataPoints.get(entry.getKey());
                
                XYChart.Data data = new XYChart.Data<>(point.getX(), point.getY());      
                series.getData().add(data);
                
                
               
                
            });

            chart.getData().add(series);
            for (XYChart.Series<Number, Number> s : chart.getData()) {
            s.getNode().setStyle("   -fx-stroke-width: 0; ");
            s.getNode().setStyle("-fx-stroke: transparent; ");

            } 


        }
            chart.setAnimated(false);
            XYChart.Series<Number,Number> serie = new XYChart.Series<>();
            NumberFormat formatter = new DecimalFormat("#0.0"); 
            averageY = Double.parseDouble(formatter.format(averageY/counter.get()));
            XYChart.Data dataMin = new XYChart.Data<>(minX,averageY);
            //dataMin.getNode().setStyle("-fx-fill:transparent;");
            XYChart.Data dataMax = new XYChart.Data<>(maxX,averageY);
            //dataMax.getNode().setStyle("-fx-fill:transparent;");
            
            
            
            serie.getData().add(dataMin);
            serie.getData().add(dataMax);
            //serie.setName("average:"+averageY*100/100);
              
            serie.setName("averageY:"+formatter.format(averageY));
            
            chart.getData().add(serie);
            
            dataMin.getNode().setStyle("-fx-background-color: transparent;");
            dataMax.getNode().setStyle("-fx-background-color: transparent;");
            
            
            serie.getNode().setStyle("-fx-stroke-width: 1px; ");
            
           /*
            for (XYChart.Series<Number, Number> s : chart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "Number Of Events : " + d.getYValue()));

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }
           
           */ 
           
           
           
           /*
           
           for (Object name : names)
                {
                    if(dataPoints.get((String)name) == point)
                    {
                        Tooltip.install(data.getNode(),new Tooltip((String)name));
                        
                        
                    }
                }
               
           
           
           
           
           */

           
           for (XYChart.Series<Number, Number> s : chart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {

                for(Object name: names)
                {
                    if(dataPoints.get((String)name).getX()==d.getXValue().doubleValue()
                       && dataPoints.get((String)name).getY()==d.getYValue().doubleValue())
                    {
                        Tooltip.install(d.getNode(),new Tooltip((String)name));
                    }
                    
                    
                }
                d.getNode().setCursor(Cursor.CROSSHAIR);
                
                
            }
           }
          
        
    }

    void clear() {
        dataPoints.clear();
        dataLabels.clear();
        counter.set(0);
    }

    private String checkedname(String name) throws InvalidDataNameException  {
        if (!name.startsWith("@"))
        
         throw new InvalidDataNameException(name);
        
        return name;
    }
}
