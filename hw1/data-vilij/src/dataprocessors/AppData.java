package dataprocessors;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import ui.AppUI;
import vilij.components.DataComponent;
import vilij.templates.ApplicationTemplate;

import java.nio.file.Path;

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

    public void loadData(String dataString)throws Exception {
        // TODO for homework 1

          processor.processString(dataString);
          
         // ((AppUI) applicationTemplate.getUIComponent()).getChart().getData().clear();
          
          
        

    }
    public int lineNum(){
        return processor.getLineNum();
    }

    
     
    @Override
    public void saveData(Path dataFilePath) {
        // TODO: NOT A PART OF HW 1
        
        try (PrintWriter writer = new PrintWriter(Files.newOutputStream(dataFilePath))) {
            writer.write(((AppUI) applicationTemplate.getUIComponent()).getTextArea().getText());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    

    @Override
    public void clear() {
        processor.clear();
    }

    public void displayData() {
        processor.toChartData(((AppUI) applicationTemplate.getUIComponent()).getChart());
    }
}
