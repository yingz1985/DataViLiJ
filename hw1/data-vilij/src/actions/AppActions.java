package actions;

import java.io.File;
import static java.io.File.separator;
import vilij.components.ActionComponent;
import vilij.templates.ApplicationTemplate;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import javafx.stage.FileChooser;
import settings.AppPropertyTypes;
import ui.AppUI;
import vilij.components.ConfirmationDialog;

/**
 * This is the concrete implementation of the action handlers required by the application.
 *
 * @author Ritwik Banerjee
 */
public final class AppActions implements ActionComponent {

    /** The application to which this class of actions belongs. */
    private ApplicationTemplate applicationTemplate;

    /** Path to the data file currently active. */
    Path dataFilePath;
    

    public AppActions(ApplicationTemplate applicationTemplate) {
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    public void handleNewRequest()
    {
        //clears whether the button clicked on is "yes" or "no"
       try{
       if(promptToSave())
         ((AppUI) applicationTemplate.getUIComponent()).clear();
       }
       catch(Exception x)
       {
          //do nothing;
       }
       
    }

    @Override
    public void handleSaveRequest() {
        // TODO: NOT A PART OF HW 1
       try{
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter save = new FileChooser.ExtensionFilter
                    (applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_FILE_EXT_DESC.name())+"(*.tsd)",
                     applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_FILE_EXT.name()));
              
              
              fileChooser.getExtensionFilters().add(save);
              

              File file = fileChooser.showSaveDialog((
                      (AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow());
       }
       catch(Exception x)
       {}
    }

    @Override
    public void handleLoadRequest() {
        // TODO: NOT A PART OF HW 1
    }

    @Override
    public void handleExitRequest() {
        // TODO for homework 1
        
        //I wanted to prompt Confirmation dialog even when exiting 
       /* if( ! ((AppUI) applicationTemplate.getUIComponent()).getChart().getData().isEmpty()   )
        {
            if(promptToSave())
            {*/
                 ((AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow().close();
            // }
       // }
    }

    @Override
    public void handlePrintRequest() {
        // TODO: NOT A PART OF HW 1
    }

    public void handleScreenshotRequest() throws IOException {
        // TODO: NOT A PART OF HW 1
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
              try{
              FileChooser fileChooser = new FileChooser();
              FileChooser.ExtensionFilter save = new FileChooser.ExtensionFilter
                    (applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_FILE_EXT_DESC.name()),
                     applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_FILE_EXT.name()));
             
              fileChooser.getExtensionFilters().add(save);
             // fileChooser.setInitialDirectory(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_RESOURCE_PATH.name()));
              String filePath =  String.join(separator,
                                             applicationTemplate.manager.getPropertyValue(AppPropertyTypes.RESOURCES_RESOURCE_PATH.name()),
                                             applicationTemplate.manager.getPropertyValue(AppPropertyTypes.DATA_RESOURCE_PATH.name()));

                                           
             // dataFilePath = dataFilePath.getFileName();
              File file = new File(filePath);
              fileChooser.setInitialDirectory(file);
                     
               file = fileChooser.showSaveDialog((
                      (AppUI) applicationTemplate.getUIComponent()).getPrimaryWindow());
              PrintWriter writer = new PrintWriter(file);
              //System.out.print(((AppUI) applicationTemplate.getUIComponent()).getText());
              writer.write(((AppUI) applicationTemplate.getUIComponent()).getText());
              writer.close();
              return file != null;}
              catch(Exception x)
              {
                   //throw new Exception();
                 // System.out.println(x.toString());
              }
           }
         
       }
        return false;
    
    }
}
