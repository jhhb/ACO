/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aco;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.*;

/**
 *
 * @author jamesboyle
 * 
 * TODO: updatePheromone needs to be done.
 * 
 * I tested my distance calculations on a very simple file of 3 cities with 2 coordinates,
 * and the resulting 3x3 grid was correct.
 */
public class Map {
    
    private String filename = null;  
    
    //coordinates read from file
    private List<List<Double>> coordinates;
   
    //pheromones, distances arrays
    private List<List<Double>> pheromones;
    private List<List<Double>> distances;
    
    private Random rand = new Random();
 
    private int numCities;
        
    public Map(String filename){
        this.filename = filename;
        this.coordinates = new ArrayList<List<Double>>();
        this.pheromones = new ArrayList<List<Double>>();
        this.distances = new ArrayList<List<Double>>();
        this.numCities = -1;
        
        
    }
    
    //initializes pheromones to be size of numCities x numCities,
    //initializes distances to be numCities x numCities and uses the Distance formula
    //to calculate distance at each point of the List of List
    public void initializeMap(){
        this.readFile();
        //make initialize pheromones
        
        this.calculateDistances(this.distances);
        this.initializePheromones(this.pheromones);

    }
    
    public int getNumberOfCities(){
        return this.numCities;
    }
    
    
    public List<List<Double>> getDistances(){
        return this.distances;
    }
    public List<List<Double>> getPheromones(){
        return this.pheromones;
    }
    
    private void calculateDistances(List<List<Double>> list){
        
        for(int i = 0; i < this.coordinates.size(); i++){
            List<Double> cityCoordinates = getCoordinates(i);
            
            List<Double> distancesForGivenCity = new ArrayList<Double>();
            for(int z = 0; z < this.coordinates.size(); z++){
                if(z == i){
                    distancesForGivenCity.add(0.1);
                }
                else{
                    Double distance = calculateDistance(cityCoordinates, this.coordinates.get(z));
                    distancesForGivenCity.add(distance);
                }
            }
            this.distances.add(distancesForGivenCity);
        }
    }
    
    //distance formula; assumes [0] is x and [y] is y coord for each city and that there are
    //only two coords.
    private double calculateDistance(List<Double> givenCity, List<Double> variableCity){
        
        return Math.sqrt((givenCity.get(0) - variableCity.get(0)) * (givenCity.get(0) - variableCity.get(0))
                + (givenCity.get(1) - variableCity.get(1)) * (givenCity.get(1) - variableCity.get(1)));
    }
      
    private List<Double> getCoordinates(int index){
        return this.coordinates.get(index);
    }
     
    private void readFile(){
        FileInputStream in = null;
        
        try(BufferedReader br = new BufferedReader(new FileReader(this.filename))){
            String line;
            int counter = 0;
            while((line = br.readLine()) != null){
                if(Character.isDigit(line.charAt(0))){
                    ArrayList<Double> coords = new ArrayList<Double>();            
                    String[] splitString = line.split(" ");      
                    for(int i = 1; i < splitString.length; i++){
                        coords.add(Double.parseDouble(splitString[i]));         
                    }
                    counter+=1;
                    this.coordinates.add(coords);
                }   
            }
            System.out.println("Read " + counter + " Cities from " + this.filename);
            this.numCities = counter;
            
            br.close();
        }
        catch( IOException exception){
            System.err.println(exception);
        }
    }
    
    private void initializePheromones(List<List<Double>> list){
        
        double nearestNeighborSum = 0.0;
        
        for(int i = 0; i < this.numCities; i++){
            nearestNeighborSum += getClosestDistance(i);
        }
                
        double initialPheromoneValue = 0.01;//1.00 / (this.numCities * nearestNeighborSum);
        //System.out.println(initialPheromoneValue);
        for(int i = 0; i < this.numCities; i++){
            List<Double> listOfDoubles = new ArrayList<>();
            for(int z = 0; z < this.numCities; z++){
                listOfDoubles.add(initialPheromoneValue);
            }
            
            list.add(listOfDoubles);
        }        
    }
    
        
    public double getNearestNeighborSum(){
        
        double nearestNeighborSum = 0.0;
        
        for(int i = 0; i < this.numCities; i++){
            nearestNeighborSum += getClosestDistance(i);
        }   
        return nearestNeighborSum;
    }
    
    /* This is for nearest neighbor calc for pheromone initialization */
    private double getClosestDistance(int cityIndex){
        int minIndex = Integer.MAX_VALUE;
        
        for(int i = 0; i < this.distances.size(); i++){
            if( this.distances.get(cityIndex).get(i) < minIndex){
                if(this.distances.get(cityIndex).get(i) != 0.1){
                    minIndex = i;
                }
            }
        }
        //returns  distance
        return this.distances.get(cityIndex).get(minIndex);
    }
    
    public String getFileName(){
        return this.filename;
    }
    
    public void printDistances(){
        for(int i = 0; i < this.distances.size(); i++){
            for(int z = 0; z < this.distances.get(i).size(); z++){
                System.out.print(this.distances.get(i).get(z) + " ");
            }
            System.out.println("\n");
        }
    }
    
    public double getT0() {
        
        int startCity = rand.nextInt(this.numCities);
        
        ArrayList<Integer> tour = new ArrayList<>();
        
        double length = 0;
        int newCity = -1;
        int iter = 0;
        while(tour.size() != this.numCities -1){
            
            tour.add(startCity);
            newCity = getNearestNeighbor(startCity, tour);
            length += this.distances.get(startCity).get(newCity);
            startCity = newCity;
            
           // System.out.println(iter);
            iter+=1;
        }
        return length;
}
    
    public int getNearestNeighbor(int startCity, ArrayList<Integer> tour){
        
        //build unvisitedCities
        
        ArrayList<Integer> unvisited = new ArrayList<>();
        
        for(int j = 0; j < this.numCities; j++){
            if(!tour.contains(j)){
                unvisited.add(j);
            }
            
        }
        
        double min = Double.MAX_VALUE;
        int minInd = -1;
        
        for(int k = 0; k < unvisited.size(); k++){
            if(this.distances.get(startCity).get(unvisited.get(k)) < min){
                min = this.distances.get(startCity).get(unvisited.get(k));
                minInd = unvisited.get(k);
            }
        }
        return minInd;
                
    }
    
    
    
   
    
}