package ui;
//@author Ying Zhang
import actions.AppActions;
import dataprocessors.AppData;
import static java.io.File.separator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private LineChart<Number, Number>    chart;          // the chart where data will be displayed
    private Button                       runButton;  // workspace button to display data on the chart
    private TextArea                     textArea;       // text area for new data input
    private boolean                      hasNewText = true ;     // whether or not the text area has any new data since last display
    private Button                       done;
    private boolean                      loadedData = false;
    private AppData                      processor;
    private String                       actualText = "";//actual text is used when loading from file 
    private ImageView                    emptyChart;
    private BorderPane                   workSpace;
    private BorderPane                   leftPane = new BorderPane();
    private Label                        description;
    private Button                       config;
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
        newButton.setText("New");
        loadButton.setText("Load");
        saveButton.setText("Save");
        
        PropertyManager manager = applicationTemplate.manager;
        String screenshotPath = String.join(
            separator, "/" + String.join(separator,
                                             manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                             manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            manager.getPropertyValue(AppPropertyTypes.SCREENSHOT_ICON.name()));
        //screenshotPath = "/gui/icons/screenshot.png";
        //a string rep of the screenShotPath frmo above 
        //System.out.print(screenshotPath);
        newButton.setDisable(false);
        scrnshotButton = setToolbarButton(screenshotPath,AppPropertyTypes.SCREENSHOT_TOOLTIP.name(),true);
        toolBar = new ToolBar(newButton,loadButton, saveButton,scrnshotButton,exitButton);
        
       
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
        actualText = "";
        textArea.clear();
        loadedData = false;
        scrnshotButton.setDisable(true);
        workSpace.setRight(emptyChart);
        leftPane = new BorderPane();
        
    }

    private void layout() {
        // TODO for homework 1
        super.setWindow(applicationTemplate);
        emptyChart = new ImageView(new Image(String.join(
            separator, "/" + String.join(separator,
            applicationTemplate.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
            applicationTemplate.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EMPTY_CHART_IMAGE.name()))));
        VBox dataField = new VBox(5);
        dataField.setPadding(new Insets(10,10,10,10));
       /* Label text = new Label(applicationTemplate.manager.getPropertyValue
        (AppPropertyTypes.TEXT_AREA.name()),textArea);
        checkbox = new CheckBox(applicationTemplate.manager.
                getPropertyValue(AppPropertyTypes.READ_ONLY.name()));
        */
        
        textArea = new TextArea();
        /*displayButton = new Button(applicationTemplate.manager.getPropertyValue
        (AppPropertyTypes.DISPLAY.name()));*/
        dataField.getChildren().addAll
        (textArea);
        textArea.setMaxWidth(300);


        description = new Label();
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart<Number,Number>(xAxis,yAxis);

        workSpace = new BorderPane();
        workSpace.setPadding(new Insets(10,10,10,10));
        workSpace.setRight(emptyChart);
       // workSpace.setRight(chart);
        chart.setMinSize(600, 540);

        //setLeftPane();
        
        VBox wholeScene = new VBox();
        wholeScene.getChildren().addAll(toolBar,workSpace);
        Scene current = new Scene(wholeScene,1000,600);
        primaryStage.setScene(current);
        primaryStage.setTitle(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.DATA_VISUALIZATION.name()));
        current.getStylesheets().add("css/chart.css");
       
        
    }
    public void setDescription(TextArea description)
    {
        Pane pane = new Pane();
        pane.setMinSize(300, 170);
        description.setMaxHeight(140);
        description.setMaxWidth(330);
        description.getStylesheets().add("css/GridPane.css");
        description.setEditable(false);
        
        pane.getChildren().add(description);
        leftPane.setCenter(pane);
        
    }
    public void setLeftPane()
    {
        //leftPane = new BorderPane();
        VBox text = new VBox(5);
        text.setMinSize(300, 210);
        if(!loadedData)
        {
            done = new Button(AppPropertyTypes.EDIT.name());
            done.setStyle(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EDIT.name()));
            
            done.setOnAction(e->
            {
                if(done.getText().equals(AppPropertyTypes.DONE.name()))
                {
                     done.setText(AppPropertyTypes.EDIT.name());
                     done.setStyle(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EDIT.name()));
                     textArea.setDisable(false);
                }
                else
                {
                    done.setText(AppPropertyTypes.DONE.name());
                    done.setStyle(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DONE.name()));
                    textArea.setDisable(true);
                }
                 
            });
            text.getChildren().addAll(textArea,done);
            description.setMinSize(300, 170);
            leftPane.setCenter(description);
        }
        else
        {
            text.getChildren().add(textArea);
            textArea.setDisable(true);
        }
        leftPane.setTop(text);
        
        leftPane.setBottom(algorithmPane());
        //leftPane.setBottom(choicePane());
        workSpace.setLeft(leftPane);
        
    }
    public BorderPane choicePane(String chosen)
    {
        BorderPane pane = new BorderPane();
        
        ToggleGroup group = new ToggleGroup();
        RadioButton cluster = new RadioButton(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.RANDOM_CLUSTER.name()));
        RadioButton clas = new RadioButton(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.RANDOM_CLASS.name()));
        String runPath = String.join(
            separator, "/" + String.join(separator,
                                             applicationTemplate.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                             applicationTemplate.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RUN_ICON.name()));
        String configPath = String.join(
            separator, "/" + String.join(separator,
                                             applicationTemplate.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                             applicationTemplate.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            applicationTemplate.manager.getPropertyValue(AppPropertyTypes.CONFIG_ICON.name()));
        config = setToolbarButton(configPath,AppPropertyTypes.CONFIG_TOOLTIP.name(),false);
        runButton = setToolbarButton(runPath,AppPropertyTypes.RUN_TOOLTIP.name(),false);
        
        Label title = new Label(chosen);
        HBox box = new HBox();
        box.getChildren().addAll(cluster,config);
        cluster.setToggleGroup(group);
        clas.setToggleGroup(group);
        cluster.setMinHeight(25);
        clas.setMinHeight(25);
        title.setMinWidth(120);
        title.setMinHeight(30);
        
        pane.setTop(title);
        pane.setCenter(box);
        pane.setBottom(runButton);
        BorderPane.setMargin(box, new Insets(10));
        pane.getStylesheets().add("css/GridPane.css");
        return pane;
        
    }
    public VBox algorithmPane()
    {
        VBox pane = new VBox();
        pane.setMinSize(150, 150);
        
        Button clas = new Button(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.CLASSIFICATION.name()));
        Button cluster = new Button(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.CLUSTERING.name()));
       Label alg = new Label(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.ALG_TYPE.name()));
       cluster.setMinSize(clas.getWidth(), clas.getHeight());
       cluster.setMinWidth(120);
       clas.setMinWidth(120);
       alg.setMinWidth(120);
       alg.setMinHeight(30);
        clas.setOnAction(e-> {
            leftPane.setBottom(choicePane(clas.getText()));
        });
        cluster.setOnAction(e-> {
            leftPane.setBottom(choicePane(cluster.getText()));
        });
        pane.getChildren().addAll(alg,clas,cluster);
        pane.getStylesheets().add("css/OriVBox.css");
       
        return pane;
        
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
               
                 String[] result = actualText.split("\n");

                 if(loadedData && result.length>10)
                    { 
                        
                        String[] check = textArea.getText().split("\n");
                        if(check.length<10){
                        
                        result = actualText.split("\n");
                        //System.out.println(textArea.getText()+"2");
                        String tempText = "";
                        tempText+= textArea.getText();
                        
                        if(result.length>10)
                        {
                            for(int i=10;i<result.length;i++)
                            {
                              tempText+=result[i]+"\n";
                            }
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
                            if(result.length>1)
                            {
                                for(int i = 0;i<result.length;i++)
                                 tempText+=result[i]+"\n";
                            }
                            else tempText = "";
                        }
                        textArea.setText(tempText);
                        
                        
                    }
                
                
                    }
                
            }});
        
               /* 
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
            );*/
               /*
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
        
        */
        	
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
