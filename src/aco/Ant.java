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
    
    private double pheromone;
    
    private HashMap<Integer,Integer> mapOfIndexToVisitedBoolean = new HashMap<Integer,Integer>();

    /* Initialize Ant with randomTourPosition between 0 inclusive and number of cities exclusive */
    
    public Ant(int randomTourPosition, int numCities) {
        this.tourHistory = new ArrayList<Integer>();
        this.currentTourLength = 0;
        this.currentTourPosition = randomTourPosition;
        this.pheromone = 0;
    }
    
    public int getCurrentTourPosition(){
        return this.currentTourPosition;
    }
    
    public void setCurrentTourPosition(int newPosition){
        this.currentTourPosition = newPosition;
    }
    
    public void updateCurrentTourHistory(int newPosition){
        this.tourHistory.add(newPosition);
    }
    
    public void updateCurrentTourLength(double distance){
        this.currentTourLength += distance;
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
    
    //should be fine . No wrap around in tour? Also, check last index maybe/
    public boolean checkIJInTourHistory(int i, int j){
        
        for(int x = 0; x < this.tourHistory.size()-1; x++){
            if(this.tourHistory.get(x) == i ){
                if(this.tourHistory.get(x+1) == j){
                    return true;   
                }
            }
        }
       return false;
    }
    
    public double getCurrentTourLength(){
        return this.currentTourLength;
    }
           
            
}
