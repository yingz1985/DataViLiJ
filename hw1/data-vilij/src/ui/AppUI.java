package ui;

import actions.AppActions;
import dataprocessors.AppData;
import static java.io.File.separator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
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
    private ScatterChart<Number, Number> chart;          // the chart where data will be displayed
    private Button                       displayButton;  // workspace button to display data on the chart
    private TextArea                     textArea;       // text area for new data input
    private boolean                      hasNewText = true ;     // whether or not the text area has any new data since last display

    public ScatterChart<Number, Number> getChart() { return chart; }

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
        applicationTemplate.setActionComponent(new AppActions(applicationTemplate));
        newButton.setOnAction(e -> applicationTemplate.getActionComponent().handleNewRequest());
        saveButton.setOnAction(e -> applicationTemplate.getActionComponent().handleSaveRequest());
        loadButton.setOnAction(e -> applicationTemplate.getActionComponent().handleLoadRequest());
        exitButton.setOnAction(e -> applicationTemplate.getActionComponent().handleExitRequest());
        printButton.setOnAction(e -> applicationTemplate.getActionComponent().handlePrintRequest());
    }

    @Override
    public void initialize() {
        layout();
        setWorkspaceActions();
    }

    @Override
    public void clear() {
        // TODO for homework 1
        
        newButton.setDisable(true);
        saveButton.setDisable(true);
        chart.getData().clear();
        textArea.clear();
    }

    private void layout() {
        // TODO for homework 1
        super.setWindow(applicationTemplate);
        VBox dataField = new VBox(5);
        dataField.setPadding(new Insets(10,10,10,10));
        Label text = new Label(applicationTemplate.manager.getPropertyValue
        (AppPropertyTypes.TEXT_AREA.name()),textArea);
        textArea = new TextArea();
        displayButton = new Button(applicationTemplate.manager.getPropertyValue
        (AppPropertyTypes.DISPLAY.name()));
        dataField.getChildren().addAll
        (text,textArea,displayButton);
        textArea.setMaxWidth(300);

        VBox chartArea = new VBox(5);
        chartArea.setPadding(new Insets(10,10,10,10));
        Label visual = new Label(
          applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.DATA_VISUALIZATION.name()),chart);
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new ScatterChart<Number,Number>(xAxis,yAxis);
        chartArea.getChildren().addAll(visual,chart);
        chartArea.setAlignment(Pos.TOP_CENTER);
        
        HBox workSpace = new HBox(5);
        workSpace.getChildren().addAll(dataField,chartArea);
        
        VBox wholeScene = new VBox();
        wholeScene.getChildren().addAll(toolBar,workSpace);
        
        primaryStage.setScene(new Scene(wholeScene,1000,600));
    
    }

    private void setWorkspaceActions() {
        // TODO for homework 1
        textArea.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable,
            String oldValue, String newValue) 
            {
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

            
            AppData data = new AppData(applicationTemplate);
            if(!textArea.getText().isEmpty() && hasNewText)
            {
                
                try{
                    //chart.getData().clear();
                    data.loadData(textArea.getText());
                    //data.displayData();
                    chart.getData().clear();
                    data.displayData();
                    newButton.setDisable(false);
                    saveButton.setDisable(false);
                    hasNewText = false;
                }
                catch(Exception x)
                {
                    //chart.getData().clear();
                }
            } 

            
        }
            );
        	
    }

    public String getText() {
        return textArea.getText(); //To change body of generated methods, choose Tools | Templates.
    }
}
