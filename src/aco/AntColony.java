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
    private double pheromoneInfluence;
    private double heuristicInfluence;
    private double evaporationFactor;
    private double elitismFactor;
    private double pheromeWearAwayEValue;
    private double pheromoneWearAwayTZero;
    private double bestLegProbability;
    
    private ArrayList<Ant> ants = new ArrayList<Ant>();
    private int numberOfCities = -1;
    
    private Random random = new Random();
    
    /*
    You will want to write your code so that a run can be terminated:
        after a max number of iterations is reached, or
        after a tour has been found that is no more than a specified percentage over the optimal, or
        both, whicever comes first
    
    Also want to allow code to stop after certain amount of time has elapsed
    */
    public AntColony(int numberOfAnts, int numberOfIterations, double pheromoneInfluence,
            double heuristicInfluence, double evaporationFactor, double elitismFactor, 
            double pheromoneWearAwayEValue, double pheromoneWearAwayTZero, double bestLegProbability){
        
        this.numberOfAnts = numberOfAnts;
        this.numberOfIterations = numberOfIterations;
        this.pheromoneInfluence = pheromoneInfluence;
        this.heuristicInfluence = heuristicInfluence;
        this.evaporationFactor = evaporationFactor;
        this.elitismFactor = elitismFactor;
        this.pheromeWearAwayEValue = pheromoneWearAwayEValue;
        this.pheromoneWearAwayTZero = pheromoneWearAwayTZero;
        this.bestLegProbability = bestLegProbability;  
    }
    
    /* Initializes ants ArrayList with each ant with a random tour position */
    public void initializeAnts(){
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
    
    
    public boolean citiesSatisfyProbability(Ant ant, double distanceBetweenCurrentAndCandidateCity){
        //THANK GOD. WE have the ant, the current city, and the candidate city distance
        //Now we can do the probability calculations and return true or false to say if we have to keep trying more or
        //if we can transfer control back to the ant colony class.
    
        
        
    return true;
    }
    
    
//    private double calculatePsubIJ(){
//        
//    }
//    
//    private double calculateTsubIJ(){
//        
//    }
//    
//    private double calculateDeltaTBestSoFar(){
//        
//    }
    
    public void setNumberOfCities(int numberOfCities){
        this.numberOfCities = numberOfCities;
    }
    
    public int getRandomTourPositionForAnt(){
        //off by one? dont think so
        return random.nextInt(this.numberOfCities);    
    }
    
        
    private void runTours(ArrayList<Ant> ants){
        

    }
    
}
