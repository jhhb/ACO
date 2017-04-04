/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

CURRENTLY, we fix a problem by setting distance to 0.1

Update pheromones is super slow.

 */

package aco;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jamesboyle
 */

        
public class ACO {
    
    private static List<List<Double>> globalDistances; // = new ArrayList<List<Double>>();
    private static List<List<Double>> globalPheromones; // = new ArrayList<List<Double>>();
    private static int globalNumberOfCities;
    private static Random rand = new Random();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Map map = new  Map("/Users/jamesboyle/NetBeansProjects/ACO/ALL_tsp/att48.tsp");
        map.initializeMap();
        globalDistances = map.getDistances();
        globalPheromones = map.getPheromones();
        
        int numberOfCities = map.getNumberOfCities();
        
        globalNumberOfCities = numberOfCities;     
        /*Usage: new AntColony(ants, iterations, alpha, beta, p, elitismNumAnts, epsilon, t0, q0) */
        AntColony antColony = new AntColony(30, 50, 1, 3, 0.1, 30, 0.1, 0, 0.9);
        antColony.setNumberOfCities(numberOfCities);
        
//        double max = 0;
//        for(int i = 0; i < globalDistances.size(); i++){
//            for(int z = 0; z < globalDistances.size(); z++){
//                
//                System.out.print(globalDistances.get(i).get(z));
//
//                if(globalDistances.get(i).get(z) > max){
//                    max = globalDistances.get(i).get(z);
//                }
//                
//            }
//            System.out.println("\n");
//
//        }
//        System.out.println(max);
        
        runElitistACO(antColony);
           
    }
    
    private static void runElitistACO(AntColony antColony){
        
        boolean runTours = true;
        
        int numberOfIterations = antColony.getNumberOfIterations();
        int counter = 0;
        
        
        while(runTours){
            antColony.initializeAnts();
                      
            for(int k = 0; k < globalNumberOfCities; k++){
                ArrayList<Ant> ants = antColony.getAnts();       
                for(int i = 0; i < ants.size(); i++){
                    //all ants are initialized to a starting city 
                    int currentCity = ants.get(i).getCurrentTourPosition();
                    ants.get(i).setVisited(currentCity);
                    
                    
                    

                    ArrayList<Double> unvisitedDistances = getUnvisitedDistancesForAnt(ants.get(i));
                    ArrayList<Double> unvisitedPheromones = getUnvisitedPheromonesForAnt(ants.get(i));
                    //have all unvisited distances, unvisited pheromones 
                    
                    double alpha = antColony.getAlpha();
                    double beta = antColony.getBeta();        
                    int nextCity = getNextCity(ants.get(i), 
                                unvisitedDistances,
                                unvisitedPheromones, alpha, beta); 
                    
                    ants.get(i).updateCurrentTourHistory(nextCity);
                    ants.get(i).setCurrentTourPosition(nextCity);
                    ants.get(i).updateCurrentTourLength(nextCity);
                } //END ANTS

            }
            System.out.println("Entering update pheromones");
            updatePheromones(antColony); 
            antColony.setBestTourLengthSoFarAndAddToTourHistory();
            
            counter+=1;
            if(counter == numberOfIterations){
                runTours = false;
            }            
        }
        System.out.println(antColony.getBestTourLengthSoFar());
        System.out.println(antColony.getBestTourSoFar());
    }
    
    private static void updatePheromones(AntColony antColony){
        for(int i = 0; i < globalPheromones.size(); i++){
            for(int j = 0; j < globalPheromones.get(i).size(); j++){
                
                ArrayList<Ant> ants = antColony.getAnts();
                
                double sumOfAntPheromones = 0.0;
                //this computes the sum
                for(int z = 0; z < ants.size(); z++){
                    if(ants.get(z).checkIJInTourHistory(i, j)){
                        sumOfAntPheromones += (1.0 / ants.get(z).getCurrentTourLength());
                    }    
                }
                
                double eProduct = antColony.getDeltaTBSF(i, j) * antColony.getElitismFactor();
                
                double pheromoneIJProduct = (1.0 - antColony.getEvaporationFactor()) * globalPheromones.get(i).get(j);
                
                double newPheromoneValue = pheromoneIJProduct + sumOfAntPheromones + eProduct;
                setPheromone(i, j, newPheromoneValue);
            }
        }
        System.out.println("Leaving update pheromones");
    }
    
    private static ArrayList<Double> getUnvisitedDistancesForAnt(Ant ant){
        
        ArrayList<Integer> unvisitedCities = ant.getUnvisitedCities();
        
        ArrayList<Double> distances = new ArrayList<>();

        for(int i = 0; i < unvisitedCities.size(); i++){
            
            int unvisitedCityIndex = unvisitedCities.get(i);
            
            distances.add(globalDistances.get(ant.getCurrentTourPosition()).get(unvisitedCityIndex));
        }
        return distances;
          
    }
    
    private static ArrayList<Double> getUnvisitedPheromonesForAnt(Ant ant){
        
        ArrayList<Integer> unvisitedCities = ant.getUnvisitedCities();
        
        ArrayList<Double> pheromones = new ArrayList<>();

        for(int i = 0; i < unvisitedCities.size(); i++){
            
            int unvisitedCityIndex = unvisitedCities.get(i);
            
            pheromones.add(globalPheromones.get(ant.getCurrentTourPosition()).get(unvisitedCityIndex));
        }
        return pheromones;
        
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
    
    private static int getNextCity(Ant ant, ArrayList<Double> unvisitedDistances, ArrayList<Double> unvisitedPheromones,
            double alpha, double beta){
        
        double denominatorSum = 0.0;

        ArrayList<Double> arrayOfNumerators = new ArrayList<>();
        
        for(int i = 0; i < ant.getUnvisitedCities().size(); i++){
            arrayOfNumerators.add(returnProduct(unvisitedDistances.get(i), unvisitedPheromones.get(i), alpha, beta));          
        }
        
        for(int i = 0; i < unvisitedDistances.size(); i++){
            
            double returnProductResult = returnProduct(unvisitedDistances.get(i), unvisitedPheromones.get(i), alpha, beta);
            if(returnProductResult > 1){
                System.exit(-1);
            }
            denominatorSum+= returnProductResult;
        }
  
        double randomValue = rand.nextDouble() * denominatorSum;
        
        double runningSum = 0.0;
        
        int cityToGoToIndex = -1;
        for(int i = 0; i< ant.getUnvisitedCities().size(); i++ ){
            runningSum += arrayOfNumerators.get(i);
            if(randomValue <= runningSum){
                cityToGoToIndex = i;
                break;
            }
        }
        
        return ant.getUnvisitedCities().get(cityToGoToIndex);
    }
    
    private static double returnProduct(double distance, double pheromone, double alpha, double beta){  
        
        return Math.pow(pheromone, alpha) * Math.pow( 1.00 / distance, beta);    
    }
    

}
    
  