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
    
    private HashMap<RadioButton,ConfigWindow> types;
    
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
            
    public void configure(int max,int update,boolean cont)
    {
        
        MaxIterations = max;
        UpdateInterval = update;
        toContinue = cont;
        configured = true;
    }
    public boolean emptyData()
    {
        return configured;
    }
    public boolean isCluster()
    {
        return algorithm.equals("Clustering");
    }
    
}
