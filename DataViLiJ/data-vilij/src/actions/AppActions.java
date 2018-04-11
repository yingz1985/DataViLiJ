package actions;

import dataprocessors.AppData;
import java.io.File;
import static java.io.File.separator;
import java.io.FileNotFoundException;
import vilij.components.ActionComponent;
import vilij.templates.ApplicationTemplate;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import settings.AppPropertyTypes;
import ui.AppUI;
import vilij.components.ConfirmationDialog;
import vilij.components.ErrorDialog;
 
/**
 * This is the concrete implementation of the action handlers required by the application.
 *
 * @author Ritwik Banerjee
 */
public final class AppActions implements ActionComponent {

    /** The application to which this class of actions belongs. */
    private ApplicationTemplate applicationTemplate;
    private boolean saved = false;
    private boolean loaded = false;
    /** Path to the data file currently active. */
    Path dataFilePath;
    //private String fileName ;
    private File file;
    private FileChooser fileChooser;
    private String filePath;
    private boolean savedOnce;
    //fileName is initialized to empty string
    
    

    public AppActions(ApplicationTemplate applicationTemplate) {
        this.applicationTemplate = applicationTemplate;
       // fileName = applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DEFAULT_FILE_NAME.name());
        filePath =  String.join(separator,
                                             applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RESOURCES_RESOURCE_PATH.name()),
                                             applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_RESOURCE_PATH.name()));

        dataFilePath = Paths.get(filePath);
             
        file = new File(dataFilePath.toString());   
        dataFilePath = Paths.get(file.toURI());
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter save = new FileChooser.ExtensionFilter
                    (applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_FILE_EXT_DESC.name()),
                     applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_FILE_EXT.name()));
        fileChooser.getExtensionFilters().add(save);
        fileChooser.setInitialDirectory(file);
              
    }

    @Override
    public void handleNewRequest()
    {
        //clears whether the button clicked on is "yes" or "no"
       try{
           
             if(promptToSave())
            {
                savedOnce = false;
                saved = false;
                ((AppUI) applicationTemplate.getUIComponent()).clear();
                 file = new File(Paths.get(filePath).toString());
                ((AppUI) applicationTemplate.getUIComponent()).setLeftPane();
                loaded = false;
                ((AppUI) applicationTemplate.getUIComponent()).newPage();
             }
           
       }
       catch(Exception x)
       {
          //do nothing;
       }
       
    }
    
    
    @Override
    public void handleSaveRequest() {
        // TODO: NOT A PART OF HW 1
        //if(((AppUI) applicationTemplate.getUIComponent()).newText()){
      if(!savedOnce){
        try{
             AppData processor = new AppData(applicationTemplate);
             processor.loadData(((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText());
            //process string and see if there's an error 
            
            fileChooser.setInitialFileName(file.getName());
            
             File  tempfile = fileChooser.showSaveDialog((
                      (AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow());
                
              if (tempfile!=null) {
                  file = tempfile;
                PrintWriter writer = new PrintWriter(file);
              //System.out.print(((AppUI) applicationTemplate.getUIComponent()).getText());
              dataFilePath= Paths.get(file.toURI());
              
              writer.write(((AppUI) applicationTemplate.getUIComponent()).returnActualText());
              writer.close();
              saved = true;
              ((AppUI)applicationTemplate.getUIComponent()).resetSaveButton();
              ((AppUI)applicationTemplate.getUIComponent()).setNewText();
              }
              savedOnce = true;
              //disables the save button
              
        }
              catch(Exception x)
              {
                   //throw new Exception();
                 // System.out.println(x.toString());
              }
      }
      else
      {
          PrintWriter writer;
          try
          {
              writer = new PrintWriter(file);
              dataFilePath= Paths.get(file.toURI());
              
              writer.write(((AppUI) applicationTemplate.getUIComponent()).returnActualText());
              writer.close();
              saved = true;
              ((AppUI)applicationTemplate.getUIComponent()).resetSaveButton();
              ((AppUI)applicationTemplate.getUIComponent()).setNewText();
              
          }
          catch (FileNotFoundException ex)
          {
             
          }
              //System.out.print(((AppUI) applicationTemplate.getUIComponent()).getText());
              
      }
    }
    //}
    

    @Override
    public void handleLoadRequest() {
        String text = "";
        //TSDProcessor processor = new TSDProcessor();
        File  tempfile = fileChooser.showOpenDialog(((AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow());
       // System.out.println(tempfile.toString());
        if (tempfile!=null)
        {
            file = tempfile;
            try
            {
                
                dataFilePath = Paths.get(file.toURI());
                
                ((AppUI) applicationTemplate.getUIComponent()).clear();
                Scanner scanner = new Scanner(tempfile);
                while(scanner.hasNextLine())
                {
                    text+=scanner.nextLine()+"\n";
                }
                AppData processor = new AppData(applicationTemplate);
                processor.loadData(text);
                TextArea description = new TextArea();
                String label="";
                for(Object s: processor.getProcessor().returnLabels())
                {
                    label+="\n"+"-"+(String)s.toString();
                   // System.out.print(s.toString());
                }
                
               
                description.setText(processor.getProcessor().getLineNum()+" instances with "
                    +processor.getProcessor().returnLabels().length+ " labels from \n"+dataFilePath.toString()
                    +". \nThe labels are:"+label);

                ((AppUI)applicationTemplate.getUIComponent()).setDescription(description);
                
                ((AppUI) applicationTemplate.getUIComponent()).setActualText(text);
               // ((AppUI) applicationTemplate.getUIComponent()).getProcessor().loadData(text);
                //processes the whole string
                
                
                ((AppUI) applicationTemplate.getUIComponent()).LoadedData();
                if(processor.lineNum()>10)
                {
                    
                    
                   /* ErrorDialog dialog = ErrorDialog.getDialog();
                    
                    dialog.show(applicationTemplate.manager.getPropertyValue
                    (AppPropertyTypes.SPECIFIED_FILE.name()), 
                    applicationTemplate.manager.getPropertyValue
                    (AppPropertyTypes.DATA_OVERFLOW_1.name())
                    +processor.lineNum()
                    +applicationTemplate.manager.getPropertyValue
                    (AppPropertyTypes.DATA_OVERFLOW_2.name()));
                    */
                    int i = 0;
                    text = "";
                    scanner = new Scanner(tempfile);
                    
                    while(i<10)
                    {
                        text+=scanner.nextLine()+"\n";
                        //load only the first 10 lines into the textArea
                        i++;
                    }
                    
                    
                }
                //System.out.println(text);
                ((AppUI) applicationTemplate.getUIComponent()).setLeftPane();
                if(processor.getProcessor().hasNull() || processor.getProcessor().returnLabels().length!=2)
                {
                    ((AppUI) applicationTemplate.getUIComponent()).disableClassification();
                    //System.out.print("disabled from app actions");
                }
                else
                    ((AppUI) applicationTemplate.getUIComponent()).enableClassification();
                ((AppUI) applicationTemplate.getUIComponent()).getTextArea().setText(text);
                loaded = true;
                ((AppUI) applicationTemplate.getUIComponent()).setProcessor(processor);
                //if read correctly without errors paste it into the text area
                //saved = true; //no need to save it when you first load it, since it was originally saved
                ((AppUI)applicationTemplate.getUIComponent()).resetSaveButton();
               // loaded = true; //loaded new data
               
                saved = true;
            }
            catch(Exception x)
            {
                //System.out.println(x.getCause());
                ((AppUI)applicationTemplate.getUIComponent()).initialize();
                //if data is loaded incorrectly, the page is cleared out
            }
        }
        
        
        
        
    }

    @Override
    public void handleExitRequest() {
        // TODO for homework 1
        
        //I wanted to prompt Confirmation dialog even when exiting 
       if(!((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText().isEmpty()
          && !saved)
       {
          ConfirmationDialog dialog = ConfirmationDialog.getDialog();
        
         dialog.show(AppPropertyTypes.EXIT_WHILE_RUNNING_WARNING .name(),
                   applicationTemplate.manager.getPropertyValue(
                   AppPropertyTypes.EXIT_WHILE_RUNNING_WARNING .name()));
         if(dialog.getSelectedOption().toString().equals("YES"))   
         {
             ((AppUI) applicationTemplate.getUIComponent()).clear();
              ((AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow().close();
              System.exit(0);
         }
        }
       else
       {
           ((AppUI) applicationTemplate.getUIComponent()).clear();
           ((AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow().close();
           System.exit(0);
       }
    }

    @Override
    public void handlePrintRequest() {
        // TODO: NOT A PART OF HW 1
    }

    public void handleScreenshotRequest()  {
        // TODO: NOT A PART OF HW 1
        
        WritableImage image = ((AppUI) applicationTemplate.getUIComponent())
                .getChart().snapshot(new SnapshotParameters(), null);

       FileChooser fileChoose = new FileChooser();
       
       FileChooser.ExtensionFilter save = new FileChooser.ExtensionFilter
                    (applicationTemplate.manager.getPropertyValue(AppPropertyTypes.SCREENSHOT_EXT_DESC.name()),
                     applicationTemplate.manager.getPropertyValue(AppPropertyTypes.SCREENSHOT_EXT.name()));
       //fileChoose.setSelectedExtensionFilter(save);
       fileChoose.getExtensionFilters().add(save);
       File f = fileChoose.showSaveDialog((
                      (AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow());
       
       if(f!=null){
       try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", f);
        } 
        catch (Exception e) {
            
            System.out.print(e.getCause());
        
        }
       }
    }

    /**
     * This helper method verifies that the user really wants to save their unsaved work, which they might not want to
     * do. The user will be presented with three options:
     * <ol>
     * <li><code>yes</code>, indicating that the user wants to save the work and continue with the action,</li>
     * <li><code>no</code>, indicating that the user wants to continue with the action without saving the work, and</li>
     * <li><code>cancel</code>, to indicate that the user does not want to continue with the action, but also does not
     * want to save the work at this point.</li>
     * </ol>
     *
     * @return <code>false</code> if the user presses the <i>cancel</i>, and <code>true</code> otherwise.
     */
    private boolean promptToSave()  throws Exception{
        // TODO for homework 1
        if(((AppUI)applicationTemplate.getUIComponent()).getTextArea().getText().isEmpty())
            return true;
        if(((AppUI) applicationTemplate.getUIComponent()).newText() && !loaded) saved = false;
        
        if(!saved){

        //returns true if the eventHandler should keep executing
        //(clear scene or exit window)

       ConfirmationDialog dialog = ConfirmationDialog.getDialog();
       
       dialog.show(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.SAVE_UNSAVED_WORK_TITLE.name()),
                   applicationTemplate.manager.getPropertyValue(AppPropertyTypes.SAVE_UNSAVED_WORK.name()));
       
       if(dialog.getSelectedOption().toString().equals("NO"))
       {
           return true;
       }
       else
       { 
           if(dialog.getSelectedOption().toString().equals("YES"))   
           {
               if(savedOnce)
               {
                      PrintWriter writer;
                 try
                {
                     writer = new PrintWriter(file);
                    dataFilePath= Paths.get(file.toURI());
              
                    writer.write(((AppUI) applicationTemplate.getUIComponent()).returnActualText());
                    writer.close();
                    saved = true;
                    ((AppUI)applicationTemplate.getUIComponent()).resetSaveButton();
                    ((AppUI)applicationTemplate.getUIComponent()).setNewText();
                    return true;
              
                }
                catch (FileNotFoundException ex)
                {
             
                }
              }
              else
               {
              try{
                    AppData processor = new AppData(applicationTemplate);
                    processor.loadData(((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText());
                //process string and see if there's an error 
              
              fileChooser.setInitialFileName(file.getName());
              
              
               file = fileChooser.showSaveDialog((
                      (AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow());
              if(file!=null){
              PrintWriter writer = new PrintWriter(file);
              //System.out.print(((AppUI) applicationTemplate.getUIComponent()).getText());
              writer.write(((AppUI) applicationTemplate.getUIComponent()).returnActualText());
              writer.close();}
              saved = true;
              return file != null;}
              catch(Exception x)
              {
                   //throw new Exception();
                  //System.out.println(x.toString());
              }
               }
           }
         
       }}
        // else if(saved)
        //if saved, or does not have new text
        //return false;
        
       return saved;    
        
    }

}
