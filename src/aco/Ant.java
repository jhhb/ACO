/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jamesboyle
 */
public class Ant {
    
    private double currentTourLength;
    private ArrayList<Integer> tourHistory;
    private int currentTourPosition;
    
    private HashMap<Integer,Integer> mapOfIndexToVisitedBoolean = new HashMap<Integer,Integer>();

    /* Initialize Ant with randomTourPosition between 0 inclusive and number of cities exclusive */
    
    public Ant(int randomTourPosition, int numCities) {
        this.tourHistory = new ArrayList<Integer>();
        this.currentTourLength = 0;
        this.currentTourPosition = randomTourPosition;
    }
    
    public int getCurrentTourPosition(){
        return this.currentTourPosition;
    }
    
    public void setVisited(int index){
        this.mapOfIndexToVisitedBoolean.put(index, 1);
    }
    public boolean hasVisited(int index){
        if(this.mapOfIndexToVisitedBoolean.containsKey(index)){
            return true;
        }
        return false;
    }
    

            
            
}
