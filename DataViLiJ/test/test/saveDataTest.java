/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dataprocessors.AppData;
import dataprocessors.TSDProcessor;
import java.awt.TextArea;
import java.io.File;
import static java.io.File.separator;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import settings.AppPropertyTypes;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;

/**
 *
 * @author Bagel
 */
public class saveDataTest
{
    
    public saveDataTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("* saveDataTest: @BeforeClass method");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("* saveDataTest: @AfterClass method");
    }




    /**
     * Test of saveData method, of class AppData.
     * 
     */
    @Test
    public void testSaveData() throws FileNotFoundException {
        System.out.println("* saveDataTest: saveStringCheck");
        TextArea text = new TextArea();
        text.setText("@instance1	label1	10,10");
        
        String filePath  = "data-vilij/resources/data/testSave.tsd";
        //Path p = Paths.get(filePath);
        File file = new File(filePath);
        AppData instance = new AppData(null);
        //String str = "save data test";
        instance.saveData(file.toPath(),text.getText());
        Scanner k = new Scanner(file);
        String m = "";
        while(k.hasNextLine())
        {
            m+=k.nextLine();
        }
        assertEquals(m,text.getText());
        
        // TODO review the generated test code and remove the default call to fail.
    }


    
}
