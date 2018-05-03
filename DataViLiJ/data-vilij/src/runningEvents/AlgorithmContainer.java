/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runningEvents;

import java.util.HashMap;
import java.util.HashSet;
import javafx.scene.control.RadioButton;
import ui.ConfigWindow;

/**
 *
 * @author Bagel
 */
public class AlgorithmContainer
{
    private String algorithm;
    private int MaxIterations;

    private int UpdateInterval;

    private boolean toContinue;
    
    private boolean configured;
    private int label;
    
    private HashMap<RadioButton,ConfigWindow> types;
    
    public int getMaxIterations()
    {
        return MaxIterations;
    }
    public int getUpdateInterval()
    {
        return UpdateInterval;
    }
    public boolean tocontinue()
    {
        return toContinue;
    }
    public AlgorithmContainer(String algorithm)
    {
        this.algorithm = algorithm;
        types = new HashMap<>();
        
    }
    public void setAlg(String alg)
    {
        algorithm = alg;
    }
    public void addType(RadioButton type,ConfigWindow window)
    {
        if(!types.containsKey(type))
        
            types.put(type,window );
    }
    public Object[] returnWindows()
    {
       HashSet result = new HashSet();
       types.values().forEach((k) ->
        {
            result.add(k);
        });
       return result.toArray();
       
    }
    public Object[] returnTypes()
    {
        return types.keySet().toArray();
    }
            
    public ConfigWindow getWindow(RadioButton type)
    {
        if(types.containsKey(type))
         return types.get(type);
        return null;
        
    }
            
    public void configure(int max,int update,boolean cont,int label)
    {
        
        MaxIterations = max;
        UpdateInterval = update;
        toContinue = cont;
        configured = true;
        this.label = label;
    }
    public void configure(String maxText,String updateText, boolean cont, String num,Algorithm alg)
    {
        try
        {
            int max = Integer.valueOf(maxText);
            if(max>0)
                MaxIterations = max;
            else
                MaxIterations = 1000;
            
        }
        catch(NumberFormatException x)
        {
            MaxIterations = 1000;
        }
        try
        {
           int update = Integer.valueOf(updateText);
           if(update>0)
             UpdateInterval = update;
           else
               UpdateInterval = 1;
        }
        catch(NumberFormatException x)
        {
            UpdateInterval = 1;
        }
        
            try
            {
                int defaultLabel = Integer.valueOf(num);
                if(defaultLabel>0&& defaultLabel<5)
                    label = defaultLabel;
                else 
                    label = defaultLabel;
            
            }
            catch(NumberFormatException x)
            {
                label = 2;
            }
        
        toContinue = cont;
    }

    public int getLabelNum()
    {
        return label;
    }
    public boolean emptyData()
    {
        return configured;
    }
    public boolean isCluster()
    {
        return algorithm.equals("Clustering");
    }
    public String toString()
    {
        return "maxIt: "+MaxIterations+" updateInt: "+UpdateInterval+" toContinue: "+
                toContinue;
    }
    
}
