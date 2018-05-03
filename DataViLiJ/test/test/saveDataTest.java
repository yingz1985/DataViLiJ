/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dataprocessors.AppData;
import java.awt.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
     * @throws java.io.FileNotFoundException
     * @Precondition:
     *     file path exists in the source folder
     * 
     * @PostCondition:
     *     text in text area is saved in designated file successfully   
     *     
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
    
     /**
     * @Precondition:
     *     file path exists in the source folder
     * Test of saveData method, of class AppData using invalid data path.
     * @PostCondition:
     *     text is not saved in file 
     * 
     */
    @Test (expected = FileNotFoundException.class)
    public void testSaveBadData() throws FileNotFoundException {
        System.out.println("* saveDataTest: saveToInvalidPathCheck");
        TextArea text = new TextArea();
        text.setText("@instance1	label1	10,10");
        
        String filePath  = "";
        
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
        assertNotEquals(m,text.getText());
        

    }


    
}
