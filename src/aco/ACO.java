/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aco;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jamesboyle
 */

        
public class ACO {
    
    private static List<List<Double>> globalDistances; // = new ArrayList<List<Double>>();
    private static List<List<Double>> globalPheromones; // = new ArrayList<List<Double>>();
    private static int globalNumberOfCities;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("hello, world!");
        
        Map map = new  Map("/Users/jamesboyle/NetBeansProjects/ACO/ALL_tsp/d2103.tsp");
        
        
        map.initializeMap();
       // map.printDistances();
        globalDistances = map.getDistances();
        globalPheromones = map.getPheromones();
        
        
        int numberOfCities = map.getNumberOfCities();
        
        globalNumberOfCities = numberOfCities;

        
        /*Usage: new AntColony(ants, iterations, alpha, beta, p, elitismNumAnts, epsilon, t0, q0) */
        AntColony antColony = new AntColony(15, 50, 1, 3, 0.1, 15, 0.1, 0, 0.9);
        antColony.setNumberOfCities(numberOfCities);
        antColony.initializeAnts();
        
        runElitistACO(antColony);
        
       
            //endfor ants
    }
    
    private static void runElitistACO(AntColony antColony){
        
        boolean runTours = true;
        
        int numberOfIterations = antColony.getNumberOfIterations();
        int counter = 0;
        
        while(runTours){
            
            for(int k = 0; k < globalNumberOfCities; k++){
                ArrayList<Ant> ants = antColony.getAnts();       
                for(int i = 0; i < ants.size(); i++){
                    //all ants are initialized to a starting city 
                    int currentCity = ants.get(i).getCurrentTourPosition();
                    ants.get(i).setVisited(currentCity);

                    boolean needIndexOfUnvisited = true;
                    boolean probabilityNotSatisfied = true;
                    int nextCityIndex = -1;

                    ArrayList<Double> unvisitedDistances = getUnvisitedDistancesForAnt(ants.get(i));
                    ArrayList<Double> unvisitedPheromones = getUnvisitedPheromonesForAnt(ants.get(i));

                    //have all unvisited distances, unvisited pheromones 
                    double distanceBetweenCities = 0.0;
                    double pheromoneBetweenCities = 0.0;
                    while(probabilityNotSatisfied){
                        //while we still need an unvisited city
                        while(needIndexOfUnvisited){
                           // System.out.println("need unvisited");
                            int newCityIndex = antColony.getRandomTourPositionForAnt();
                            if(!ants.get(i).hasVisited(newCityIndex)){
                                needIndexOfUnvisited = false;
                                nextCityIndex = newCityIndex;
                            }
                        }        
                        distanceBetweenCities = getDistance(currentCity, nextCityIndex);
                        pheromoneBetweenCities = getPheromone(currentCity, nextCityIndex);
                        probabilityNotSatisfied = antColony.citiesDontSatisfyProbability(ants.get(i), 
                                distanceBetweenCities, pheromoneBetweenCities, nextCityIndex, unvisitedDistances,
                                unvisitedPheromones);  // ants.get(i).cititesSatisfyProbability(currentCity, nextCityIndex);
                    }                
                    ants.get(i).updateCurrentTourHistory(nextCityIndex);
                    ants.get(i).setCurrentTourPosition(nextCityIndex);
                    ants.get(i).updateCurrentTourLength(distanceBetweenCities);
                    //probability Satisfied, so add to tour
                } //END ANTS
                //System.out.println(k + " cities done");
            }
            System.out.println("Entering update pheromones");
            updatePheromones(antColony); 
            counter+=1;
            if(counter == numberOfIterations){
                runTours = false;
            }            
        }
        System.out.println(antColony.getBestTourLengthSoFar());
        System.out.println(antColony.getBestTourSoFar().toString());
    }
    
    private static void updatePheromones(AntColony antColony){
        System.out.println("Update pheromones");
        System.out.println(globalPheromones.size());
        for(int i = 0; i < globalPheromones.size(); i++){
            System.out.println("i = " + i);
            for(int j = 0; j < globalPheromones.get(i).size(); j++){
                System.out.println("j = " + j);
                
                ArrayList<Ant> ants = antColony.getAnts();
                
                double sumOfAntPheromones = 0.0;
                
                //this computes the sum
                for(int z = 0; z < ants.size(); z++){
                    if(ants.get(z).checkIJInTourHistory(i, j)){
                        sumOfAntPheromones += (1.0 / ants.get(z).getCurrentTourLength());
                       System.out.println("ants");
                    }    
                }
                
                double eProduct = antColony.getDeltaTBSF(i, j) * antColony.getElitismFactor();
                
                double pheromoneIJProduct = (1.0 - antColony.getEvaporationFactor()) * globalPheromones.get(i).get(j);
                
                double newPheromoneValue = pheromoneIJProduct + sumOfAntPheromones + eProduct;
                setPheromone(i, j, newPheromoneValue);
            }
            System.out.println("Index: " + i);

        }
    }
    
    private static ArrayList<Double> getUnvisitedDistancesForAnt(Ant ant){
        
        ArrayList<Double> distances = new ArrayList<>();
        
        for(int i = 0; i < globalNumberOfCities; i++){
            
            if(!ant.hasVisited(i)){
                distances.add(globalDistances.get(ant.getCurrentTourPosition()).get(i));
            }           
        } 
        return distances;   
    }
    
    private static ArrayList<Double> getUnvisitedPheromonesForAnt(Ant ant){
        
        ArrayList<Double> distances = new ArrayList<>();
        
        for(int i = 0; i < globalNumberOfCities; i++){
            
            if(!ant.hasVisited(i)){
                distances.add(globalDistances.get(ant.getCurrentTourPosition()).get(i));
            }           
        } 
        return distances;   
    }
    
    private static double getDistance(int city1, int city2){
        return globalDistances.get(city1).get(city2);
    }
    
    private static double getPheromone(int city1, int city2){
        return globalPheromones.get(city1).get(city2);
    }
    
    private static void setPheromone(int city1, int city2, double pheromoneValue){
        globalPheromones.get(city1).set(city2, pheromoneValue);
    }
    
    
}
    
  