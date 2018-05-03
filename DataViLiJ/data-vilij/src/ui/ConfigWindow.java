/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import static java.io.File.separator;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import runningEvents.AlgorithmContainer;
import settings.AppPropertyTypes;
import static vilij.settings.PropertyTypes.GUI_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.ICONS_RESOURCE_PATH;
import vilij.templates.ApplicationTemplate;

/**
 *
 * @author Bagel
 */
public class ConfigWindow
{
    private ApplicationTemplate app;
    private AlgorithmContainer config;
    private int defaultMax = 1000;
    private int defaultIt = 1;
    private int defaultLabels = 1;
    private boolean toCont;
    private TextField maxText;
    private TextField updateText;
    private TextField num;
    private Stage stage;
    private GridPane pane;
    private BorderPane root;
    private CheckBox continuous;
    private Button  done;
    private boolean closed;
    private Button button;
    private Button close;
    
    
    static double xOffset;
    static double yOffset;
    
    public ConfigWindow(ApplicationTemplate app,AlgorithmContainer container)
    {
        this.app = app;
        config = container;
        root = new BorderPane();
        num = new TextField();
        maxText = new TextField();
        updateText = new TextField();
        setConfigButton();
    }
    public AlgorithmContainer returnContainer()
    {
        config.configure(defaultMax, defaultIt, toCont,defaultLabels);
        return config;
    }
    public void configure(AlgorithmContainer container)
    {
        config = container;
    }
    
    public void close()
    {
        stage.close();
        
    }
    /**
     * <dt>Precondition:
     *  <dd> user has done setting up configuration at least once
     * 
     * <dt>Postcondition:
     *  <dd> text fields are populated by default values entered by the user
     *  <dd> and will restore the next time user opens the configuration window
     */
    public void setupVals()
    {
        if(closed)
        {
            maxText.setText(String.valueOf(defaultMax));
            updateText.setText(String.valueOf(defaultIt));
            num.setText(String.valueOf(defaultLabels));
            continuous.setSelected(toCont);
               
        }
        
    }
    
    public void setLabelNum(int k)
    {
        this.defaultLabels = k;
    }
    //if closed at least once, that means the user has changed config
    //if closed without configuring, the algorithm filsl the default data
    public boolean closed()
    {
        return closed;
    }
    public void checkConfig()
    {
        try
        {
            int max = Integer.valueOf(maxText.getText());
            if(max>0)
                defaultMax = max;
            
            
        }
        catch(NumberFormatException x)
        {
            maxText.setText(String.valueOf(defaultMax));
        }
        try
        {
           int update = Integer.valueOf( updateText.getText());
           if(update>0)
             defaultIt = update;
        }
        catch(NumberFormatException x)
        {
            updateText.setText(String.valueOf(defaultIt));
        }
        if(config.isCluster()){
            try
            {
                int defaultLabel = Integer.valueOf(num.getText());
                if(defaultLabel>0&& defaultLabel<5)
                    defaultLabels = defaultLabel;
            
            }
            catch(NumberFormatException x)
            {
                num.setText(String.valueOf(defaultLabels));
            }
        }
        toCont = continuous.isSelected();
    }
    
