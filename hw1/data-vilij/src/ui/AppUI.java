package ui;
//@author Ying Zhang
import actions.AppActions;
import dataprocessors.AppData;
import static java.io.File.separator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vilij.propertymanager.PropertyManager;
import settings.AppPropertyTypes;
import static vilij.settings.PropertyTypes.GUI_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.ICONS_RESOURCE_PATH;
import vilij.templates.ApplicationTemplate;
import vilij.templates.UITemplate;

/**
 * This is the application's user interface implementation.
 *
 * @author Ritwik Banerjee
 */
public final class AppUI extends UITemplate {

    /** The application to which this class of actions belongs. */
    ApplicationTemplate applicationTemplate;

    private Button                       scrnshotButton; // toolbar button to take a screenshot of the data
    private LineChart<Number, Number> chart;          // the chart where data will be displayed
    private Button                       displayButton;  // workspace button to display data on the chart
    private TextArea                     textArea;       // text area for new data input
    private boolean                      hasNewText = true ;     // whether or not the text area has any new data since last display
    private CheckBox                     checkbox;
    private boolean                      loadedData = false;
    private AppData                      processor;
    private String                       actualText = "";//actual text is used when loading from file 
    public LineChart<Number, Number> getChart() { return chart; }

    public void LoadedData()
    {
        loadedData = true;
    }
    public void setNewText()
    {
        hasNewText = false;
        //if text loaded 
    }
    public void setActualText(String s)
    {
        this.actualText = s;
    }
    public void setProcessor(AppData processor)
    {
        this.processor = processor;
    }
    public AppUI(Stage primaryStage, ApplicationTemplate applicationTemplate) {
        super(primaryStage, applicationTemplate);
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    protected void setResourcePaths(ApplicationTemplate applicationTemplate) {
        super.setResourcePaths(applicationTemplate);
    }

    @Override
    protected void setToolBar(ApplicationTemplate applicationTemplate) {
        // TODO for homework 1
        super.setToolBar(applicationTemplate);
        PropertyManager manager = applicationTemplate.manager;
        String screenshotPath = String.join(
            separator, "/" + String.join(separator,
                                             manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                             manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            manager.getPropertyValue(AppPropertyTypes.SCREENSHOT_ICON.name()));
        //screenshotPath = "/gui/icons/screenshot.png";
        //a string rep of the screenShotPath frmo above 
        //System.out.print(screenshotPath);
        
        scrnshotButton = setToolbarButton(screenshotPath,AppPropertyTypes.SCREENSHOT_TOOLTIP.name(),true);
        toolBar = new ToolBar(newButton, saveButton, loadButton, printButton, exitButton,scrnshotButton);
        
       
    }

    @Override
    protected void setToolbarHandlers(ApplicationTemplate applicationTemplate) {
        AppActions s = new AppActions(applicationTemplate);
        applicationTemplate.setActionComponent(new AppActions(applicationTemplate));
        newButton.setOnAction(e -> applicationTemplate.getActionComponent().handleNewRequest());
        saveButton.setOnAction(e -> applicationTemplate.getActionComponent().handleSaveRequest());
        loadButton.setOnAction(e -> applicationTemplate.getActionComponent().handleLoadRequest());
        exitButton.setOnAction(e -> applicationTemplate.getActionComponent().handleExitRequest());
        printButton.setOnAction(e -> applicationTemplate.getActionComponent().handlePrintRequest());
        scrnshotButton.setOnAction(e -> s.handleScreenshotRequest());

    }

    @Override
    public void initialize() {
        layout();
        setWorkspaceActions();
    }
    public void resetSaveButton()
    {
        saveButton.setDisable(true);
    }
    @Override
    public void clear() {
        // TODO for homework 1
        
        newButton.setDisable(true);
        saveButton.setDisable(true);
        chart.getData().clear();
        textArea.clear();
        loadedData = false;
        actualText = "";
        scrnshotButton.setDisable(true);
        
    }

    private void layout() {
        // TODO for homework 1
        super.setWindow(applicationTemplate);
        VBox dataField = new VBox(5);
        dataField.setPadding(new Insets(10,10,10,10));
        Label text = new Label(applicationTemplate.manager.getPropertyValue
        (AppPropertyTypes.TEXT_AREA.name()),textArea);
        checkbox = new CheckBox(applicationTemplate.manager.
                getPropertyValue(AppPropertyTypes.READ_ONLY.name()));
        
        textArea = new TextArea();
        displayButton = new Button(applicationTemplate.manager.getPropertyValue
        (AppPropertyTypes.DISPLAY.name()));
        dataField.getChildren().addAll
        (text,textArea,displayButton,checkbox);
        textArea.setMaxWidth(300);

        VBox chartArea = new VBox(5);
        chartArea.setPadding(new Insets(10,10,10,10));
        Label visual = new Label(
          applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.DATA_VISUALIZATION.name()),chart);
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart<Number,Number>(xAxis,yAxis);
        
        chartArea.getChildren().addAll(visual,chart);
        chartArea.setAlignment(Pos.TOP_CENTER);
        
        HBox workSpace = new HBox(5);
        workSpace.getChildren().addAll(dataField,chartArea);
        
        VBox wholeScene = new VBox();
        wholeScene.getChildren().addAll(toolBar,workSpace);
        Scene current = new Scene(wholeScene,1000,600);
        current.getStylesheets().add("ui/chart.css");
        primaryStage.setScene(current);
    
    }

    private void setWorkspaceActions() {
        // TODO for homework 1
        textArea.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable,
            String oldValue, String newValue) 
            {
                
                //actualText = textArea.getText();
                
                
                hasNewText = true;
                if(!textArea.getText().isEmpty())
                {
                    newButton.setDisable(false);
                    saveButton.setDisable(false);

                }
                else
                {
                    newButton.setDisable(true);
                    saveButton.setDisable(true);
                }
            }});
        
                
        displayButton.setOnAction((ActionEvent event)->
        {   
            
           
            if(!loadedData || actualText.isEmpty() || processor.lineNum()<=10) 
            {
                 actualText=textArea.getText();
                 processor =  new AppData(applicationTemplate);
            }
            
            if((!actualText.isEmpty() && hasNewText))
            {
                
                try{
                    //chart.getData().clear();
                        if(loadedData && processor.lineNum()>10)
                    { 
                        
                        String[] result = actualText.split("\n");
                        //System.out.println(textArea.getText()+"2");
                        String tempText = "";
                        tempText+= textArea.getText();
                        for(int i=10;i<result.length;i++)
                        {
                            tempText+=result[i]+"\n";
                        }
                        
                        actualText = tempText;
                        result = actualText.split("\n");
                        tempText="";
                        if(result.length>10){
                            for(int i = 0;i<10;i++)
                                tempText+=result[i]+"\n";
                        }
                        
                        else
                        {
                             for(int i = 0;i<result.length;i++)
                                 tempText+=result[i]+"\n";
                        }
                        textArea.setText(tempText);
                        
                        
                    }
                       
                    
                    processor.clear();
                    chart.getData().clear();
                    
                    processor.loadData(actualText);
                    
                    //TSDProcessor tryProcess = new TSDProcessor();
                    //tryProcess.processString(actualText);
                    //if text can be processed without error, proceed 
                    //data.loadData(textArea.getText());
                    
                    processor.displayData();
                    //chart.lookup(".default.chart-series-line").setStyle("-fxstroke: transparent");
                    
                    //data.displayData();
                    newButton.setDisable(false);
                    saveButton.setDisable(false);
                    hasNewText = false;
                    //if no exception is caught, chart is displayed, and therefore 
                    scrnshotButton.setDisable(false);
                }
                catch(Exception x)
                {
                    //System.out.print(x.getLocalizedMessage());
                }
            
            } 

            
        }
            );
        checkbox.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true)
                {
                    textArea.setDisable(true);
                }
                else
                {
                    textArea.setDisable(false);
                }
            
            }
            
        });
        
        
        	
    }

    public TextArea getTextArea() {
        return textArea; //To change body of generated methods, choose Tools | Templates.
    }
    public boolean newText(){
        return hasNewText;
    }
    public String returnActualText()
    {
        return actualText;
    }
}
/*
-fx-background-insets: 0, 2;
    -fx-background-radius: 5px;
    -fx-padding: 5px;
*/