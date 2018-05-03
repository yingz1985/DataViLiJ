package dataprocessors;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import ui.AppUI;
import vilij.components.DataComponent;
import vilij.templates.ApplicationTemplate;

import java.nio.file.Path;
import javafx.stage.Stage;
import settings.AppPropertyTypes;
import vilij.components.ErrorDialog;

/**
 * This is the concrete application-specific implementation of the data component defined by the Vilij framework.
 *
 * @author Ritwik Banerjee
 * @see DataComponent
 */
public class AppData implements DataComponent {

    private TSDProcessor        processor;
    private ApplicationTemplate applicationTemplate;

    public AppData(ApplicationTemplate applicationTemplate) {
        this.processor = new TSDProcessor();
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    public void loadData(Path dataFilePath) {
        
        // TODO: NOT A PART OF HW 1
        
        
    }
    


    public void loadData(String dataString) throws Exception{
        Boolean caught = false;
        try
        {
          processor.processString(dataString);
        }
         catch(NumberFormatException e)
         {
                       
                        ErrorDialog dialog = ErrorDialog.getDialog();
                          dialog.show(AppPropertyTypes.NUMBER_FORMAT_EXCEPTION.toString(),
                                  applicationTemplate.manager.getPropertyValue(AppPropertyTypes.NUMBER_FORMAT_EXCEPTION.name())+
                                  "\n"+applicationTemplate.manager.getPropertyValue(
                        AppPropertyTypes.ERROR_LINE.name())+processor.getLineNum());
                        caught = true;
                         
        }
        catch(IllegalArgumentException x)
        {
                         
                          ErrorDialog dialog = ErrorDialog.getDialog();
                          dialog.show(applicationTemplate.manager.getPropertyValue(
                            AppPropertyTypes.INVALID_DATA_EXCEPTION.name()),
                            applicationTemplate.manager.getPropertyValue(
                            AppPropertyTypes.NAME_ERROR_MSG.name())+"\n"+
                            applicationTemplate.manager.getPropertyValue(
                            AppPropertyTypes.ERROR_LINE.name())+processor.getLineNum());
                            caught = true;
                          
                         
        }
               
                  
        catch(NullPointerException k)
        {
                        caught=true;
                        ErrorDialog dialog = ErrorDialog.getDialog();
                          dialog.show(AppPropertyTypes.IDENTICAL_NAME_EXCEPTION.toString(),
                                  applicationTemplate.manager.getPropertyValue(AppPropertyTypes.IDENTICAL_NAME_EXCEPTION.name())+
                                  processor.getName()+"\n"+applicationTemplate.manager.getPropertyValue(
                                AppPropertyTypes.ERROR_LINE.name())+processor.getLineNum()+
                                          "\n");
        }
        catch (Exception e)
        {
                      
                        caught=true;
                      ErrorDialog dialog = ErrorDialog.getDialog();
                      dialog.show(AppPropertyTypes.INVALID_SPACING_ERRORS.toString()
                      ,applicationTemplate.manager.getPropertyValue(AppPropertyTypes.INVALID_SPACING_ERRORS.name())
                      +"\n"+applicationTemplate.manager.getPropertyValue(
                            AppPropertyTypes.ERROR_LINE.name())+processor.getLineNum());
                       
        }
        if(caught)
        {
            processor.clear();
            throw new Exception();
        }
         // ((AppUI) applicationTemplate.getUIComponent()).getChart().getData().clear();
          
          
        

    }
    public int lineNum(){
        return processor.getLineNum();
    }

    
     
    @Override
    public void saveData(Path dataFilePath) {
        // TODO: NOT A PART OF HW 1
        
        
        try (PrintWriter writer = new PrintWriter(Files.newOutputStream(dataFilePath))) {
            writer.write(((AppUI) applicationTemplate.getUIComponent()).returnActualText());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void saveData(Path dataFilePath, String text)
    {
        try (PrintWriter writer = new PrintWriter(Files.newOutputStream(dataFilePath))) {
            writer.write(text);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    
    public TSDProcessor getProcessor()
    {
        return processor;
    }
    @Override
    public void clear() {
        processor.clear();
    }

    public void displayData() {
        processor.toChartData(((AppUI) applicationTemplate.getUIComponent()).getChart());
    }
}
