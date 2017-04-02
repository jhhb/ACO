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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("hello, world!");
        
        Map map = new Map("/Users/jamesboyle/NetBeansProjects/ACO/test.tsp");
        map.initializeMap();
        map.printDistances();
        
        int numberOfCities = map.getNumberOfCities();
        
        /*Usage: new AntColony(ants, iterations, alpha, beta, p, elitismNumAnts, epsilon, t0, q0) */
        AntColony antColony = new AntColony(20, 50, 1, 3, 0.1, 5, 0.1, 0, 0.9);
        antColony.setNumberOfCities(numberOfCities);
        antColony.initializeAnts();
        
        boolean runACO = true;
        while(runACO){
            ArrayList<Ant> ants = antColony.getAnts();       
            //for each ant\
            for(int i = 0; i < ants.size(); i++){
                //all ants are initialized to a starting city 
                int currentCity = ants.get(i).getCurrentTourPosition();
                ants.get(i).setVisited(currentCity);
                
                boolean needIndexOfUnvisited = true;
                boolean probabilityNotSatisfied = true;
                int nextCityIndex = -1;
                
                ArrayList<Double> unvisitedDistances = map.getUnvisitedDistancesForAnt(ants.get(i));
                ArrayList<Double> unvisitedPheromones = map.getUnvisitedPheromonesForAnt(ants.get(i));
                
                //have all unvisited distances, unvisited pheromones                                         
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
                    double distanceBetweenCities = map.getDistance(currentCity, nextCityIndex);
                    double pheromoneBetweenCities = map.getPheromone(currentCity, nextCityIndex);
                    probabilityNotSatisfied = antColony.citiesSatisfyProbability(ants.get(i), 
                            distanceBetweenCities, pheromoneBetweenCities, nextCityIndex, unvisitedDistances,
                            unvisitedPheromones);  // ants.get(i).cititesSatisfyProbability(currentCity, nextCityIndex);
                }
                //update pheromone
                
                //probability Satisfied, so add to tour
            }           
        }
            //endfor ants
    }
   
}
    
  