package ui;
//@author Ying Zhang
import actions.AppActions;
import dataprocessors.AppData;
import dataprocessors.DataSet;
import static java.io.File.separator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import runningEvents.Algorithm;
import runningEvents.AlgorithmContainer;
import runningEvents.RandomClassifier;
import vilij.propertymanager.PropertyManager;
import settings.AppPropertyTypes;
import vilij.components.ErrorDialog;
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
    private boolean                      hasNewText;     // whether or not the text area has any new data since last display
    private Button                       done ;
    private boolean                      loadedData;
    private AppData                      processor;
    private String                       actualText = "";//actual text is used when loading from file 
    private ImageView                    emptyChart;
    private BorderPane                   workSpace;
    private BorderPane                   leftPane = new BorderPane();
    private Label                        description;
    private AlgorithmContainer           ClusterContainer;
    private AlgorithmContainer           ClassContainer; 
    private AlgorithmContainer           currentContainer;
    private Button                       clas;
    private Button                       cluster;
    private ToggleGroup                  group;
    
    private Algorithm                    run;
    
    private Thread                       thread;
    private int                          counter = 0;
    private Button                       backButton;
    private VBox                        chartSpace;
    private Label                       iteration;
    
    public void disableClassification()
    {
        clas.setDisable(true);
    }
    public void enableClassification()
    {
        clas.setDisable(false);
    }
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
    public void setNewButton()
    {
        newButton.setDisable(false);
    }
    public void setActualText(String s)
    {
        this.actualText = s;
    }
    public void setText(String s)
    {
        textArea.setText(s);
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
        ClusterContainer = new AlgorithmContainer(null);
        ClassContainer = new AlgorithmContainer(null);
        processor = new AppData(applicationTemplate);
        setupButtons();
        populateContainers();
        run = new RandomClassifier(null,0,0,false,0,applicationTemplate);;
        done = new Button(AppPropertyTypes.EDIT.name());
        iteration = new Label();
        chartSpace = new VBox();
        chartSpace.getChildren().addAll(chart,iteration);
    }
    
    public void resetSaveButton()
    {
        saveButton.setDisable(true);
    }
    public void setIteration(int i)
    {
        iteration.setText("Iteration "+i);
    }
    
    @Override
    public void clear() {
        // TODO for homework 1
        clas.setDisable(false);
        iteration.setText("");
        thread = null;
        //newButton.setDisable(true);
        saveButton.setDisable(true);
        chart.getData().clear();
        scrnshotButton.setDisable(true);
        actualText = "";
        textArea.clear();
        loadedData = false;
        scrnshotButton.setDisable(true);
        workSpace.setRight(emptyChart);
        leftPane = new BorderPane();
        workSpace.setLeft(leftPane);
        processor = new AppData(applicationTemplate);
        counter = 0;
        done.setDisable(false);
        //if(window!=null)
        //    window.close();
        //window = null;  
    }
    
    public void setScreenshot(boolean c)
    {
        scrnshotButton.setDisable(c);
    }

    private void layout() {
        // TODO for homework 1
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
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
        
        textArea.setMaxWidth(300);


        description = new Label();
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        
        chart = new LineChart<Number,Number>(xAxis,yAxis);

        workSpace = new BorderPane();
        workSpace.setPadding(new Insets(10,10,10,10));
        workSpace.setRight(emptyChart);
       // workSpace.setRight(chart);
        chart.setMinSize(600, 500);
        chart.setMaxSize(600, 500);
        
        

        //setLeftPane();
        
        VBox wholeScene = new VBox();
        wholeScene.getChildren().addAll(toolBar,workSpace);
        Scene current = new Scene(wholeScene,1000,600);
        
        primaryStage.setScene(current);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.DATA_VISUALIZATION.name()));
        current.getStylesheets().add("css/chart.css");
       
        
    }
    
    public void newPage()
    {
        leftPane.setBottom(null);
        leftPane.setCenter(null);
        workSpace.setRight(emptyChart);
        done.setText("Edit");
    }
    
    public void setDescription()
    {
        TextArea description = new TextArea();
                String label="";
                for(Object s: processor.getProcessor().returnLabels())
                {
                    label+="\n"+"-"+(String)s.toString();
                   // System.out.print(s.toString());
                }
               
                description.setText(processor.getProcessor().getLineNum()+" instances with "
                    +processor.getProcessor().returnLabels().length+ " labels"+
                    " were entered. \nThe labels are:"+label);
                Pane pane = new Pane();
                pane.setMinSize(300, 170);
        
                description.setMaxHeight(155);
                description.setMaxWidth(330);
                description.setPrefRowCount(10);
                description.getStylesheets().add("css/GridPane.css");
                description.setEditable(false);
        
             pane.getChildren().add(description);
             leftPane.setCenter(pane);
                //((AppUI)applicationTemplate.getUIComponent()).setDescription(description);
    }

    public void setDescription(TextArea description)
    {
        Pane pane = new Pane();
        pane.setMinSize(300, 170);
        
        description.setMaxHeight(155);
        description.setMaxWidth(330);
        description.setPrefRowCount(10);
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
            textArea.setDisable(false);
            done.setStyle(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EDIT.name()));
            
            done.setOnAction(e->
            {
                if(done.getText().equals(AppPropertyTypes.DONE.name()))
                {
                     done.setText(AppPropertyTypes.EDIT.name());
                     done.setStyle(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.EDIT.name()));
                     textArea.setDisable(false);
                     newPage(); //all previously available algorithm now dissappears 
                                //label is not available either since user has decided to edit again
                }
                else
                {
                    if(!textArea.getText().isEmpty()){
                    try
                    {
                        processor = new AppData(applicationTemplate);
                        processor.loadData(textArea.getText());
                        leftPane.setBottom(algorithmPane());
                        done.setText(AppPropertyTypes.DONE.name());
                        done.setStyle(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DONE.name()));
                        textArea.setDisable(true);
                        setDescription();
                    }
                    catch (Exception ex)
                    {
                        newPage();
                    }
                    if(processor.getProcessor().hasNull() || processor.getProcessor().returnLabels().length!=2)
                {
                    ((AppUI) applicationTemplate.getUIComponent()).disableClassification();
                }
                    else
                    {
                        clas.setDisable(false);
                    }
                    }
                    
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
    /*
    populated choice pane with radio buttons
    */
    public void populateContainers()
    {
        group = new ToggleGroup();
        String[] classifier = applicationTemplate.manager.getPropertyValue(AppPropertyTypes.CLASSIFIER_ALG.name()).split(",");
        String[] clusterer = applicationTemplate.manager.getPropertyValue(AppPropertyTypes.CLUSTERER_ALG.name()).split(",");
        for(int i = 0;i<classifier.length;i++)
        {
            RadioButton c0 = new RadioButton(classifier[i]);
            ClassContainer.addType(c0, new ConfigWindow(applicationTemplate,ClassContainer));
            c0.setPrefHeight(30);
            c0.setToggleGroup(group);
        }
        for(int i = 0;i<clusterer.length;i++)
        {
            RadioButton c0 = new RadioButton(clusterer[i]);
            ClusterContainer.addType(c0, new ConfigWindow(applicationTemplate,ClusterContainer));
            c0.setPrefHeight(30);
            c0.setToggleGroup(group);
        }
        
        
    }
    public void setRun(boolean c)
    {
        runButton.setDisable(c);
    }
    public void running(boolean c)
    {
        backButton.setDisable(c);
        done.setDisable(c);
        currentContainer.getWindow((RadioButton)group.getSelectedToggle()).getButton().setDisable(c);
        
    }
    /**
     * <dt>Precondition:
     *  <dd> User has clicked on one of the algorithm types in the algorithm pane
     * @param chosen chosen algorithm type from the algorithm pane event action
     * @return  returns a border pane object to be set as the node on the lower left of the workspace
     */
    public BorderPane choicePane(String chosen)
    {
        
        BorderPane pane = new BorderPane();
        group.selectToggle(null);
        String runPath = String.join(
            separator, "/" + String.join(separator,
                                             applicationTemplate.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                             applicationTemplate.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RUN_ICON.name()));
        String backPath = String.join(
            separator, "/" + String.join(separator,
                                             applicationTemplate.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                             applicationTemplate.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            applicationTemplate.manager.getPropertyValue(AppPropertyTypes.BACK_ICON.name()));
        runButton = setToolbarButton(runPath,applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RUN_TOOLTIP.name()),true);
        backButton = setToolbarButton(backPath,applicationTemplate.manager.getPropertyValue(AppPropertyTypes.BACK_TOOLTIP.name()),false);
        HBox bot = new HBox(100);
        bot.getChildren().addAll(backButton,runButton);
        bot.setAlignment(Pos.CENTER_LEFT);
        pane.setBottom(bot);
        runButton.setVisible(false);
        backButton.setOnAction(e->
        {
            if(this.thread!=null)
            {
                
                run.stop();
                thread = null;
            }
            leftPane.setBottom(algorithmPane());
        }     
        );
        //runButton enabled only when config had been set 
        runActions();
        Label title = new Label(chosen);
        HBox box = new HBox();
        box.setPrefHeight(30);
        VBox midBox = new VBox();
        ScrollPane mid = new ScrollPane();
        if(chosen.equals("Classification"))
        {
            currentContainer = ClassContainer;
 
        }
        else
        {
            currentContainer = ClusterContainer;
        }
        for(Object o : currentContainer.returnTypes())
            {
                    box = new HBox();
                    box.getChildren().addAll((RadioButton)o,currentContainer.getWindow((RadioButton)o).getButton());
                    midBox.getChildren().add(box);
            }
        mid.setContent(midBox);
        mid.setPannable(true);
        mid.setPrefViewportHeight(40);
        //config.setDisable(true);
        for(Object o: currentContainer.returnWindows())
        {
            ((ConfigWindow)o).getButton().setOnAction(e->
            {
                ((ConfigWindow)o).init();
                

            });
        }
        
        for(Object o:currentContainer.returnTypes())
        {
            ((RadioButton)o).setOnAction(e->
            {
                currentContainer.getWindow((RadioButton)o).getButton().setDisable(false);
                runButton.setVisible(true);
                /*if(currentContainer.getWindow((RadioButton)o).closed())
                {
                    enableRun();
                }
                else*/
                    runButton.setDisable(false);

            });
        }
        
        
        
        title.setMinWidth(120);
        title.setMinHeight(30);
        
        
         
        pane.setTop(title);

        pane.setCenter(mid);
        BorderPane.setAlignment(box, Pos.TOP_CENTER);
        //pane.setBottom(runButton);
        BorderPane.setMargin(box, new Insets(10));
        pane.setBorder(new Border(new BorderStroke(Color.ANTIQUEWHITE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        pane.getStylesheets().add("css/GridPane.css");
        return pane;
         
    }
    public void setupButtons()
    {
        clas = new Button(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.CLASSIFICATION.name()));
        cluster = new Button(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.CLUSTERING.name()));
    }
    public Runnable getThread()
    {
        return run;
        
    }

    public synchronized void runActions()
    {
        runButton.setOnAction(e->
        {   
            
            if(!currentContainer.getWindow((RadioButton)group.getSelectedToggle()).closed())
            {
                ErrorDialog dialog = ErrorDialog.getDialog();
                dialog.show(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.CONFIG_TOOLTIP.toString()),
                                  applicationTemplate.manager.getPropertyValue(AppPropertyTypes.CONFIG_ERRORS.name()));
                currentContainer.getWindow((RadioButton)group.getSelectedToggle()).init();
            }
            else
            {
                //////////button should be disabled, will be dealt with later 
                //currentContainer.getWindow((RadioButton)group.getSelectedToggle()).getButton().setDisable(true);
                
                AlgorithmContainer container = currentContainer.getWindow((RadioButton)group.getSelectedToggle()).returnContainer();
                workSpace.setRight(chartSpace);
                ClassLoader loader = ClassLoader.getSystemClassLoader();
                try
                {
                    backButton.setDisable(true);
                    DataSet dataset= new DataSet();
                    dataset.fromTSDFile(this.returnActualText());
                    dataset.setChart(chart);

                    Class c = loader.loadClass(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RUNNINGEVENT.name())+((RadioButton)group.getSelectedToggle()).getText());
                    Constructor con = c.getConstructors()[0];
                    //run = (Algorithm)con.newInstance(dataset,container.getMaxIterations(),container.getUpdateInterval(),
                             //   container.tocontinue(),container.getLabelNum(),applicationTemplate);
                    //Method method = c.getMethod("run");
                    //thread = new Thread((Runnable)k);
                    //thread.start();
                    if(container.tocontinue())
                    {
                            isolateChoice(true);
                            counter = 0;

                            run = (Algorithm)con.newInstance(dataset,container.getMaxIterations(),container.getUpdateInterval(),
                                container.tocontinue(),container.getLabelNum(),applicationTemplate);
                            thread = new Thread(run);
                            thread.start();
                    }
                     else
                        {
                            if(counter!=0)  
                            {
                                
                                if(!(boolean)c.getMethod("done").invoke(run))
                                    c.getMethod("notifyThread").invoke(run);
                                else
                                    counter = 0;
                                    
                                //if(!((RandomClassifier)run).done())
                                  //  ((RandomClassifier)run).notifyThread();
                                //else
                                  //  counter = 0;
                                
                            }
                            if(counter ==0){
                                isolateChoice(true);
                                run = (Algorithm)con.newInstance(dataset,container.getMaxIterations(),container.getUpdateInterval(),
                                container.tocontinue(),container.getLabelNum(),applicationTemplate);
                                thread = new Thread(run);
                                thread.start();  
                                counter+=container.getUpdateInterval();
                            
                            }
                        }
                        
                        
                        chart.setLegendVisible(false);
                        
                        

                    
                    //System.out.println("workinggggg");
                    
                    
                }
                catch (ClassNotFoundException ex)
                {
                   System.out.println("could not find class");
                }
                catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
                {
                    System.out.println("constructor invoked wronggg");
                }
                catch (NoSuchMethodException ex)
                {
                    System.out.println("cannot find method");
                }
                catch (SecurityException ex)
                {
                    System.out.println("security issues");
                }
                
             /*   
                if(!container.isCluster())
                {
                    backButton.setDisable(true);
                    
                    //System.out.print(container.toString());
                     try
                    {
                        DataSet dataset= new DataSet();
                        dataset.fromTSDFile(this.returnActualText());
                        dataset.setChart(chart);

                        if(container.tocontinue())
                        {
                            counter = 0;
                            run = new RandomClassifier(dataset,container.getMaxIterations(),container.getUpdateInterval(),
                                    container.tocontinue(),0,applicationTemplate);
                            thread = new Thread(run);
                            thread.start();
                        }
                        else
                        {
                            if(counter!=0)  
                            {
                                if(!((RandomClassifier)run).done())
                                    ((RandomClassifier)run).notifyThread();
                                else
                                    counter = 0;
                                //synchronized(runButton)
                                //thread.notifyAll();
                                //  { runButton.notifyAll();}
                            }
                            if(counter ==0){
                                run = new RandomClassifier(dataset,container.getMaxIterations(),container.getUpdateInterval(),
                                    container.tocontinue(),0,applicationTemplate);
                            //run.setToContinue(container.tocontinue());
                                thread = new Thread(run);
                                thread.start();
                                counter+=container.getUpdateInterval();
                            }
                        }
                        
                        
                        chart.setLegendVisible(false);
                        
                        
                      }
                        
                    catch (Exception ex)
                    {
                        
                    }
                    
                   
                   
                   
                }
                else
                {
                    DataSet dataset= new DataSet();
                    dataset.fromTSDFile(this.returnActualText());
                    dataset.setChart(chart);
                    
                    
                    if(counter!=0)  
                    {
                                if(!clust.done())
                                    clust.notifyThread();
                                else
                                    counter = 0;
                                //synchronized(runButton)
                                //thread.notifyAll();
                                //  { runButton.notifyAll();}
                    }
                    if(counter ==0){
                                clust = new RandomClusterer(dataset,container.getMaxIterations(),container.getUpdateInterval(),
                                container.tocontinue(),container.getLabelNum(),applicationTemplate);
                                //new KMeansClusterer(dataset,container.getMaxIterations(),container.getUpdateInterval(),
                                //container.tocontinue(),container.getLabelNum(),applicationTemplate);
                                thread = new Thread(clust);
                                thread.start();
                                chart.setLegendVisible(false);
                                counter+=container.getUpdateInterval();
                    }
                    //chart.getXAxis().setAutoRanging(true);
                    //chart.getYAxis().setAutoRanging(true);
                    
                    
                }*/
            }
        });
        
    }

    /**
     * <dt> Precondition:
     *  <dd> Data (either loaded or entered by user) must be valid
     * Method contains the initial UI for choosing an algorithm 
     * 
     * <dt> Postcondition:
     *  <dd> One of the two algorithms have been selected 
     * @return returns a vbox object for the lower left of the workspace
     */
    public VBox algorithmPane()
    {
        VBox pane = new VBox();
        pane.setMinSize(150, 150);
        

       Label alg = new Label(applicationTemplate.manager.getPropertyValue(
          AppPropertyTypes.ALG_TYPE.name()));
       cluster.setMinSize(clas.getWidth(), clas.getHeight());
       cluster.setMinWidth(140);
       cluster.setMinHeight(30);
       clas.setMinWidth(140);
       clas.setMinHeight(30);
       alg.setMinWidth(140);
       alg.setMinHeight(40);
       clas.setOnAction(e-> {
            
            ClassContainer.setAlg(clas.getText());
            
                
                if(!loadedData)
                {
                    actualText = textArea.getText();
                }
            
                    try
                    {
                        if(!actualText.isEmpty()&&!loadedData&&hasNewText)
                        {
                             processor.clear();
                             processor.loadData(actualText);
                        }
                        if(processor.getProcessor().hasNull() || processor.getProcessor().returnLabels().length!=2)
                        {
                            ErrorDialog dialog = ErrorDialog.getDialog();
                            dialog.show(applicationTemplate.manager.getPropertyValue
                                (AppPropertyTypes.SELECTION_ERROR.name()),applicationTemplate
                                .manager.getPropertyValue(AppPropertyTypes.NULL_ERROR.name()));
                        }
                        else 
                        {
                            leftPane.setBottom(choicePane(clas.getText()));
                            isolateChoice(false);
                        }
                        
                    }
                    catch (Exception ex)
                    {
                        
                    }
                
 
        });
        
        cluster.setOnAction(e-> {
            ClusterContainer.setAlg(cluster.getText());
            leftPane.setBottom(choicePane(cluster.getText()));
            
        });
        
       
        pane.getChildren().addAll(alg,clas,cluster);
        pane.getStylesheets().add("css/OriVBox.css");
        //pane.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        
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
                    
                    
                    
                    processor.displayData();
                    
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
        if(!loadedData)
            return textArea.getText();
        return actualText;
    }
    public boolean currentRun()
    {
        return runButton.isDisabled();
    }
    public void isolateChoice(boolean c)
    {
        for(Object o:currentContainer.returnTypes())
        {
            ((RadioButton)o).setDisable(c);
        }
    }
    
    public boolean done()
    {
        return run.done();
    }
    public void stop()
    {
        run.stop();
    }
}
