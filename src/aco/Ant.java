/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jamesboyle
 */
public class Ant {
    
    private double currentTourLength;
    private ArrayList<Integer> tourHistory;
    private int currentTourPosition;
    
    private double pheromone;
    
   // private int changingNumberOfCities = 0;
    /* Initialize Ant with randomTourPosition between 0 inclusive and number of cities exclusive */
    
    private Random random = new Random();
    private ArrayList<Integer> unvisitedCities = new ArrayList<Integer>();
    private int numCities = 0;
    
    private int initialCityIndex = 0;
    
    public Ant(int randomTourPosition, int numCities) {
        this.tourHistory = new ArrayList<Integer>();
        this.currentTourLength = 0;
        this.currentTourPosition = randomTourPosition;
        this.pheromone = 0;
        this.numCities = numCities;
        this.initialCityIndex = randomTourPosition;
       // this.changingNumberOfCities = numCities;
        initializeUnvisitedCities();
    }
    
    private void initializeUnvisitedCities(){
        for(int i = 0; i < this.numCities; i++){
            this.unvisitedCities.add(i);
        }
    }
    
    public ArrayList<Integer> getTourHistory(){
        return this.tourHistory;
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
    
    public void setVisited(int city){
        //remove item at index    
        int indexToRemoveAt = -1;
        
        for(int i = 0; i < this.unvisitedCities.size(); i++){
            if(this.unvisitedCities.get(i) == city){
                indexToRemoveAt = i;
            }
        }

        if(indexToRemoveAt == -1){
            System.exit(-2);
        }
        this.unvisitedCities.remove(indexToRemoveAt);
        
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
    
    public int getInitialCityIndex(){
        return this.initialCityIndex;
    }
    
    public int getRandomCityForAnt(){
           
        int newIndex = random.nextInt(this.unvisitedCities.size());
                
        return newIndex;
         
    }
    
    public int getSizeOfUnvisitedCities(){
        return this.unvisitedCities.size();
    }      
    
    public ArrayList<Integer> getUnvisitedCities(){
        return this.unvisitedCities;
        
    }
}