    /**
     * at window opening, checks whether the algorithm type is clustering or 
     * classification, and prompt corresponding window to open.
     */
    public void init()
    {
        
        if(config.isCluster())
        {
            initCluster();
        }
        else
            initClass();
        setupVals();
        dragScene();
        stage.show();
    }
    public void initCluster()
    {
        initClass();
        Label numLabels = new Label(app.manager.getPropertyValue(AppPropertyTypes.NUM_LABELS.name()));
        num = new TextField();
       // pane.add(numLabels, 0, 3);
        //Label continuousL = new Label(app.manager.getPropertyValue(AppPropertyTypes.CONTINUOUS.name()));
        //pane.add(numLabels,0,2);
        //pane.add(num,1,2);
        pane.addRow(2,numLabels,num);
       // continuous = new CheckBox();
        //pane.add(continuousL,0,3);
        //pane.add(continuous, 1, 3);
        pane.getRowConstraints().clear();
        RowConstraints row = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        // RowConstraints row4 = new RowConstraints();
        row.setPercentHeight(20);
        row1.setPercentHeight(20);
        row2.setPercentHeight(20);//#####
        row3.setPercentHeight(20);
        pane.getRowConstraints().addAll(row,row1,row2,row3);
        
        
        
        
    }
    /**
     * initializes the stage for classification type configuration window
     * note: does not prompt user for the number of labels 
     */
    public void initClass()
    {
        stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setX(app.getUIComponent().getPrimaryWindow().getWidth()-400);
        stage.setY(app.getUIComponent().getPrimaryWindow().getHeight()-200);
        root = new BorderPane();
        pane = new GridPane();
        pane.setPadding(new Insets(10,10,10,10));
        RowConstraints row = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        // RowConstraints row4 = new RowConstraints();
        row.setPercentHeight(25);
        row1.setPercentHeight(25);
        row2.setPercentHeight(0);//#####
        row3.setPercentHeight(25);
        pane.getRowConstraints().addAll(row,row1,row2,row3);
       
        
        Label title = new Label("    "+app.manager.getPropertyValue(AppPropertyTypes.CONFIG_WINDOW_TITLE.name()));
        
        title.setAlignment(Pos.CENTER);
        Label max = new Label(app.manager.getPropertyValue(AppPropertyTypes.MAX_ITER.name()));
        maxText = new TextField();
        Label update = new Label(app.manager.getPropertyValue(AppPropertyTypes.UPDATE_INT.name()));
        updateText = new TextField();
        root.setTop(title);
        Label continuousL = new Label(app.manager.getPropertyValue(AppPropertyTypes.CONTINUOUS.name()));
        continuous = new CheckBox();
        done = new Button(null, new ImageView(new Image(getClass().getResourceAsStream(
        String.join(
            separator, "/" + String.join(separator,
            app.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
             app.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            app.manager.getPropertyValue(AppPropertyTypes.DONE_ICON.name()))))));
        close =  new Button(null, new ImageView(new Image(getClass().getResourceAsStream(
        String.join(
            separator, "/" + String.join(separator,
            app.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
             app.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            app.manager.getPropertyValue(AppPropertyTypes.CLOSE_ICON.name()))))));
        HBox buttons = new HBox(5);
        buttons.getChildren().addAll(close,done);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        //pane.addRow(0, title);
        pane.addRow(0, max,maxText);
        pane.addRow(1, update,updateText);
        pane.addRow(3, continuousL,continuous);
        //pane.addRow(3, new Label());
        pane.add(buttons, 1, 4);
        GridPane.setHalignment(buttons, HPos.RIGHT);
        
        pane.setHgap(30);
        root.setCenter(pane);
        close.setOnAction(e-> 
        {
            stage.close();
        });
        done.setOnAction(e->
        {
            closed=true;
            checkConfig();
            stage.close();
            
        });
        
        pane.setMinSize(350, 250);
        root.setManaged(false);
        Scene scene = new Scene(root,400,300);
        
        
        root.getStylesheets().add("css/configWindow.css");
        root.setPadding(new Insets(10,10,10,10));
        
        title.setStyle("-fx-font-size:18pt; -fx-font-family:Didot;");
        //title.setFont(new Font("Didot",25));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle( app.manager.getPropertyValue(AppPropertyTypes.CONFIG_WINDOW_TITLE.name()) );
        //stage.show();
    }
    /**
     * allows user to draw configuration window (undecorated and always on top)
     */
    public void dragScene()
    {
        
        root.setOnMousePressed(e-> {
        
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            
        });
        
       root.setOnMouseDragged(e-> {
            
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            }
        );
    }
    /**
     * initializes button for entering configuration window 
     */
    private void setConfigButton()
    {

        String configPath = String.join(
            separator, "/" + String.join(separator,
                                             app.manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                             app.manager.getPropertyValue(ICONS_RESOURCE_PATH.name())),
            app.manager.getPropertyValue(AppPropertyTypes.CONFIG_ICON.name()));
        
        button = new Button(null, new ImageView(new Image(getClass().getResourceAsStream(configPath)))); 
        button.setTooltip(new Tooltip(app.manager.getPropertyValue(AppPropertyTypes.CONFIG_TOOLTIP.name())));
        button.setMinHeight(30);
        //button.setMaxHeight(25);
        //note that currently only two buttons are enabled 
        //button.setDisable(true);
        
    }
    /**
     * Every config window to be prompted by the user is triggered by the gear button
     * The buttons are unique to each window, but have the same outlooks 
     * @return returns a config button object 
     */
    public Button getButton()
    {  
        return button;
    }

}
