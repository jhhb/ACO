/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aco;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jamesboyle
 */
public class AntColony {
    
    private int numberOfAnts;
    private int numberOfIterations;
    private double evaporationFactor;
    private double elitismFactor;
    private double pheromeWearAwayEValue;
    private double pheromoneWearAwayTZero;
    private double bestLegProbability;
    
    private ArrayList<Ant> ants = new ArrayList<Ant>();
    private int numberOfCities = -1;
    
    private double bestTourLengthSoFar = Double.MAX_VALUE;
    private ArrayList<Integer> bestTourSoFar = new ArrayList<>();    
    
    private double alpha = 0;
    private double beta  = 0;
    private Random random = new Random();
    
    /*
    You will want to write your code so that a run can be terminated:
        after a max number of iterations is reached, or
        after a tour has been found that is no more than a specified percentage over the optimal, or
        both, whicever comes first
    
    Also want to allow code to stop after certain amount of time has elapsed
    */
    public AntColony(int numberOfAnts, int numberOfIterations, double alpha,
            double beta, double evaporationFactor, double elitismFactor, 
            double pheromoneWearAwayEValue, double pheromoneWearAwayTZero, double bestLegProbability){
        
        this.numberOfAnts = numberOfAnts;
        this.numberOfIterations = numberOfIterations;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationFactor = evaporationFactor;
        this.elitismFactor = elitismFactor;
        this.pheromeWearAwayEValue = pheromoneWearAwayEValue;
        this.pheromoneWearAwayTZero = pheromoneWearAwayTZero;
        this.bestLegProbability = bestLegProbability;  
    }
    
    /* Initializes ants ArrayList with each ant with a random tour position */
    public void initializeAnts(){
        if(!this.ants.isEmpty()){
            this.ants.clear();
        }   
        for(int i = 0; i < this.numberOfAnts; i++){

            int randomTourPosition = getRandomTourPositionForAnt();
            Ant ant = new Ant(randomTourPosition, this.numberOfCities);
            
            this.ants.add(ant);
        }
    }
    
    public int getNumberOfAnts(){
        return this.numberOfAnts;
    }
    public ArrayList<Ant> getAnts(){
        return this.ants;
    }
    
    private double returnProduct(double distance, double pheromone){  
        
        return Math.pow(pheromone,  this.alpha) * Math.pow( 1.00 / distance, this.beta);    
    }
    
    public double getDeltaTBSF(int firstCity, int secondCity){    
        
        for(int x = 0; x < this.bestTourSoFar.size()-1; x++){
            if(this.bestTourSoFar.get(x) == firstCity ){
                if(this.bestTourSoFar.get(x+1) == secondCity){
                    return 1.0 / this.bestTourLengthSoFar; 
                }
            }
        }
         return 0.00;
    }
    
    public void setBestTourLengthSoFarAndAddToTourHistory(){
        
        double bestSoFar = this.bestTourLengthSoFar;
        int bestIndex = -1;
        for(int i = 0; i < this.ants.size(); i++){
            if(ants.get(i).getCurrentTourLength() < bestSoFar){
                bestSoFar = ants.get(i).getCurrentTourLength();
                bestIndex = i;
            }
        }
        
        if(bestIndex != -1){
            this.bestTourSoFar = ants.get(bestIndex).getTourHistory(); 
            this.bestTourLengthSoFar = bestSoFar; 
        }  
    }
    
    public double getBestTourLengthSoFar(){
        return this.bestTourLengthSoFar;
    }
    
    public ArrayList<Integer> getBestTourSoFar(){
        return this.bestTourSoFar;
    }
    
    public double getEvaporationFactor(){
        return this.evaporationFactor;
    }
    
    public double getElitismFactor(){
        return this.elitismFactor;
    }
    
    public int getNumberOfIterations(){
        return this.numberOfIterations;
    }

    public void setNumberOfCities(int numberOfCities){
        this.numberOfCities = numberOfCities;
    }
    public int getRandomTourPositionForAnt(){
        //off by one? dont think so
        return random.nextInt(this.numberOfCities);    
    }
    
    public double getAlpha(){
        return this.alpha;
    }
    
    public double getBeta(){
        return this.beta;
    }
    
    public double getBestLegProbability(){
        return this.bestLegProbability;
    }
    
    public double getEpsilon(){
        return this.pheromeWearAwayEValue;
    }
    
    public double getTzero() {
        return this.pheromoneWearAwayTZero;
    }
    
}
