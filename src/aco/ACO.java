/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aco;

import java.util.ArrayList;

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
                int currentCity = ants.get(i).getCurrentTourPosition();
                ants.get(i).setVisited(currentCity);
                
                boolean needIndexOfUnvisited = true;
                boolean probabilityNotSatisfied = true;
                int nextCityIndex = -1;
                //while we havent matched the probability calculation
                while(probabilityNotSatisfied){
                    //while we still need an unvisited city
                    while(needIndexOfUnvisited){
                        int newCityIndex = antColony.getRandomTourPositionForAnt();
                        if(!ants.get(i).hasVisited(newCityIndex)){
                            needIndexOfUnvisited = false;
                            nextCityIndex = newCityIndex;
                        }
                    }
                double distanceBetweenCities = map.getDistance(currentCity, nextCityIndex);
                probabilityNotSatisfied = antColony.citiesSatisfyProbability(ants.get(i), distanceBetweenCities);  // ants.get(i).cititesSatisfyProbability(currentCity, nextCityIndex);
                }
                //antColony.transferControlToAntColony();
            }           
        }
            //endfor ants
    }        
}
    
  