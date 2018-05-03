/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dataprocessors.TSDProcessor;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Bagel
 */
public class parseDataTest
{
    
    public parseDataTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("* parseDataTest: @BeforeClass method");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("* parseDataTest: @AfterClass method");
    }

    /**
     * Test of processString method using an empty string, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *   IllegalArgumentException should be thrown
     *   note: IllegalArgumentException is thrown is a line does not start with a @ tag
     *         Also, save button functionality is not enabled if text area is empty 
     *         but this does not prevent a pass in of an empty *.tsd file, 
     *         if externally created 
     *   Should expect exception thrown to cause lineNum to reset to 0
     *   
     */
    @Test (expected = IllegalArgumentException.class)
    public void testProcessString1() throws Exception {
        System.out.println("* parseDataTest: emptyStringCheck");
        String tsdString = "";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),0);

    }
    
    /**
     * Test of processString method using a valid string, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *   A single instance of a data point has been added to the processor
     *   no exception is expected to be thrown 
     *   
     */
    @Test 
    public void testProcessString2() throws Exception {
        System.out.println("* parseDataTest: validStringCheck");
        String tsdString = "@male#1	male	96.3,70";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),1);

    }
    
     /**
     * Test of processString method using a string with valid data but not tab-separated, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *     ArrayIndexOutOfBoundsException exception should be thrown 
     *     note: the parser method will expect the line of data to be valid
     *           and will parse according to correct format
     *   
     */
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testProcessString3() throws Exception {
        System.out.println("* parseDataTest: spaceSeparatedFormatCheck");
        String tsdString = "@male#1 male	96.3,70";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),0);

    }
    
    /**
     * Test of processString method using a string with numbers that cannot be displayed on chart, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *     NumberFormatException is manually invoked from the method
     *     note: Nan and Infinity are valid double values 
     *           but are not acceptable values to be plotted onto chart 
     *   
     */
    @Test (expected = NumberFormatException.class)
    public void testProcessString4() throws Exception {
        System.out.println("* parseDataTest: LargeNumbersCheck");
        String tsdString = "@male#1	male	NaN,Infinity";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),0);

    }
    
    
     /**
     * Test of processString method using negative numbers, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *   A single instance of a data point has been added to the processor
     *   no exception is expected to be thrown 
     *   line number should be incremented and ==1 
     *   
     */
    @Test 
    public void testProcessString5() throws Exception {
        System.out.println("* parseDataTest: NegativeNumbersCheck");
        String tsdString = "@male#1	male	-500,-20";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),1);

    }
    
    /**
     * Test of processString method using decimal numbers as coordinates, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *   A single instance of a data point has been added to the processor
     *   no exception is expected to be thrown 
     *   line number should be incremented and ==1 
     *   
     */
    @Test
    public void testProcessString6() throws Exception {
        System.out.println("* parseDataTest: DecimalValuesCheck");
        String tsdString = "@male#1	male	3.6,-20.7";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),1);

    }
    
    /**
     * Test of processString method using natural exponential numbers as coordinates, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *   A single instance of a data point has been added to the processor
     *   no exception is expected to be thrown 
     *   line number should be incremented and ==1 
     *   
     */
    @Test
    public void testProcessString7() throws Exception {
        System.out.println("* parseDataTest: ExponentialValuesCheck");
        String tsdString = "@male#1	male	7.6E+7,1E-5";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),1);

    }
    
    /**
     * Test of processString method using letters as coordinates, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *      NumberFormatException should be thrown
     *      Parser expects tsdString to contain two valid double coordinates
     */
    @Test (expected = NumberFormatException.class)
    public void testProcessString8() throws Exception {
        System.out.println("* parseDataTest: InvalidCoordinateCheck");
        String tsdString = "@male#1	male	k.m,3";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),0);

    }
    
        /**
     * Test of processString method using duplicate instance names, of class TSDProcessor.
     * @throws java.lang.Exception
     * @PostCondition:
     *      NullPointerException should be thrown
     *      Parser expects tsdString to contain instances with different names
     *      Did not create custom exceptions calls for duplicate names and used pre-existing ones
     */
    @Test (expected = NullPointerException.class)
    public void testProcessString9() throws Exception {
        System.out.println("* parseDataTest: DuplicateNameCheck");
        String tsdString = "@male#1	male	96.3,70\n" +
                           "@male#1	male	96.7,71";
        TSDProcessor instance = new TSDProcessor();
        instance.processString(tsdString);
        assertEquals(instance.getLineNum(),0);

    }
    
    
    
    

}
