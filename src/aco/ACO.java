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
        
public class ACO {
    
    private static List<List<Double>> globalDistances; // = new ArrayList<List<Double>>();
    private static List<List<Double>> globalPheromones; // = new ArrayList<List<Double>>();
    private static int globalNumberOfCities;
    
    private static List<List<Double>> choice = new ArrayList<List<Double>>();
    
    private static Random rand = new Random();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        test();
        
//        Map map = new  Map("/Users/jamesboyle/NetBeansProjects/ACO/ALL_tsp/d2103.tsp");
//        map.initializeMap();
//        globalDistances = map.getDistances();
//        globalPheromones = map.getPheromones();    
//        int numberOfCities = map.getNumberOfCities();          
//        globalNumberOfCities = numberOfCities;             
//        double t0 = map.getT0();
//        System.out.println("T0: " +t0);
//        
//       int NUM_ANTS = 15; 
//       AntColony antColony = new AntColony(NUM_ANTS, 200, 1, 3, 0.1, 30, 0.1, 1.00 / t0, 0.9);
//       antColony.setNumberOfCities(numberOfCities);
//
//       // runElitistACO(antColony);
//               
//        long startTime = System.currentTimeMillis();
//       // runACS(antColony);
//        runElitistACO(antColony);
//        
//        long elapsed = System.currentTimeMillis() - startTime;
//        System.out.println("Elapsed:" + elapsed);
    }
    
    private static void test(){
        
        final int NUM_ITERATIONS = 200;
        final double ALPHA = 1.0;
        final double BETA = 3;
        final double EVAPORATION_FACTOR = 0.1;
        final double EPSILON = 0.1;
        final double Q_ZERO = 0.9;
        final int[] ANTS_ARRAY= {10,15,20};
        String[] FILENAMES = {"d2103.tsp", "fl3795.tsp","fnl4461-use.tsp", "rl5915.tsp"};
        
        final int NUM_TRIALS = 5;
        ArrayList<Double> results = new ArrayList<>();
        for(int z = 0; z < 2; z++){
            for(int a = 0; a < FILENAMES.length; a++){
                for(int b = 0; b < ANTS_ARRAY.length; b++){
                    for(int trials = 0; trials < NUM_TRIALS; trials++){
                        System.out.println("START OF RUN FOR TRIAL " + trials);
                        long startTime = System.currentTimeMillis();

                        Map map = new  Map(FILENAMES[a]);
                        map.initializeMap();
                        globalDistances = map.getDistances();
                        globalPheromones = map.getPheromones();    
                        int numberOfCities = map.getNumberOfCities();          
                        globalNumberOfCities = numberOfCities;             
                        double t0 = 1.00 / map.getT0(); 

                        AntColony antColony = new AntColony(ANTS_ARRAY[b], NUM_ITERATIONS, ALPHA, BETA, EVAPORATION_FACTOR,
                                ANTS_ARRAY[b], EPSILON, t0, Q_ZERO);
                        antColony.setNumberOfCities(numberOfCities); 

                        if(z == 0){
                            System.out.println("ACS,"+ANTS_ARRAY[b]+","+NUM_ITERATIONS+","+ALPHA+","+BETA+","+EVAPORATION_FACTOR+","+
                                    ANTS_ARRAY[b]+","+EPSILON+","+t0+","+Q_ZERO+",");
                            runACS(antColony);
                        }
                        else{
                            System.out.println("EAS,"+ANTS_ARRAY[b]+","+NUM_ITERATIONS+","+ALPHA+","+BETA+","+EVAPORATION_FACTOR+","+
                                    ANTS_ARRAY[b]+","+EPSILON+","+t0+","+Q_ZERO+",");
                            runElitistACO(antColony);
                        }
                        long elapsed = System.currentTimeMillis() - startTime;
                        System.out.println("Elapsed time in ms: " + elapsed);
                        System.out.println("END OF RUN FOR TRIAL "+trials);   
                        results.add(antColony.getBestTourLengthSoFar());
                    }
                    System.out.println("Results of 5 trials: " + results.toString());
                    results.clear();
                }
            }  
        }
    }
    
    private static void runACS(AntColony antColony){
        
        boolean runTours = true;
        
        int numberOfIterations = antColony.getNumberOfIterations();
        int counter = 0;
        
        while(runTours) { 
            if(counter % 10 == 0){
                System.out.println("best tour at iteration " + counter +": " +antColony.getBestTourLengthSoFar());
            }
            antColony.initializeAnts();
            precompute(antColony.getAlpha(), antColony.getBeta());
            for(int k = 0; k < globalNumberOfCities; k++) {
                ArrayList<Ant> ants = antColony.getAnts();
                
                for(int i = 0; i < ants.size(); i++) {
                    int currentCity = ants.get(i).getCurrentTourPosition();
                    ants.get(i).setVisited(currentCity);
                    int nextCity = -1;
                    double randomVal = rand.nextDouble();
                    if(randomVal <= antColony.getBestLegProbability()) {
                        nextCity = getBestLeg(ants.get(i), antColony.getAlpha(), antColony.getBeta());
                    } else {
                        nextCity = getNextCity(ants.get(i));
                    }         
                    if(nextCity == -1) {
                        nextCity = ants.get(i).getInitialCityIndex();
                    }                 
                    ants.get(i).updateCurrentTourHistory(nextCity);
                    ants.get(i).setCurrentTourPosition(nextCity);
                    ants.get(i).updateCurrentTourLength(getDistance(currentCity, nextCity));
                    
                    // eating YUM YUM
                    double epsilon = antColony.getEpsilon();
                    double tzero = antColony.getTzero();                                       
                    eatPheromone(currentCity, nextCity, epsilon, tzero);
                    eatPheromone(nextCity, currentCity, epsilon, tzero);
                }
            }
            antColony.setBestTourLengthSoFarAndAddToTourHistory();
            updateGlobalPheromones(antColony, 1);
            
            counter+=1;
            if(counter == numberOfIterations){
                runTours = false;
            }  
        }   
        System.out.println("best tour at iteration " + counter +": " +antColony.getBestTourLengthSoFar());
    }
       
    private static void runElitistACO(AntColony antColony){
        
        boolean runTours = true;
        
        int numberOfIterations = antColony.getNumberOfIterations();
        int counter = 0;
        
        while(runTours){
            if(counter % 10 == 0){
                System.out.println("best tour at iteration " + counter +": " +antColony.getBestTourLengthSoFar());
            }
            antColony.initializeAnts();
            precompute(antColony.getAlpha(), antColony.getBeta());
            
            for(int k = 0; k < globalNumberOfCities; k++){
                ArrayList<Ant> ants = antColony.getAnts(); 
                for(int i = 0; i < ants.size(); i++){
                    int currentCity = ants.get(i).getCurrentTourPosition();
                    ants.get(i).setVisited(currentCity);
                    int nextCity = getNextCity(ants.get(i)); 
                    if(nextCity == -1) {
                        nextCity = ants.get(i).getInitialCityIndex();
                    }
                    ants.get(i).updateCurrentTourHistory(nextCity);
                    ants.get(i).setCurrentTourPosition(nextCity);
                    ants.get(i).updateCurrentTourLength(getDistance(currentCity, nextCity));
                } //END ANTS
            }
            antColony.setBestTourLengthSoFarAndAddToTourHistory();
            updateGlobalPheromones(antColony, 0);           
            counter+=1;
            if(counter == numberOfIterations){
                runTours = false;
            }            
        }
        System.out.println("best tour at iteration " + counter +": " +antColony.getBestTourLengthSoFar());
        //System.out.println(antColony.getBestTourSoFar());
    }
    
    // 0 = Elistist Ant System
    // 1 = Ant Colony System
    private static void updateGlobalPheromones(AntColony antColony, int algType){
                
        for(int i = 0; i < globalNumberOfCities; i++) {
            for(int j = 0; j < globalNumberOfCities; j++) {
                double pheromoneUpdate = (antColony.getEvaporationFactor() * -1) * globalPheromones.get(i).get(j);
                updatePheromone(i, j, pheromoneUpdate);
            }
        }
        //ants pheromone
        //DO FOR ANT COLONY
        if(algType == 1) {
            updateAntsPheromone(antColony);
        }
        
        //best so far pheromone
        updateBSFPheromone(antColony, algType); 
        //flippyThing
        for(int i = 0; i < globalNumberOfCities; i++) {
            for(int j = 0; j < globalNumberOfCities; j++) {
                if (j > i) {
                    globalPheromones.get(j).set(i, globalPheromones.get(i).get(j));  
                }
            }
        }
    }

    private static void updateAntsPheromone(AntColony colony) {
        
        ArrayList<Ant> ants = colony.getAnts();
        
        for(int i = 0; i < ants.size(); i++) {
            for(int j = 0; j < ants.get(i).getTourHistory().size() - 1; j++) {
                
                int city1 = ants.get(i).getTourHistory().get(j);
                int city2 = ants.get(i).getTourHistory().get(j+1);
                
                if (city1 > city2) {
                    int temp = city1;
                    city1 = city2;
                    city2 = temp;
                }
                
                double antsPheromone = 1.00 / ants.get(i).getCurrentTourLength();
                
                updatePheromone(city1, city2, antsPheromone);
            }
            
            int city1 = ants.get(i).getTourHistory().get(ants.get(i).getTourHistory().size() - 1);
            int city2 = ants.get(i).getTourHistory().get(0);
            
            if (city1 > city2) {
                int temp = city1;
                city1 = city2;
                city2 = temp;
            }
                
            double antsPheromone = 1.00 / ants.get(i).getCurrentTourLength();
                
            updatePheromone(city1, city2, antsPheromone); 
        }
        
        
    }
    private static void updateBSFPheromone(AntColony colony, int algType) {
        
        for (int i = 0; i < colony.getBestTourSoFar().size() - 1; i++) {
            
            int city1 = colony.getBestTourSoFar().get(i);
            int city2 = colony.getBestTourSoFar().get(i+1);
                
            if (city1 > city2) {
                int temp = city1;
                city1 = city2;
                city2 = temp;
            }
            
            double multiplier = 0.0;
            
            if (algType == 0) {
                multiplier = colony.getElitismFactor();
            } else if (algType == 1) {
                multiplier = colony.getEvaporationFactor();
            }
            
            double pheromone = multiplier / colony.getBestTourLengthSoFar();

            updatePheromone(city1, city2, pheromone);
        }
        int city1 = colony.getBestTourSoFar().get(0);
        int city2 = colony.getBestTourSoFar().get(colony.getBestTourSoFar().size()-1);

        if (city1 > city2) {
            int temp = city1;
            city1 = city2;
            city2 = temp;
        }

        double pheromone = colony.getElitismFactor() / colony.getBestTourLengthSoFar();

        updatePheromone(city1, city2, pheromone);  
    }
    
    private static int getBestLeg(Ant ant, double alpha, double beta) {
        
        if(ant.getUnvisitedCities().isEmpty()) {
            return -1;
        }
        
        ArrayList<Double> arrayOfNumerators = new ArrayList<>();
        
        //CHANGED
        for(int i = 0; i < ant.getUnvisitedCities().size(); i++){
            double pheromoneDistanceProduct = choice.get(ant.getCurrentTourPosition()).get(ant.getUnvisitedCities().get(i));

            arrayOfNumerators.add(pheromoneDistanceProduct);          
        }
        
        double currBest = 0;
        int bestIndex = -1;
        for(int i = 0; i < arrayOfNumerators.size(); i++) {
            double temp = arrayOfNumerators.get(i);
            if(temp > currBest) {
                currBest = temp;
                bestIndex = i;
            }
        }
        
        return ant.getUnvisitedCities().get(bestIndex);
    }

    
    private static int getNextCity(Ant ant){
        
        double denominatorSum = 0.0;

        ArrayList<Double> arrayOfNumerators = new ArrayList<>();
        
        for(int i = 0; i < ant.getUnvisitedCities().size(); i++){
            double pheromoneDistanceProduct = choice.get(ant.getCurrentTourPosition()).get(ant.getUnvisitedCities().get(i));
            arrayOfNumerators.add(pheromoneDistanceProduct);            
            denominatorSum += pheromoneDistanceProduct;
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
        if (cityToGoToIndex == -1) {
            return -1;
        }
        return ant.getUnvisitedCities().get(cityToGoToIndex);
    }
    
    private static double returnProduct(double distance, double pheromone, double alpha, double beta){  
        
        return Math.pow(pheromone, alpha) * Math.pow( 1.00 / distance, beta);    
    }
    
        
private static void precompute(double alpha, double beta){
    if(!choice.isEmpty()){
        choice.clear();
    }

    for(int i = 0; i < globalDistances.size(); i++){
        List<Double> choices = new ArrayList<>();
        for(int j =0; j < globalDistances.size(); j++){
            double product = returnProduct(globalDistances.get(i).get(j), globalPheromones.get(i).get(j), alpha, beta);

            choices.add(product); 
        }
        choice.add(choices);
    }
}

    
private static double getDistance(int city1, int city2){
        return globalDistances.get(city1).get(city2);
    }
    
private static double getPheromone(int city1, int city2){
        return globalPheromones.get(city1).get(city2);
    }
    
private static void updatePheromone(int city1, int city2, double pheromoneValue){
        double prevValue = globalPheromones.get(city1).get(city2);
        globalPheromones.get(city1).set(city2, pheromoneValue + prevValue);
    }
    
private static void eatPheromone(int city1, int city2, double epsilon, double tzero) {

    double newVal = globalPheromones.get(city1).get(city2) * (1 - epsilon) + (epsilon * tzero);
    globalPheromones.get(city1).set(city2, newVal);
}
    
}

