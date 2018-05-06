/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dataprocessors.DataSet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import runningEvents.AlgorithmContainer;
import runningEvents.KMeansClusterer;
import runningEvents.RandomClassifier;
import runningEvents.RandomClusterer;

/**
 *
 * @author Bagel
 */
public class configurationTest
{
    
    public configurationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
         System.out.println("* configurationTest: @BeforeClass method");
    }
    
    @AfterClass
    public static void tearDownClass() {
         System.out.println("* configurationTest: @AfterClass method");
    }




    /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with Integer.Max_Value for maxIterations and UpdateIntervals
     */
    @Test
    public void testConfigureRandomClassifier() {
        System.out.println("* configurationTest: RandomClassifierConfigCheck");
        RandomClassifier c = new RandomClassifier(new DataSet(),0,0,false,0,null);
        String maxText = "2147483647";
        String updateText = "2147483647";
        boolean cont = false;
        String num = "2";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClassifier(null,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,Double.valueOf(maxText)==c.getMaxIterations());
        assertEquals(true,Double.valueOf(updateText)==c.getUpdateInterval());
        
    }
    
     /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with numeric values out of range
     */
    @Test
    public void testConfigureRandomClassifierWithOutOfRangeinputs() {
        System.out.println("* configurationTest: RandomClassifierConfigCheckWithOutOfRangeInputs");
        RandomClassifier c = new RandomClassifier(null,0,0,false,0,null);
        String maxText = "0";
        String updateText = "0";
        boolean cont = false;
        String num = "5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClassifier(null,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1000==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        
    }
     
    /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with numeric values that are negative
     */
    @Test
    public void testConfigureRandomClassifierWithNegativeinputs() {
        System.out.println("* configurationTest: RandomClassifierConfigCheckWithNegativeInputs");
        RandomClassifier c = new RandomClassifier(null,0,0,false,0,null);
        String maxText = "-5";
        String updateText = "-10";
        boolean cont = false;
        String num = "5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClassifier(null,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1000==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        
    }
    
     /**
     * Test of configure method, of class AlgorithmContainer.
     * #note: MaxIteration and update iterations are both set to value 1
     *       any number less than that would qualify as a bad input
     *        and will be changed to default values in the back-end
     */
    @Test
    public void testConfigureRandomClassifierWithBoundaryValues() {
        System.out.println("* configurationTest: RandomClassifierConfigCheckWithBoundaryValues");
        RandomClassifier c = new RandomClassifier(null,0,0,false,0,null);
        String maxText = "1";
        String updateText = "1";
        boolean cont = false;
        String num = "5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClassifier(null,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        
    }
    
     /**
     * Test of configure method, of class AlgorithmContainer.
     */
    @Test
    public void testConfigureRandomClassifierWithInvalidInputs() {
        System.out.println("* configurationTest: RandomClassifierConfigCheckWithInvalidInputs");
        RandomClassifier c = new RandomClassifier(null,0,0,false,0,null);
        String maxText = "k";
        String updateText = "m";
        boolean cont = false;
        String num = "5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClassifier(null,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1000==c.getMaxIterations()); 
        assertEquals(true,1==c.getUpdateInterval());
        
    }
    
     /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with Integer.Max_Value for maxIterations and UpdateIntervals
     */
    @Test
    public void testConfigureRandomClusterer() {
        System.out.println("* configurationTest: RandomClustererConfigCheck");
        RandomClusterer c = new RandomClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "2147483647";
        String updateText = "2147483647";
        boolean cont = false;
        String num = "2";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClusterer(new DataSet(),instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,Double.valueOf(maxText)==c.getMaxIterations());
        assertEquals(true,Double.valueOf(updateText)==c.getUpdateInterval());
        assertEquals(true,Double.valueOf(num)==c.getNumberOfClusters());
        
    }
    
         /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with numeric values out of range
     */
    @Test
    public void testConfigureRandomClustererWithOutOfRangeinputs() {
        System.out.println("* configurationTest: RandomClustererConfigCheckWithOutOfRangeInputs");
        RandomClusterer c = new RandomClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "0";
        String updateText = "0";
        boolean cont = false;
        String num = "5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        DataSet n = new DataSet();
        n.fromTSDFile("@male#1	male	96.3,70\n" +
        "@male#2	male	96.7,71\n" +
        "@male#3	male	96.9,74\n" +
        "@male#4	male	97.0,80");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClusterer(n,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1000==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        assertEquals(true,4==c.getNumberOfClusters());
        
    }
    
     /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with numeric values out of range
     */
    @Test
    public void testConfigureRandomClustererWithNegativeinputs() {
        System.out.println("* configurationTest: RandomClustererConfigCheckWithNegativeInputs");
        RandomClusterer c = new RandomClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "-5";
        String updateText = "-10";
        boolean cont = false;
        String num = "-5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClusterer(new DataSet(),instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1000==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        assertEquals(true,2==c.getNumberOfClusters());
        
    }
    
        
     /**
     * Test of configure method, of class AlgorithmContainer.
     * #note: MaxIteration and update iterations are both set to value 1
     *       any number less than that would qualify as a bad input
     *        and will be changed to default values in the back-end
     *        NumberOfClusters is set to 2, since algorithm requires at least 2 
     *          and will not work for numbers less than 2
     * 
     */
    @Test
    public void testConfigureRandomClustererWithBoundaryValues() {
        System.out.println("* configurationTest: RandomClustererConfigCheckWithBoundaryValues");
        RandomClusterer c = new RandomClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "1";
        String updateText = "1";
        boolean cont = false;
        String num = "2";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new RandomClusterer(new DataSet(),instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        assertEquals(true,2==c.getNumberOfClusters());
        
    }
    


    
    
         /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with Integer.Max_Value for maxIterations and UpdateIntervals
     */
    @Test
    public void testConfigureKMeansClusterer() {
        System.out.println("* configurationTest: KMeansClustererConfigCheck");
        KMeansClusterer c = new KMeansClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "2147483647";
        String updateText = "2147483647";
        boolean cont = false;
        String num = "2";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new KMeansClusterer(new DataSet(),instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,Double.valueOf(maxText)==c.getMaxIterations());
        assertEquals(true,Double.valueOf(updateText)==c.getUpdateInterval());
        assertEquals(true,Double.valueOf(num)==c.getNumberOfClusters());
        
    }
    
         /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with numeric values out of range
     */
    @Test
    public void testConfigureKMeansClustererWithOutOfRangeinputs() {
        System.out.println("* configurationTest: KMeansClustererConfigCheckWithOutOfRangeInputs");
        KMeansClusterer c = new KMeansClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "0";
        String updateText = "0";
        boolean cont = false;
        String num = "5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        DataSet n = new DataSet();
        n.fromTSDFile("@male#1	male	96.3,70\n" +
        "@male#2	male	96.7,71\n" +
        "@male#3	male	96.9,74\n" +
        "@male#4	male	97.0,80");
        c = new KMeansClusterer(n,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1000==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        assertEquals(true,4==c.getNumberOfClusters());
        
    }
    
        /**
     * Test of configure method, of class AlgorithmContainer.
     *     Tested with numeric values out of range
     */
    @Test
    public void testConfigureKMeansClustererWithNegativeinputs() {
        System.out.println("* configurationTest: KMeansClustererConfigCheckWithNegativeInputs");
        KMeansClusterer c = new KMeansClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "-5";
        String updateText = "-10";
        boolean cont = false;
        String num = "-5";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        c = new KMeansClusterer(new DataSet(),instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1000==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        assertEquals(true,2==c.getNumberOfClusters());
        
    }
    
        
     /**
     * Test of configure method, of class AlgorithmContainer.
     * #note: MaxIteration and update iterations are both set to value 1
     *       any number less than that would qualify as a bad input
     *        and will be changed to default values in the back-end
     *        NumberOfClusters is set to 2, since algorithm requires at least 2 
     *          and will not work for numbers less than 2
     * 
     */
    @Test
    public void testConfigureKMeansClustererWithBoundaryValues() {
        System.out.println("* configurationTest: KMeansClustererConfigCheckWithBoundaryValues");
        KMeansClusterer c = new KMeansClusterer(new DataSet(),0,0,false,0,null);
        String maxText = "1";
        String updateText = "1";
        boolean cont = false;
        String num = "2";
        AlgorithmContainer instance = new AlgorithmContainer("");
        instance.configure(maxText, updateText, cont, num, c);
        DataSet n = new DataSet();
        n.fromTSDFile("@male#1	male	96.3,70\n" +
        "@male#2	male	96.7,71\n" +
        "@male#3	male	96.9,74\n" +
        "@male#4	male	97.0,80");
        c = new KMeansClusterer(n,instance.getMaxIterations(),
                instance.getUpdateInterval(),instance.tocontinue(),instance.getLabelNum(),null);
        assertEquals(true,1==c.getMaxIterations());  //bad inputs are set to default
        assertEquals(true,1==c.getUpdateInterval());
        assertEquals(true,2==c.getNumberOfClusters());
        
    }
    


    
    
    
    
}
